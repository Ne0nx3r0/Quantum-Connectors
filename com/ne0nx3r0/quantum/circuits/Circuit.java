package com.ne0nx3r0.quantum.circuits;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;

public class Circuit{
    //aka location, type
    private Map<Location,Receiver> receivers;
    
    public Circuit(){
        receivers = new HashMap<Location,Receiver>();
    }
    public Circuit(Map<Location,Receiver> map){
        receivers = map;
    }

    public void addReceiver(Location loc,int type,int delay){
        receivers.put(loc,new Receiver(type,delay));
    }
    
    public void setReceivers(Map<Location,Receiver> map){
        receivers = map;
    }
    
    public Boolean getReceiver(Location loc){
        return receivers.containsKey(loc);
    }
    public Map<Location, Receiver> getReceivers(){
        return receivers;
    }
    
    public void delReceiver(Location loc){
        receivers.remove(loc);
    }
}
