package com.ne0nx3r0.quantum.circuits;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

//TODO: Allow naming of circuits

public class Circuit{
    private List<Receiver> receivers;
    private String playerName;
    
    public Circuit(String player){
        this.playerName = player;
        this.receivers = new ArrayList<Receiver>();
    }
    public Circuit(String player,List receivers){
        this.playerName = player;
        this.receivers = receivers;
    }
    
    public void addReceiver(Location loc,int type,int delay){
        receivers.add(new Receiver(loc,type,delay));
    }
    
    public void setReceivers(List receivers){
        this.receivers = receivers;
    }
    
    public Receiver getReceiver(int index){
        return (Receiver) receivers.get(index);
    }
    
    public List<Receiver> getReceivers(){
        return receivers;
    }
    
    public int getReceiversCount(){
        return receivers.size();
    }
    
    public void delReceiver(Receiver r){
        receivers.remove(r);
    }
    
    public void setOwner(String player){
        this.playerName = player;
    }
    
    public String getOwner(){
        return playerName;
    }
}
