package com.ne0nx3r0.quantum.receiver;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.material.Lever;

/**
 * Created by ysl3000 on 14.01.17.
 */
public class LeverReceiver extends AbstractReceiver {
    public LeverReceiver(Location location, int type) {
        super(location, type);
    }

    public LeverReceiver(Location location, int type, int delay) {
        super(location, type, delay);
    }

    /**
     * default receivers return "qc:LeverReceiver"
     *
     * @return the namedType of the receiver
     */
    @Override
    public String getType() {
        return "qc:" + getClass().getSimpleName();
    }

    @Override
    public boolean isActive() {
        return ((Lever) location.getBlock().getState().getData()).isPowered();
    }

    @Override
    public void setActive(boolean powerOn) {
        BlockState state = location.getBlock().getState();
        Lever lever = (Lever) state.getData();
        lever.setPowered(powerOn);
        state.setData(lever);
        state.update();
    }

    @Override
    public boolean isValid() {
        return location.getBlock().getState().getData() instanceof Lever;
    }
}
