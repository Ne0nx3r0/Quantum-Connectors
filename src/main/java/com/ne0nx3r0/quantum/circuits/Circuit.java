package com.ne0nx3r0.quantum.circuits;

import com.ne0nx3r0.quantum.receiver.Receiver;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

//TODO: Allow naming of circuits

public class Circuit implements ConfigurationSerializable {
    private List<Receiver> receivers;
    private UUID playerUUID;

    private CircuitManager circuitManager;

    public Circuit(UUID playerUUID, CircuitManager cmanager) {
        this(playerUUID, Collections.EMPTY_LIST, cmanager);
    }

    public Circuit(UUID playerUUID, List<Receiver> receivers, CircuitManager cmanager) {
        this.playerUUID = playerUUID;
        if (receivers == null) {
            this.receivers = new ArrayList<Receiver>();
        } else
            this.receivers = new ArrayList<Receiver>(receivers);
        circuitManager = cmanager;
    }

    public Circuit(Map<String, Object> map, CircuitManager circuitManager) {

        this(UUID.fromString((String) map.get("o")), (List<Receiver>) map.get("r"), circuitManager);
        //return circuit;

    }

    public void addReceiver(Location loc, int type, int delay) {
        receivers.add(circuitManager.fromType(loc, type, delay));
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

    @Override
    public Map<String, Object> serialize() {

        Map<String, Object> map = new HashMap<>();

        map.put("uuid", playerUUID.toString());
        map.put("receivers", receivers);

        return map;

    }


}
