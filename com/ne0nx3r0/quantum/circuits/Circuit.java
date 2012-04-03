package com.ne0nx3r0.quantum.circuits;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;

public class Circuit{
    //aka location, type
    private Map<Location,Integer> receivers;
    
    public Circuit(){
        receivers = new HashMap<Location,Integer>();
    }
    public Circuit(Map<Location,Integer> map){
        receivers = map;
    }

    public void addReceiver(Location loc,int type){
        receivers.put(loc,new Integer(type));
    }
    public void setReceivers(Map<Location,Integer> map){
        receivers = map;
    }
    
    public Boolean getReceiver(Location loc){
        return receivers.containsKey(loc);
    }
    public Map<Location, Integer> getReceivers(){
        return receivers;
    }
    
    public void delReceiver(Location loc){
        receivers.remove(loc);
    }
}
