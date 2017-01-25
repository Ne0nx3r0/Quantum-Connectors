package com.ne0nx3r0.quantum.impl.nmswrapper;

import org.bukkit.World;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


public class QSWorld {



    private ClassRegistry classRegistry;

    public QSWorld(ClassRegistry classRegistry) {
        this.classRegistry = classRegistry;
    }

    public void setStatic(World world, boolean isStatic) {
        try {
            Object nmsWorld = classRegistry.getNmsWorldField().invoke(world);
            Field field = classRegistry.getIsClientSide();
            field.setAccessible(true);
            field.set(nmsWorld, isStatic);

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
