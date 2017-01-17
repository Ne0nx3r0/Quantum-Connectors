package com.ne0nx3r0.quantum.circuits;

public enum CircuitTypes{
    QUANTUM(0),
    ON(1),
    OFF(2),
    TOGGLE(3),
    REVERSE(4),
    RANDOM(5);

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
    
    public static String getName(int id){
        if (id == CircuitTypes.QUANTUM.getId()) {
            return "QUANTUM";
        } else if (id == CircuitTypes.ON.getId()) {
            return "ON";
        } else if (id == CircuitTypes.OFF.getId()) {
            return "OFF";
        } else if (id == CircuitTypes.TOGGLE.getId()) {
            return "TOGGLE";
        } else if (id == CircuitTypes.REVERSE.getId()) {
            return "REVERSE";
        } else if (id == CircuitTypes.RANDOM.getId()) {
            return "RANDOM";
        } else {
            return "unknown";
        }
    }
}