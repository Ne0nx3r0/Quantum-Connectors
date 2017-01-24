package com.ne0nx3r0.quantum.impl.nmswrapper;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Class will import specific Minecraft-Version dependent things.
 */
public class ClassRegistry {
    private String apiVersion;
    private Class<?> craftWorldClass;
    private Class<?> nmsWorldClass;
    private Method nmsWorldHandle;
    private Field isClientSide;


    public ClassRegistry() {

        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        this.apiVersion = packageName.substring(packageName.lastIndexOf('.') + 1);

        try {
            this.craftWorldClass = Class.forName("org.bukkit.craftbukkit." + apiVersion + ".CraftWorld");
            this.nmsWorldClass = Class.forName("net.minecraft.server." + apiVersion + ".World");
            this.nmsWorldHandle = craftWorldClass.getDeclaredMethod("getHandle");
            this.isClientSide = nmsWorldClass.getDeclaredField("isClientSide");
        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


    public Method getNmsWorldField() {
        return nmsWorldHandle;
    }

    public Field getIsClientSide() {
        return isClientSide;
    }

    public String getApiVersion() {
        return apiVersion;
    }
}
