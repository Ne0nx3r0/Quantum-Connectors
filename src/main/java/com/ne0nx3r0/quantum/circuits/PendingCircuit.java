package com.ne0nx3r0.quantum.circuits;

import org.bukkit.Location;

import java.util.UUID;

public class PendingCircuit {
    private Location senderLocation;
    private Circuit circuit;
    private int currentType;
    private int currentDelay;

    public PendingCircuit(UUID ownerUUID, int type, int delay, CircuitManager cmanager) {
        currentType = type;
        currentDelay = delay;
        circuit = new Circuit(ownerUUID, cmanager);
    }

    public void setCircuitType(int type, int delay) {
        currentType = type;
        currentDelay = delay;
    }

    public int getCurrentType() {
        return currentType;
    }

    public Location getSenderLocation() {
        return senderLocation;
    }

    public void setSenderLocation(Location loc) {
        senderLocation = loc;
    }

    public boolean hasSenderLocation() {
        return (senderLocation != null);
    }

    public Circuit getCircuit() {
        return this.circuit;
    }

    // Note there is no remove receiver function, because this is a one way road
    // If the player cancels, this whole object gets blown away. 
    public void addReceiver(Location loc) {
        this.circuit.addReceiver(loc, this.currentType, this.currentDelay);
    }

    public boolean hasReceiver() {
        return circuit.getReceiversCount() > 0;
    }
}
