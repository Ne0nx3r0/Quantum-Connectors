package com.ne0nx3r0.quantum.impl.receiver.base;

import com.ne0nx3r0.quantum.api.receiver.ReceiverNotValidException;
import com.ne0nx3r0.quantum.api.receiver.ValueNotChangedException;
import com.ne0nx3r0.quantum.impl.interfaces.Receiver;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

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

        Bukkit.getScheduler().runTaskLater(this.javaPlugin, () -> {
            try {
                receiver.setActive(powerOn);
            } catch (ValueNotChangedException | ReceiverNotValidException ignored) {
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