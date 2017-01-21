package com.ne0nx3r0.quantum.receiver;

import com.ne0nx3r0.quantum.api.Receiver;
import com.ne0nx3r0.quantum.receiver.base.AbstractReceiver;
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
public class ReceiverRegistry {

    private final static ReceiverMap receiverMap = new ReceiverMap();


    public static void registerReceiver(JavaPlugin javaPlugin, Class<? extends AbstractReceiver> receiver) {
        receiverMap.put(javaPlugin.getName().replaceAll("[a-z]", "").toLowerCase() + ":" + receiver
                .getSimpleName(), receiver);

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

    public static void registerReceiver(JavaPlugin javaPlugin, Class<? extends AbstractReceiver>... receivers) {
        for (Class<? extends AbstractReceiver> receiver : receivers)
            registerReceiver(javaPlugin, receiver);
    }


    public static Constructor<? extends AbstractReceiver> getReceiverInstance(String receiverType) throws NoSuchMethodException {

        Class<? extends AbstractReceiver> clazz = receiverMap.get(receiverType);

        if (clazz == null) return null;

        Constructor<? extends AbstractReceiver> constructor = clazz.getConstructor(Map.class);
        if (constructor == null) {
            return null;
        }
        return constructor;
    }

    private static AbstractReceiver getReceiver(Class<? extends AbstractReceiver> receiver) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<? extends AbstractReceiver> constructor = receiver.getConstructor();

        if (constructor != null) {
            return constructor.newInstance();
        }
        return null;
    }


    public static List<Class<? extends AbstractReceiver>> fromType(Location location) {
        Material m = location.getBlock().getType();
        if (receiverMap.contains(m)) {
            return receiverMap.get(m);
        }
        return null;
    }

    public static Receiver instantiatFrom(Class<? extends AbstractReceiver> receiverClass, Location location, int delay) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return receiverClass.getConstructor(Location.class, Integer.class).newInstance(location, delay);
    }


    public static boolean isValidReceiver(Block block) {
        return receiverMap.contains(block.getType());
    }


    private final static class ReceiverMap {

        private final Map<String, Class<? extends AbstractReceiver>> receiverMap = new HashMap<>();
        private final Map<Material, List<Class<? extends AbstractReceiver>>> receiverMapMaterial = new HashMap<>();


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

        public List<Class<? extends AbstractReceiver>> get(Material type) {
            return receiverMapMaterial.get(type);
        }

        public Class<? extends AbstractReceiver> get(String type) {
            return receiverMap.get(type);
        }

        public boolean contains(Material material) {
            return receiverMapMaterial.containsKey(material);
        }

    }

}
