package com.ne0nx3r0.quantum.listeners;

import com.ne0nx3r0.quantum.QuantumConnectors;
import com.ne0nx3r0.quantum.circuits.CircuitManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class QuantumConnectorsBlockListener implements Listener {
    private final QuantumConnectors plugin;
    public static String string;

    public QuantumConnectorsBlockListener(final QuantumConnectors plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockRedstoneChange(BlockRedstoneEvent event){
        if (CircuitManager.circuitExists(event.getBlock().getLocation())) {
            CircuitManager.activateCircuit(event.getBlock().getLocation(), event.getNewCurrent());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Location l = event.getBlock().getLocation();
        if (CircuitManager.circuitExists(l)) { // Breaking Sender
            CircuitManager.removeCircuit(l);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onFuranceBurn(FurnaceBurnEvent e){
        if(CircuitManager.circuitExists(e.getBlock().getLocation())){            
            if(e.isBurning()){
                CircuitManager.activateCircuit(e.getBlock().getLocation(), 5);
            }/* Still in the process of finding an "off" event for furnaces. else{
                CircuitManager.activateCircuit(e.getBlock().getLocation(), 0);
            }*/
        }
    }
}