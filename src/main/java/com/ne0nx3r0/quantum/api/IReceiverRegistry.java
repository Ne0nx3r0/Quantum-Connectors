package com.ne0nx3r0.quantum.api;

import com.ne0nx3r0.quantum.api.receiver.AbstractReceiver;
import com.ne0nx3r0.quantum.interfaces.Receiver;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Yannick on 23.01.2017.
 */
public interface IReceiverRegistry {
    void registerReceiver(JavaPlugin javaPlugin, Class<? extends AbstractReceiver> receiver);

    void registerReceiver(JavaPlugin javaPlugin, Class<? extends AbstractReceiver>... receivers);

    Constructor<? extends AbstractReceiver> getReceiverInstance(String receiverType) throws NoSuchMethodException;

    List<Class<? extends AbstractReceiver>> fromType(Location location);

    String getUniqueKey(Class<? extends AbstractReceiver> receiver);

    Receiver instantiateFrom(Class<? extends AbstractReceiver> receiverClass, Location location, int delay) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;

    boolean isValidReceiver(Block block);
}
