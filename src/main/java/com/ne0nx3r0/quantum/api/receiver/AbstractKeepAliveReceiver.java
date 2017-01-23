package com.ne0nx3r0.quantum.api.receiver;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Map;

public abstract class AbstractKeepAliveReceiver extends AbstractReceiver {

    // keepAlives - lamps/torches/etc that should stay powered regardless of redstone events
    public final static ArrayList<Block> keepAlives = new ArrayList<>();


    /**
     * only use to getValidMaterials
     */
    public AbstractKeepAliveReceiver() {
        super();
    }

    public AbstractKeepAliveReceiver(Location location) {
        super(location);
    }

    public AbstractKeepAliveReceiver(Location location, Integer delay) {
        super(location, delay);
    }

    public AbstractKeepAliveReceiver(Map<String, Object> map) {
        super(map);
        if (isActive()) keepAlives.add(location.getBlock());
    }

    @Override
    public void setActive(boolean powerOn) throws ValueNotChangedException, ReceiverNotValidException {
        super.setActive(powerOn);
        if (isActive() && !powerOn) {
            keepAlives.remove(location.getBlock());
        } else if (!isActive() && powerOn) {
            keepAlives.add(location.getBlock());
        }

    }
}
