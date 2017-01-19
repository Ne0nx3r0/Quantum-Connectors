package com.ne0nx3r0.quantum.nmswrapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Class will import specific Minecraft-Version dependent things.
 */
public class ClassRegistry {


    private Class<?> craftWorldClass;
    private Class<?> nmsWorldClass;
    private Method nmsWorldHandle;
    private Field isClientSide;


    public ClassRegistry(String version) throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException {
        this.craftWorldClass = Class.forName("org.bukkit.craftbukkit." + version + ".CraftWorld");
        this.nmsWorldClass = Class.forName("net.minecraft.server." + version + ".World");
        this.nmsWorldHandle = craftWorldClass.getDeclaredMethod("getHandle");
        this.isClientSide = nmsWorldClass.getDeclaredField("isClientSide");
    }


    public Method getNmsWorldField() {
        return nmsWorldHandle;
    }

    public Field getIsClientSide() {
        return isClientSide;
    }
}
