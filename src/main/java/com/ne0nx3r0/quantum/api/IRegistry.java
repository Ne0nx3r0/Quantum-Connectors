package com.ne0nx3r0.quantum.api;

import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

public interface IRegistry<T extends IValidMaterials> {

    void register(JavaPlugin javaPlugin, Class<? extends T>... classes);

    void register(JavaPlugin javaPlugin, Class<? extends T> clazz);

    String getUniqueKey(Class<? extends T> clazz);

    boolean isValid(Block block);
}
