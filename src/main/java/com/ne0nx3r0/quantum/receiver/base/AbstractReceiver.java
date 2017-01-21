package com.ne0nx3r0.quantum.receiver.base;

import com.ne0nx3r0.quantum.api.Receiver;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractReceiver implements Receiver {

    protected Location location;
    protected long delay;

    /**
     * only use to getValidMaterials
     */
    protected AbstractReceiver() {
    }


    protected AbstractReceiver(Location location) {
        this(location, 0);
    }

    protected AbstractReceiver(Location location, Integer delay) {
        this.location = location;
        this.delay = delay;
        this.calculateRealLocation();
    }

    protected AbstractReceiver(Map<String, Object> map) {
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

    public abstract List<Material> getValidMaterials();

    @Override
    public long getDelay() {
        return delay;
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
