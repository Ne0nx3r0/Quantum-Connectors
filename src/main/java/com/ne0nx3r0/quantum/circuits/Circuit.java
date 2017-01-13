package com.ne0nx3r0.quantum.circuits;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

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
    
    public Receiver getReceiver(int index){
        return receivers.get(index);
    }
    
    public List<Receiver> getReceivers(){
        return receivers;
    }

    public void setReceivers(List receivers) {
        this.receivers = receivers;
    }
    
    public int getReceiversCount(){
        return receivers.size();
    }
    
    public void delReceiver(Receiver r){
        receivers.remove(r);
    }
    
    public String getOwner(){
        return playerName;
    }

    public void setOwner(String player) {
        this.playerName = player;
    }
}
