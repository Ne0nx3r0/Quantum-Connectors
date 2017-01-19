package com.ne0nx3r0.quantum.circuits;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum CircuitTypes {
    QUANTUM(0),
    ON(1),
    OFF(2),
    TOGGLE(3),
    REVERSE(4),
    RANDOM(5);

    private final static Map<Integer, CircuitTypes> BY_ID = new HashMap<>();
    private final static Map<String, CircuitTypes> BY_NAME = new HashMap<>();

    static {

        Arrays.asList(CircuitTypes.values()).forEach(c -> {
            BY_ID.put(c.id, c);
            BY_NAME.put(c.name, c);
        });


    }

    public final int id;
    public final String name;

    CircuitTypes(int id, String name) {
        this.id = id;
        this.name = name;
    }

    CircuitTypes(int id) {
        this.id = id;
        this.name = this.name().toLowerCase();
    }

    public static CircuitTypes getByID(int id) {
        return BY_ID.get(id);
    }

    public static CircuitTypes getByName(String name) {
        return BY_NAME.get(name);
    }


}