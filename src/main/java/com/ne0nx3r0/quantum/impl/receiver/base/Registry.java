package com.ne0nx3r0.quantum.impl.receiver.base;

import com.ne0nx3r0.quantum.api.IRegistry;
import com.ne0nx3r0.quantum.api.IValidMaterials;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class Registry<T extends IValidMaterials> implements IRegistry<T> {

    private final QuantumMap<T> typeMap = new QuantumMap<>();

    @Override
    public final void register(JavaPlugin javaPlugin, Class<? extends T>... classes) {
        for (Class<? extends T> clazz : classes)
            register(javaPlugin, clazz);
    }

    @Override
    public final void register(JavaPlugin javaPlugin, Class<? extends T> clazz) {

        String uniqueKey = javaPlugin.getName() + ":" + clazz
                .getSimpleName();
        typeMap.put(uniqueKey, clazz);

        typeMap.put(clazz, uniqueKey);

        try {
            T object = getObject(clazz);

            if (object != null) {
                for (Material material : object.getValidMaterials()) {
                    typeMap.put(material, clazz);
                }
            }


        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }


    }

    @Override
    public final String getUniqueKey(Class<? extends T> clazz) {
        return typeMap.get(clazz);
    }

    @Override
    public final boolean isValid(Block block) {
        return typeMap.contains(block.getType());
    }

    public final T getObject(Class<? extends T> receiver) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<? extends T> constructor = receiver.getConstructor();

        if (constructor != null) {
            return constructor.newInstance();
        }
        return null;
    }

    public final Constructor<? extends T> getInstance(String type) throws NoSuchMethodException {
        Class<? extends T> clazz = typeMap.get(type);

        if (clazz == null) return null;

        Constructor<? extends T> constructor = clazz.getConstructor(Map.class);
        if (constructor == null) {
            return null;
        }
        return constructor;
    }

    public final List<Class<? extends T>> fromType(Location location) {
        Material m = location.getBlock().getType();
        if (typeMap.contains(m)) {
            return typeMap.get(m);
        }
        return null;
    }

    public final T instantiateFrom(Class<? extends T> clazz, Location location, int delay) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return clazz.getConstructor(Location.class, Integer.class).newInstance(location, delay);
    }


}
