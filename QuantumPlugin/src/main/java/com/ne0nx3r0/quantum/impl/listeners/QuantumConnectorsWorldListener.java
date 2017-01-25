package com.ne0nx3r0.quantum.impl.listeners;

import com.ne0nx3r0.quantum.impl.circuits.CircuitLoader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class QuantumConnectorsWorldListener implements Listener {
    private CircuitLoader circuitLoader;

    public QuantumConnectorsWorldListener(CircuitLoader circuitLoader) {
        this.circuitLoader = circuitLoader;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(WorldLoadEvent event) {
        circuitLoader.loadWorld(event.getWorld());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldUnload(WorldUnloadEvent event) {
        circuitLoader.saveWorld(event.getWorld());
    }
}
