package com.ne0nx3r0.quantum.circuits;

import com.ne0nx3r0.quantum.api.Receiver;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.*;

//TODO: Allow naming of circuits

@SerializableAs("Circuit")
public class Circuit implements ConfigurationSerializable {
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

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("owner", playerUUID.toString());
        map.put("type", circuitTypes.name);
        map.put("receiver", receivers);
        return map;
    }
}
