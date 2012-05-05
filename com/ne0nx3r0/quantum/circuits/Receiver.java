package com.ne0nx3r0.quantum.circuits;

import org.bukkit.Location;

public class Receiver{
    public Location location;
    public int type;
    public int delay = 0;
    public int blockMaterial = -1;
    public byte blockData = -1;
    
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
}
