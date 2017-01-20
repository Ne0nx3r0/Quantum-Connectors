package com.ne0nx3r0.quantum.circuits;

import com.ne0nx3r0.quantum.api.Receiver;
import com.ne0nx3r0.quantum.receiver.AbstractReceiver;
import com.ne0nx3r0.quantum.receiver.ReceiverRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

//TODO: Allow naming of circuits

@SerializableAs("Circuit")
public class Circuit implements ConfigurationSerializable {
    private List<Receiver> receivers = new ArrayList<>();
    private UUID playerUUID;
    private CircuitManager circuitManager;
    private CircuitTypes circuitTypes;
    private Location location;
    private long delay;

    public Circuit(UUID playerUUID, CircuitManager cmanager, CircuitTypes circuitTypes, long delay) {
        this(playerUUID, new ArrayList<>(), cmanager, circuitTypes, delay);
    }

    public Circuit(UUID playerUUID, List<Receiver> receivers, CircuitManager cmanager, CircuitTypes circuitTypes, long delay) {
        this.playerUUID = playerUUID;
        this.circuitTypes = circuitTypes;
        this.delay = delay;
        this.receivers.addAll(receivers);
        this.circuitManager = cmanager;
    }

    public Circuit(CircuitManager circuitManager, Map<?, ?> map) {
        this.circuitManager = circuitManager;
        this.playerUUID = UUID.fromString((String) map.get("owner"));
        this.circuitTypes = CircuitTypes.getByName((String) map.get("type"));
        this.location = new Location(Bukkit.getWorld((String) map.get("world")), (Integer) map.get("x"), (Integer) map.get("y"), (Integer) map.get("z"));
        this.delay = (Long) map.get("delay");
        List<?> reciverObjectList = (List<?>) map.get("receiver");

        for (Object receiverObject : reciverObjectList) {

            Map<?, ?> receiverMap = (Map<?, ?>) receiverObject;

            String type = (String) receiverMap.get("type");
            try {
                Constructor<? extends AbstractReceiver> receiverConstructor = ReceiverRegistry.getReceiverInstance(type);
                Receiver receiver = receiverConstructor.newInstance(receiverMap);

                if (receiver.isValid()) {
                    receivers.add(receiver);
                }

            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }

        }
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

    public boolean isValid() {
        // TODO: 20.01.2017 write validation
        return true;
    }

    public CircuitTypes getCircuitType() {
        return circuitTypes;
    }

    public void setCircuitType(CircuitTypes circuitType) {
        this.circuitTypes = circuitType;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("owner", playerUUID.toString());
        map.put("type", circuitTypes.name);
        map.put("receiver", receivers);
        map.put("world", location.getWorld().getName());
        map.put("x", location.getBlockX());
        map.put("y", location.getBlockY());
        map.put("z", location.getBlockZ());
        map.put("delay", delay);
        return map;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
}
