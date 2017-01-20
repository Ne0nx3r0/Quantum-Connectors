package com.ne0nx3r0.quantum.receiver;

import com.ne0nx3r0.quantum.api.Receiver;
import com.ne0nx3r0.quantum.nmswrapper.QSWorld;
import com.ne0nx3r0.quantum.utils.ValidMaterials;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yannick on 19.01.2017.
 */
public class ReceiverRegistry {

    private final static Map<String, Class<? extends AbstractReceiver>> receiverMap = new HashMap<>();


    public static void registerReceiver(JavaPlugin javaPlugin, Class<? extends AbstractReceiver> receiver) {
        //ConfigurationSerialization.registerClass(receiver);
        receiverMap.put(javaPlugin.getName().replaceAll("[a-z]", "").toLowerCase() + ":" + receiver
                .getSimpleName(), receiver);
    }

    public static void registerReceiver(JavaPlugin javaPlugin, Class<? extends AbstractReceiver>... receivers) {
        for (Class<? extends AbstractReceiver> receiver : receivers)
            registerReceiver(javaPlugin, receiver);
    }


    public static Constructor<? extends AbstractReceiver> getReceiverInstance(String receiverType) throws NoSuchMethodException {

        Class<? extends AbstractReceiver> clazz = receiverMap.get(receiverType);

        Constructor<? extends AbstractReceiver> constructor = clazz.getConstructor(Map.class);

        if (constructor == null) {
            System.out.println("Constructor is null");
            return null;
        }


        return constructor;
    }

    public static Receiver fromType(Location location, long delay, QSWorld qsWorld) {
        Material m = location.getBlock().getType();

        if (ValidMaterials.LAMP.contains(m)) {
            return new RedstoneLampReceiver(location, delay, qsWorld);
        } else if (ValidMaterials.OPENABLE.contains(m)) {
            return new OpenableReceiver(location, delay);
        } else if (ValidMaterials.LEVER.contains(m)) {
            return new LeverReceiver(location, delay);
        } else if (ValidMaterials.RAIL.contains(m)) {
            return new PoweredRailReceiver(location, delay);
        } else if (ValidMaterials.PISTON.contains(m)) {
            return new PistonReceiver(location, delay);
        }
        return null;
    }

}
