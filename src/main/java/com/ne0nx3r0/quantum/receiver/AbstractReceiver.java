package com.ne0nx3r0.quantum.receiver;

import com.ne0nx3r0.quantum.api.Receiver;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ysl3000 on 19.01.17.
 */
public abstract class AbstractReceiver implements Receiver {

    protected Location location;
    protected long delay;


    public AbstractReceiver(Location location) {
        this(location, 0);
    }

    public AbstractReceiver(Location location, long delay) {
        this.location = location;
        this.delay = delay;
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
