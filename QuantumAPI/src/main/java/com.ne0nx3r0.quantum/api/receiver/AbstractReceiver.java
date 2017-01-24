package com.ne0nx3r0.quantum.api.receiver;

import com.ne0nx3r0.quantum.api.IQuantumConnectorsAPI;
import com.ne0nx3r0.quantum.api.IValidMaterials;
import com.ne0nx3r0.quantum.api.QuantumConnectorsAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractReceiver implements Receiver, IValidMaterials {

    protected final static IQuantumConnectorsAPI api = QuantumConnectorsAPI.getAPI();

    protected Location location;
    protected long delay;

    /**
     * only use to getValidMaterials
     */
    public AbstractReceiver() {
    }

    public AbstractReceiver(Location location) {
        this(location, 0);
    }

    public AbstractReceiver(Location location, Integer delay) {
        this.location = location;
        this.delay = delay;
        this.calculateRealLocation();
    }

    public AbstractReceiver(Map<String, Object> map) {
        this.location = new Location(
                Bukkit.getWorld((String) map.get("location_world")),
                (Integer) map.get("location_x"),
                (Integer) map.get("location_y"),
                (Integer) map.get("location_z"));
        this.delay = (Integer) map.get("delay");
    }


    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void calculateRealLocation() {
    }

    @Override
    public final String getType() {
        return QuantumConnectorsAPI.getReceiverRegistry().getUniqueKey(this.getClass());
    }

    @Override
    public long getDelay() {
        return delay;
    }

    @Override
    public void setActive(boolean powerOn) throws ReceiverNotValidException, ValueNotChangedException {
        if (!isValid()) throw new ReceiverNotValidException();
        if (isActive() == powerOn) throw new ValueNotChangedException();
    }

    @Override
    public boolean isValid() {
        return getValidMaterials().contains(location.getBlock().getType());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("location_world", location.getWorld().getName());
        map.put("location_x", location.getBlockX());
        map.put("location_y", location.getBlockY());
        map.put("location_z", location.getBlockZ());
        map.put("delay", delay);
        map.put("type", getType());
        return map;
    }
}
