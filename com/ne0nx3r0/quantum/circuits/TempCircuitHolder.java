package com.ne0nx3r0.quantum.circuits;

import org.bukkit.Location;

public class TempCircuitHolder{
    public Location senderLocation;
    public Circuit circuit;
    public int currentType;
    public int currentDelay;

    public TempCircuitHolder(int type, int delay){
        this.currentType = type;
        this.currentDelay = delay;
    }
    
    public void setCircuitType(int type,int delay){
        this.currentType = type;
        this.currentDelay = delay;
    }
    
    public void setSender(Location loc){
        this.senderLocation = loc;
    }
    
    public Location getSender(){
        return this.senderLocation;
    }
    
    
}
