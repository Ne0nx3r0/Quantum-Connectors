package com.ne0nx3r0.quantum.api.receiver;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Yannick on 22.01.2017.
 */
public abstract class AbstractKeepAliveReceiver extends AbstractReceiver {

    // keepAlives - lamps/torches/etc that should stay powered regardless of redstone events
    public final static ArrayList<Block> keepAlives = new ArrayList<>();

    /**
     * only use to getValidMaterials
     */
    protected AbstractKeepAliveReceiver() {
        super();
    }

    protected AbstractKeepAliveReceiver(Location location) {
        super(location);
    }

    protected AbstractKeepAliveReceiver(Location location, Integer delay) {
        super(location, delay);
    }

    protected AbstractKeepAliveReceiver(Map<String, Object> map) {
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
