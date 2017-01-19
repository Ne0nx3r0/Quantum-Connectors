package com.ne0nx3r0.quantum.receiver;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

/**
 * Created by ysl3000 on 14.01.17.
 */
public class OpenableReceiver extends AbstractReceiver {
    public OpenableReceiver(Location location, int type) {
        super(location, type);
    }

    public OpenableReceiver(Location location, int type, int delay) {
        super(location, type, delay);
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
