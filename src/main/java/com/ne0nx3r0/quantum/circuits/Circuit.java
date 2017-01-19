package com.ne0nx3r0.quantum.circuits;

import com.ne0nx3r0.quantum.api.Receiver;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

//TODO: Allow naming of circuits

public class Circuit {
    private List<Receiver> receivers;
    private UUID playerUUID;
    private CircuitManager circuitManager;
    private CircuitTypes circuitTypes;

    public Circuit(UUID playerUUID, CircuitManager cmanager, CircuitTypes circuitTypes) {
        this(playerUUID, Collections.EMPTY_LIST, cmanager, circuitTypes);
    }

    public Circuit(UUID playerUUID, List<Receiver> receivers, CircuitManager cmanager, CircuitTypes circuitTypes) {
        this.playerUUID = playerUUID;
        this.circuitTypes = circuitTypes;
        this.receivers = new ArrayList<>(receivers);
        circuitManager = cmanager;
    }

    public void addReceiver(Location loc, long delay) {
        receivers.add(circuitManager.fromType(loc, delay));
    }

    public List<Receiver> getReceivers() {
        return receivers;
    }

    public int getReceiversCount() {
        return receivers.size();
    }

    public void delReceiver(Receiver r) {
        receivers.remove(r);
    }

    public UUID getOwner() {
        return playerUUID;

    }


    public void setOwner(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public CircuitTypes getCircuitType() {
        return circuitTypes;
    }
}
