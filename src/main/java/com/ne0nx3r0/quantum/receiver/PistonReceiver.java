package com.ne0nx3r0.quantum.receiver;

import org.bukkit.Location;
import org.bukkit.material.PistonBaseMaterial;

/**
 * Created by ysl3000 on 14.01.17.
 */
public class PistonReceiver extends AbstractReceiver {
    public PistonReceiver(Location location, int type) {
        super(location, type);
    }

    public PistonReceiver(Location location, int type, int delay) {
        super(location, type, delay);
    }

    @Override
    public String getType() {
        return "qc:" + getClass().getSimpleName();
    }

    @Override
    public boolean isActive() {
        return ((PistonBaseMaterial) location.getBlock().getState().getData()).isPowered();
    }

    @Override
    public void setActive(boolean powerOn) {
        ((PistonBaseMaterial) location.getBlock().getState().getData()).setPowered(powerOn);
        location.getBlock().getState().update();
    }

    @Override
    public boolean isValid() {
        return location.getBlock().getState().getData() instanceof PistonBaseMaterial;
    }
}
