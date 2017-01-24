package com.ne0nx3r0.quantum.api;

import org.bukkit.block.Block;

public interface IRegistry<T extends IValidMaterials> {

    void register(QuantumExtension quantumExtension, Class<? extends T>... classes);

    void register(QuantumExtension quantumExtension, Class<? extends T> clazz);

    String getUniqueKey(Class<? extends T> clazz);

    boolean isValid(Block block);
}
