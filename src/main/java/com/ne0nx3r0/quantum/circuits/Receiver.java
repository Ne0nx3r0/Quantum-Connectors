package com.ne0nx3r0.quantum.circuits;

import org.bukkit.Location;

public class Receiver{
    private Location location;
    private int type;
    private int delay = 0;
    private int blockMaterial = -1;
    private byte blockData = -1;
    
    public Receiver(Location location,int type,int delay,int material,byte data){
        this.location = location;
        this.type = type;
        this.delay = delay;
        this.blockMaterial = material;
        this.blockData = data;
    }
    
    public Receiver(Location location,int type,int delay){
        this.location = location;
        this.type = type;
        this.delay = delay;
    }
    
    public Receiver(Location location,int type){
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

    public int getBlockMaterial() {
        return blockMaterial;
    }

    public byte getBlockData() {
        return blockData;
    }
}
