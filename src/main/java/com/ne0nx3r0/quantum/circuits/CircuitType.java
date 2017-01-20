package com.ne0nx3r0.quantum.circuits;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum CircuitType {
    QUANTUM(0),
    ON(1),
    OFF(2),
    TOGGLE(3),
    REVERSE(4),
    RANDOM(5);

    private final static Map<Integer, CircuitType> BY_ID = new HashMap<>();
    private final static Map<String, CircuitType> BY_NAME = new HashMap<>();

    static {

        Arrays.asList(CircuitType.values()).forEach(c -> {
            BY_ID.put(c.id, c);
            BY_NAME.put(c.name, c);
        });

    }

    public final int id;
    public final String name;

    CircuitType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    CircuitType(int id) {
        this.id = id;
        this.name = this.name().toLowerCase();
    }

    public static CircuitType getByID(int id) {
        return BY_ID.get(id);
    }

    public static CircuitType getByName(String name) {
        return BY_NAME.get(name);
    }


}