package com.ne0nx3r0.quantum.circuits;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;

public class Receiver {
    private Location location;
    private int type;
    private int delay = 0;

    public Receiver(Location location, int type, int delay) {
        this.location = location;
        this.type = type;
        this.delay = delay;
    }

    public Receiver(Location location, int type) {
        this.location = location;
        this.type = type;
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


    public void setActive(boolean powerOn) {

        Block block = location.getBlock();
        Material material = block.getType();

        BlockState state = block.getState();
        MaterialData data = state.getData();

    }
}
