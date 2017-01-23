package com.ne0nx3r0.quantum.receiver.base;

import com.ne0nx3r0.quantum.api.IReceiverRegistry;
import com.ne0nx3r0.quantum.api.receiver.AbstractReceiver;
import com.ne0nx3r0.quantum.interfaces.Receiver;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yannick on 19.01.2017.
 */
public class ReceiverRegistry implements IReceiverRegistry {

    private final ReceiverMap receiverMap = new ReceiverMap();

    private static AbstractReceiver getReceiver(Class<? extends AbstractReceiver> receiver) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<? extends AbstractReceiver> constructor = receiver.getConstructor();

        if (constructor != null) {
            return constructor.newInstance();
        }
        return null;
    }

    public void registerReceiver(JavaPlugin javaPlugin, Class<? extends AbstractReceiver> receiver) {
        String uniqueKey = javaPlugin.getName() + ":" + receiver
                .getSimpleName();
        receiverMap.put(uniqueKey, receiver);

        receiverMap.put(receiver, uniqueKey);

        try {
            AbstractReceiver abstractReceiver = getReceiver(receiver);

            if (abstractReceiver != null) {
                for (Material material : abstractReceiver.getValidMaterials()) {
                    receiverMap.put(material, receiver);
                }
            }


        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }


    }

    public void registerReceiver(JavaPlugin javaPlugin, Class<? extends AbstractReceiver>... receivers) {
        for (Class<? extends AbstractReceiver> receiver : receivers)
            registerReceiver(javaPlugin, receiver);
    }

    public String getUniqueKey(Class<? extends AbstractReceiver> receiver) {
        return receiverMap.get(receiver);
    }

    public Constructor<? extends AbstractReceiver> getReceiverInstance(String receiverType) throws NoSuchMethodException {

        Class<? extends AbstractReceiver> clazz = receiverMap.get(receiverType);

        if (clazz == null) return null;

        Constructor<? extends AbstractReceiver> constructor = clazz.getConstructor(Map.class);
        if (constructor == null) {
            return null;
        }
        return constructor;
    }

    public List<Class<? extends AbstractReceiver>> fromType(Location location) {
        Material m = location.getBlock().getType();
        if (receiverMap.contains(m)) {
            return receiverMap.get(m);
        }
        return null;
    }

    public Receiver instantiateFrom(Class<? extends AbstractReceiver> receiverClass, Location location, int delay) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return receiverClass.getConstructor(Location.class, Integer.class).newInstance(location, delay);
    }


    public boolean isValidReceiver(Block block) {
        return receiverMap.contains(block.getType());
    }


    private final class ReceiverMap {

        private final Map<String, Class<? extends AbstractReceiver>> receiverMap = new HashMap<>();
        private final Map<Material, List<Class<? extends AbstractReceiver>>> receiverMapMaterial = new HashMap<>();
        private final Map<Class<? extends AbstractReceiver>, String> receiverMapNamedID = new HashMap<>();


        public Class<? extends AbstractReceiver> put(String key, Class<? extends AbstractReceiver> receiver) {
            return receiverMap.put(key, receiver);
        }

        public Class<? extends AbstractReceiver> put(Material material, Class<? extends AbstractReceiver> receiver) {
            List<Class<? extends AbstractReceiver>> abstClassList = receiverMapMaterial.get(material);

            if (abstClassList == null) {
                abstClassList = new ArrayList<>();
                receiverMapMaterial.put(material, abstClassList);
            }

            return abstClassList.add(receiver) ? receiver : null;
        }

        public String put(Class<? extends AbstractReceiver> receiver, String receiverType) {
            return receiverMapNamedID.put(receiver, receiverType);
        }

        public List<Class<? extends AbstractReceiver>> get(Material type) {
            return receiverMapMaterial.get(type);
        }

        public Class<? extends AbstractReceiver> get(String type) {
            return receiverMap.get(type);
        }

        public String get(Class<? extends AbstractReceiver> receiverClass) {
            return receiverMapNamedID.get(receiverClass);
        }

        public boolean contains(Material material) {
            return receiverMapMaterial.containsKey(material);
        }

        public boolean contains(Class<? extends AbstractReceiver> receiverClass) {
            return receiverMapNamedID.containsKey(receiverClass);
        }

        public boolean contains(String type) {
            return receiverMap.containsKey(type);
        }


    }

}
