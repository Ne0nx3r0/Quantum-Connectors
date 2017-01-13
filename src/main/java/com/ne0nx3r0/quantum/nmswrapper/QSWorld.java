package com.ne0nx3r0.quantum.nmswrapper;

import net.minecraft.server.v1_11_R1.World;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;

/**
 * Created by ysl3000 on 09.12.16.
 */
public class QSWorld {

    private World nmsWorld;

    public QSWorld(org.bukkit.World world){
        nmsWorld = ((CraftWorld) world).getHandle();
    }


    public void setStatic(boolean isStatic){

        java.lang.reflect.Field field = null;
        try {
            field = World.class.getDeclaredField("isClientSide");
            field.setAccessible(true);
            field.set(nmsWorld, isStatic);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
