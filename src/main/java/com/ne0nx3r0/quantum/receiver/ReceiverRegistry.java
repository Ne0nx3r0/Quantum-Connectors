package com.ne0nx3r0.quantum.receiver;

import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yannick on 19.01.2017.
 */
public class ReceiverRegistry {

    private static Map<String, Class<? extends AbstractReceiver>> receiverMap = new HashMap<>();


    public static void registerReceiver(JavaPlugin javaPlugin, Class<? extends AbstractReceiver> receiver) {
        receiverMap.put(javaPlugin.getName() + receiver
                .getSimpleName(), receiver);
    }

    public static void registerReceiver(JavaPlugin javaPlugin, Class<? extends AbstractReceiver>... receivers) {
        for (Class<? extends AbstractReceiver> receiver : receivers)
            registerReceiver(javaPlugin);
    }


    public static Constructor<? extends AbstractReceiver> getReceiverInstance(String receiverType) throws NoSuchMethodException {
        return receiverMap.get(receiverType).getConstructor(Map.class);
    }
}
