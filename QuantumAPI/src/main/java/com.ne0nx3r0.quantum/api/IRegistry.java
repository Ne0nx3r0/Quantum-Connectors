package com.ne0nx3r0.quantum.api;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public interface IRegistry<T extends IValidMaterials> {

    void register(QuantumExtension quantumExtension, Class<? extends T>... classes);

    void register(QuantumExtension quantumExtension, Class<? extends T> clazz);

    Set<Material> getMaterials();

    Set<String> getNames();

    String getUniqueKey(Class<? extends T> clazz);

    boolean isValid(Block block);

    Class<? extends T> getFromUniqueKey(String uniqueKey);

    Constructor<? extends T> getInstance(String type) throws NoSuchMethodException;

    T instantiateFrom(Class<? extends T> clazz, Location location, int delay) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;
}
