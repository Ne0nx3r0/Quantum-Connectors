package com.ne0nx3r0.quantum.circuits;

import com.ne0nx3r0.quantum.QuantumConnectors;
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
    private CircuitType circuitTypes;
    private Location location;
    private long delay;

    public Circuit(UUID playerUUID, CircuitType circuitTypes, long delay) {
        this(playerUUID, new ArrayList<>(), circuitTypes, delay);
    }

    public Circuit(UUID playerUUID, List<Receiver> receivers, CircuitType circuitTypes, long delay) {
        this.playerUUID = playerUUID;
        this.circuitTypes = circuitTypes;
        this.delay = delay;
        this.receivers.addAll(receivers);
    }

    public Circuit(Map<?, ?> map) {
        this.playerUUID = UUID.fromString((String) map.get("owner"));
        this.circuitTypes = CircuitType.getByName((String) map.get("type"));
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
        receivers.add(ReceiverRegistry.fromType(loc, delay, CircuitManager.keepAlives, QuantumConnectors.getQsWorld()));
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

    public CircuitType getCircuitType() {
        return circuitTypes;
    }

    public void setCircuitType(CircuitType circuitType) {
        this.circuitTypes = circuitType;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("owner", playerUUID.toString());
        map.put("type", circuitTypes.name);

        List<Map<String, ?>> receiverMap = new ArrayList<>();

        for (Receiver receiver : receivers) {
            receiverMap.add(receiver.serialize());
        }

        map.put("receiver", receiverMap);
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
