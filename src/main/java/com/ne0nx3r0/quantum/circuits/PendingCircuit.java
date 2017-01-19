package com.ne0nx3r0.quantum.circuits;

import org.bukkit.Location;

import java.util.UUID;

public class PendingCircuit {
    private Location senderLocation;
    private Circuit circuit;
    private long currentDelay;
    private CircuitTypes circuitType;

    public PendingCircuit(UUID ownerUUID, CircuitTypes type, int delay, CircuitManager cmanager) {
        currentDelay = delay;
        this.circuitType = type;
        circuit = new Circuit(ownerUUID, cmanager, circuitType);
    }

    public void setCircuitType(CircuitTypes type, long delay) {
        circuitType = type;
        currentDelay = delay;
    }

    public CircuitTypes getCurrentType() {
        return circuitType;
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
        this.circuit.addReceiver(loc, this.currentDelay);
    }

    public boolean hasReceiver() {
        return circuit.getReceiversCount() > 0;
    }
}
