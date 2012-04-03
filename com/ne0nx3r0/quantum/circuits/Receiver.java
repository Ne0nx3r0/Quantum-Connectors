package com.ne0nx3r0.quantum.circuits;

public class Receiver{
    public int type;
    public int delay = 0;
    
    public Receiver(int type,int delay){
        this.type = type;
        this.delay = delay;
    }
    
    public Receiver(int type){
        this.type = type;
    }
}
