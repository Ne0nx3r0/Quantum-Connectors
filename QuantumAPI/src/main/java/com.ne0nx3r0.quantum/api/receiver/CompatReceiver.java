package com.ne0nx3r0.quantum.api.receiver;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yannick on 24.01.2017.
 */
public final class CompatReceiver implements Receiver {

    private HashMap<String, Object> receiver;


    public CompatReceiver(HashMap<String, Object> map) {
        this.receiver = map;
    }


    @Override
    public Location getLocation() {
        return null;
    }

    /**
     * Will calculate real location for doubleBlocks
     */
    @Override
    public void calculateRealLocation() {

    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public long getDelay() {
        return 0;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setActive(boolean powerOn) throws ValueNotChangedException, ReceiverNotValidException {

    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public int getBlockCurrent() {
        return 0;
    }

    @Override
    public Map<String, Object> serialize() {
        return receiver;
    }
}
