package com.ne0nx3r0.quantum.listeners;

import com.ne0nx3r0.quantum.QuantumConnectors;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

public class QuantumConnectorsBlockListener implements Listener {
    private final QuantumConnectors plugin;
    public static String string;

    public QuantumConnectorsBlockListener(final QuantumConnectors plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockRedstoneChange(BlockRedstoneEvent event) {
        if (QuantumConnectors.circuitManager.circuitExists(event.getBlock().getLocation())) {
            QuantumConnectors.circuitManager.activateCircuit(event.getBlock().getLocation(), event.getNewCurrent());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Location l = event.getBlock().getLocation();
        if (QuantumConnectors.circuitManager.circuitExists(l)) { // Breaking Sender
            QuantumConnectors.circuitManager.removeCircuit(l);
        }
        /*  TODO: Consider whether this is worthwhile to keep
         *  It looks short, but this checks EVERY receiver of EVERY circuit EVERY time a block is broken
         *  Probably is more effecient to only check receivers when the circuit is activated
         *  WorldEdit/etc wont fire this event for changes anyway
         * 
         * else if(QuantumConnectors.circuits.receiverExists(l)){ // Breaking receiver
            QuantumConnectors.circuits.removeReceiver(l);
        }
        * */

    }
}