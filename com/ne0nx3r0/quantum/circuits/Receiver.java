package com.ne0nx3r0.quantum.circuits;

import org.bukkit.Location;

public class Receiver{
    public int type;
    public int delay = 0;
    public Location location;
    
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
