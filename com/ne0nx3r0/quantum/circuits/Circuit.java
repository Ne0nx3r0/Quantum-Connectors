package com.ne0nx3r0.quantum.circuits;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

//TODO: Save player with circuit

//TODO: Allow naming of circuits

public class Circuit{
    //aka location, type
    private List receivers;
    
    public Circuit(){
        this.receivers = new ArrayList<Receiver>();
    }
    public Circuit(List receivers){
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
    public List getReceivers(){
        return receivers;
    }
    
    public int getReceiversCount(){
        return receivers.size();
    }
    
    public void delReceiver(Receiver r){
        receivers.remove(r);
    }
}
