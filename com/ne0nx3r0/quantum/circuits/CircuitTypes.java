package com.ne0nx3r0.quantum.circuits;

public enum CircuitTypes{
    QUANTUM(0),
    ON(1),
    OFF(2),
    TOGGLE(3),
    REVERSE(4),
    RANDOM(5);
//    BLOCK(6),

    public int id;
    public String name;

    CircuitTypes(int id, String name){
        this.id = id;
        this.name = name;
    }

    CircuitTypes(int id){
        this.id = id;
        this.name = this.name().toLowerCase();
    }

    public int getId(){
        return id;
    }
}