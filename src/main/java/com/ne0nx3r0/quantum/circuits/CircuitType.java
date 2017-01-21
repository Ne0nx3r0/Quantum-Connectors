package com.ne0nx3r0.quantum.circuits;

import com.ne0nx3r0.quantum.api.Receiver;
import com.ne0nx3r0.quantum.api.RecieverSetter;
import com.ne0nx3r0.quantum.receiver.base.ReceiverState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    public void calculateAndSet(RecieverSetter recieverSetter, Receiver receiver, ReceiverState oldState, ReceiverState newState) {
        switch (this) {

            case OFF:
                if (newState.compareTo(ReceiverState.S0) == 0 && oldState.compareTo(ReceiverState.S0) == 1) {
                    recieverSetter.setReceiver(receiver, false);
                }
                break;
            case ON:
                if (newState.compareTo(ReceiverState.S0) == 1 && oldState.compareTo(ReceiverState.S0) == 0) {
                    recieverSetter.setReceiver(receiver, true);
                }
                break;
            case QUANTUM:
                recieverSetter.setReceiver(receiver, newState.compareTo(ReceiverState.S0) == 1);
                break;
            case RANDOM:
                if (newState.compareTo(ReceiverState.S0) == 1 && oldState.compareTo(ReceiverState.S0) == 0) {
                    recieverSetter.setReceiver(receiver, new Random().nextBoolean());
                }
                break;

            case REVERSE:
                if (oldState.compareTo(ReceiverState.S0) == 0 || newState.compareTo(ReceiverState.S0) == 0) {
                    recieverSetter.setReceiver(receiver, newState.compareTo(ReceiverState.S1) == -1);
                }
                break;

            case TOGGLE:
                if (newState.compareTo(ReceiverState.S0) == 1 && oldState.compareTo(ReceiverState.S0) == 0) {
                    recieverSetter.setReceiver(receiver, recieverSetter.getState(receiver.getLocation().getBlock()).compareTo(ReceiverState.S1) == -1);
                }
                break;
        }
    }


}