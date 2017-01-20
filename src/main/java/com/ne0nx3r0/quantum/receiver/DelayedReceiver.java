package com.ne0nx3r0.quantum.receiver;

import com.ne0nx3r0.quantum.api.Receiver;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

/**
 * Created by ysl3000 on 19.01.17.
 */
public class DelayedReceiver implements Receiver {

    private JavaPlugin javaPlugin;
    private Receiver receiver;

    public DelayedReceiver(JavaPlugin javaPlugin, Receiver receiver) {
        this.receiver = receiver;
        this.javaPlugin = javaPlugin;
    }

    @Override
    public Location getLocation() {
        return receiver.getLocation();
    }

    @Override
    public void calculateRealLocation() {
        receiver.calculateRealLocation();
    }

    @Override
    public String getType() {
        return receiver.getType();
    }

    @Override
    public long getDelay() {
        return receiver.getDelay();
    }

    @Override
    public boolean isActive() {
        return receiver.isActive();
    }

    @Override
    public void setActive(boolean powerOn) {

        Bukkit.getScheduler().runTaskLater(this.javaPlugin, new Runnable() {
            @Override
            public void run() {
                receiver.setActive(powerOn);
            }
        }, receiver.getDelay() * 20);
    }

    @Override
    public boolean isValid() {
        return receiver.isValid();
    }

    @Override
    public Map<String, Object> serialize() {
        return receiver.serialize();
    }
}