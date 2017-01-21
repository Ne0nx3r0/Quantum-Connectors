package com.ne0nx3r0.quantum.listeners;

import com.ne0nx3r0.quantum.QuantumConnectors;
import com.ne0nx3r0.quantum.circuits.CircuitManager;
import com.ne0nx3r0.quantum.utils.MessageLogger;
import com.ne0nx3r0.quantum.utils.SourceBlockUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;

public class QuantumConnectorsBlockListener implements Listener {
    public static String string;
    private QuantumConnectors plugin;
    private CircuitManager circuitManager;
    private MessageLogger messageLogger;

    public QuantumConnectorsBlockListener(final QuantumConnectors plugin, CircuitManager circuitManager, MessageLogger messageLogger) {
        this.plugin = plugin;
        this.circuitManager = circuitManager;
        this.messageLogger = messageLogger;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockRedstoneChange(BlockRedstoneEvent e) {
        if (circuitManager.circuitExists(e.getBlock().getLocation())) {
            circuitManager.activateCircuit(e.getBlock().getLocation(), e.getOldCurrent(), e.getNewCurrent());
        }

        if (circuitManager.shouldLeaveReceiverOn(e.getBlock())) {
            e.setNewCurrent(15);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Location sourceLocation = SourceBlockUtil.getSourceBlock(event.getBlock().getLocation());
        if (circuitManager.circuitExists(sourceLocation)) { // Breaking Sender
            circuitManager.removeCircuit(sourceLocation);
            messageLogger.msg(event.getPlayer(), messageLogger.getMessage("circuit_deleted"));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onFuranceBurn(FurnaceBurnEvent e) {
        if (circuitManager.circuitExists(e.getBlock().getLocation())) {
            if (e.isBurning()) {
                Location lFurnace = e.getBlock().getLocation();

                //SEND ON
                circuitManager.activateCircuit(lFurnace, 0, 1);

                //Schedule a check to send the corresponding OFF
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                        plugin,
                        new DelayedFurnaceCoolCheck(lFurnace),
                        e.getBurnTime() + 5
                );
            }
        }
    }

    private class DelayedFurnaceCoolCheck implements Runnable {
        private final Location lFurnace;

        DelayedFurnaceCoolCheck(Location lFurnace) {
            this.lFurnace = lFurnace;
        }

        @Override
        public void run() {
            Block bFurnace = lFurnace.getBlock();

            // If it's a BURNING_FURNACE it's still on and the next 
            // FurnaceBurnEvent is responsible for dispatching a delayed task
            if (bFurnace.getType() == Material.FURNACE) {
                //Send OFF
                if (circuitManager.circuitExists(lFurnace)) {
                    circuitManager.activateCircuit(lFurnace, 1, 0);
                }
            }
        }
    }
}