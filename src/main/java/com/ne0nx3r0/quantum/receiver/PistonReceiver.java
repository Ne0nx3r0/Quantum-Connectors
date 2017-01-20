package com.ne0nx3r0.quantum.receiver;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.material.PistonBaseMaterial;

import java.util.Map;

@SerializableAs("PistonReceiver")
public class PistonReceiver extends AbstractReceiver {
    public PistonReceiver(Location location) {
        super(location);
    }

    public PistonReceiver(Map<String, Object> map) {
        super(map);
    }

    public PistonReceiver(Location location, long delay) {
        super(location, delay);
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
