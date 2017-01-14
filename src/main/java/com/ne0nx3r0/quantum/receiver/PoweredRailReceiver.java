package com.ne0nx3r0.quantum.receiver;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.material.PoweredRail;

/**
 * Created by ysl3000 on 14.01.17.
 */
public class PoweredRailReceiver extends Receiver {
    public PoweredRailReceiver(Location location, int type) {
        super(location, type);
    }

    public PoweredRailReceiver(Location location, int type, int delay) {
        super(location, type, delay);
    }

    @Override
    public void setActive(boolean powerOn) {

        BlockState state = location.getBlock().getState();
        PoweredRail poweredRail = (PoweredRail) state.getData();
        poweredRail.setPowered(powerOn);
        state.setData(poweredRail);
        state.update();

    }
}
