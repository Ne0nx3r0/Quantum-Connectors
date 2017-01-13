package com.ne0nx3r0.quantum.nmswrapper;

import org.bukkit.World;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by ysl3000 on 09.12.16.
 */
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
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
