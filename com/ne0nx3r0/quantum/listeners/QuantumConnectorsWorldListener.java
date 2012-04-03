package com.ne0nx3r0.quantum.listeners;

import com.ne0nx3r0.quantum.QuantumConnectors;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class QuantumConnectorsWorldListener implements Listener {
    private QuantumConnectors plugin;

    public QuantumConnectorsWorldListener(QuantumConnectors plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(WorldLoadEvent event) {
        QuantumConnectors.circuitManager.loadWorld(event.getWorld());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldUnload(WorldUnloadEvent event) {
        QuantumConnectors.circuitManager.saveWorld(event.getWorld());
    }
}
