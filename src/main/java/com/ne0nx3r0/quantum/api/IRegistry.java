package com.ne0nx3r0.quantum.api;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by ysl3000 on 23.01.17.
 */
public interface IRegistry<T extends IValidMaterials> {

    void register(JavaPlugin javaPlugin, Class<? extends T>... classes);

    void register(JavaPlugin javaPlugin, Class<? extends T> clazz);

    T getObject(Class<? extends T> receiver) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;

    String getUniqueKey(Class<? extends T> clazz);

    Constructor<? extends T> getInstance(String type) throws NoSuchMethodException;

    List<Class<? extends T>> fromType(Location location);

    T instantiateFrom(Class<? extends T> clazz, Location location, int delay) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;

    boolean isValid(Block block);
}
