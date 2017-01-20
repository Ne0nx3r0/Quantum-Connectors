package com.ne0nx3r0.quantum.receiver;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.material.PoweredRail;

import java.util.Map;

public class PoweredRailReceiver extends AbstractReceiver {
    public PoweredRailReceiver(Location location) {
        super(location);
    }

    public PoweredRailReceiver(Location location, long delay) {
        super(location, delay);
    }

    public PoweredRailReceiver(Map<String, Object> map) {
        super(map);
    }

    @Override
    public String getType() {
        return "qc:" + getClass().getSimpleName();
    }

    @Override
    public boolean isActive() {
        return ((PoweredRail) location.getBlock().getState().getData()).isPowered();
    }

    @Override
    public void setActive(boolean powerOn) {

        BlockState state = location.getBlock().getState();
        PoweredRail poweredRail = (PoweredRail) state.getData();
        poweredRail.setPowered(powerOn);
        state.setData(poweredRail);
        state.update();

    }

    @Override
    public boolean isValid() {
        return location.getBlock().getState().getData() instanceof PoweredRail;
    }
}
