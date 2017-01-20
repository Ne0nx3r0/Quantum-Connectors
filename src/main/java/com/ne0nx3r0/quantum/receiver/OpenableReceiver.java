package com.ne0nx3r0.quantum.receiver;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

@SerializableAs("OpenableReceiver")
public class OpenableReceiver extends AbstractReceiver {
    public OpenableReceiver(Location location) {
        super(location);
    }

    public OpenableReceiver(Location location, long delay) {
        super(location, delay);
    }

    @Override
    public String getType() {
        return "qc:" + getClass().getSimpleName();
    }

    @Override
    public boolean isActive() {
        return ((Openable) location.getBlock().getState().getData()).isOpen();
    }

    @Override
    public void setActive(boolean powerOn) {
        BlockState state = location.getBlock().getState();
        MaterialData data = state.getData();
        ((Openable) data).setOpen(powerOn);
        state.setData(data);
        state.update();
        location.getWorld().playEffect(location, Effect.DOOR_TOGGLE, 0, 10);
    }

    @Override
    public boolean isValid() {
        return location.getBlock().getState().getData() instanceof Openable;
    }
}
