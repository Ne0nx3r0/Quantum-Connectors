package com.ne0nx3r0.quantum.circuits;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

//TODO: Allow naming of circuits

public class Circuit implements ConfigurationSerializable {
    private List<Receiver> receivers;
    private UUID playerUUID;

    public Circuit(UUID playerUUID) {
        this(playerUUID, Collections.EMPTY_LIST);
    }

    public Circuit(UUID playerUUID, List<Receiver> receivers) {
        this.playerUUID = playerUUID;
        this.receivers = receivers;
    }

    public void addReceiver(Location loc, int type, int delay) {
        receivers.add(new Receiver(loc, type, delay));
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
