package com.ne0nx3r0.quantum.receiver;

import org.bukkit.Location;


public abstract class Receiver {

    protected Location location;
    protected int type;
    protected int delay;


    public Receiver(Location location, int type) {
        this(location, type, 0);
    }

    public Receiver(Location location, int type, int delay) {
        this.location = location;
        this.type = type;
        this.delay = delay;
    }

    public Location getLocation() {
        return location;
    }

    public int getType() {
        return type;
    }

    public int getDelay() {
        return delay;
    }

    @Deprecated
    public int getBlockMaterial() {
        return location.getBlock().getTypeId();
    }

    @Deprecated
    public byte getBlockData() {
        return location.getBlock().getData();
    }

    public abstract void setActive(boolean powerOn);
}
