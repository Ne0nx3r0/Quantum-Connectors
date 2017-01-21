package com.ne0nx3r0.quantum.listeners;

import com.ne0nx3r0.quantum.QuantumConnectors;
import com.ne0nx3r0.quantum.circuits.Circuit;
import com.ne0nx3r0.quantum.circuits.CircuitManager;
import com.ne0nx3r0.quantum.receiver.AbstractReceiver;
import com.ne0nx3r0.quantum.receiver.ReceiverRegistry;
import com.ne0nx3r0.quantum.utils.MessageLogger;
import com.ne0nx3r0.quantum.utils.SourceBlockUtil;
import com.ne0nx3r0.quantum.utils.ValidMaterials;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.InventoryHolder;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class QuantumConnectorsPlayerListener implements Listener {
    private final QuantumConnectors plugin;

    private CircuitManager circuitManager;
    private MessageLogger messageLogger;

    public QuantumConnectorsPlayerListener(QuantumConnectors instance, CircuitManager circuitManager, MessageLogger messageLogger) {
        this.plugin = instance;
        this.circuitManager = circuitManager;
        this.messageLogger = messageLogger;

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("QuantumConnectors.update") || player.isOp()) {
            if (plugin.isUpdateAvailable()) {
                player.sendMessage(ChatColor.RED + "[QC] An update is available: " + ChatColor.WHITE + plugin.getUpdateName());
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (!event.hasBlock()) {
            return;
        }

        Location location = SourceBlockUtil.getSourceBlock(event.getClickedBlock().getLocation());

        Block block = location.getBlock();

        //Holding redstone, clicked a block, and has a pending circuit from /qc
        if (event.getItem() != null
                && event.getItem().getType() == Material.REDSTONE
                && circuitManager.hasPendingCircuit(event.getPlayer())) {
            Player player = event.getPlayer();
            Circuit pc = circuitManager.getPendingCircuit(player);

            //No sender yet
            if (pc.getLocation() == null) {
                //Is this a valid block to act as a sender?
                if (circuitManager.isValidSender(block)) {
                    //There is already a circuit there
                    if (circuitManager.circuitExists(location)) {
                        messageLogger.msg(player, ChatColor.YELLOW + "A circuit already sends from this location!");
                        messageLogger.msg(player, "Break the block to remove it.");

                    }
                    //Set the sender location
                    else {
                        pc.setLocation(location);
                        messageLogger.msg(player, "Sender saved!");
                    }
                }
                //Invalid sender
                else {
                    messageLogger.msg(player, ChatColor.RED + "Invalid sender!");
                    messageLogger.msg(player, ChatColor.YELLOW + "Senders: " + ChatColor.WHITE + circuitManager.getValidSendersString());

                }
            }
            //Adding a receiver
            else {
                //Player clicked the sender block again
                if (pc.getLocation().toString().equals(location.toString())) {
                    messageLogger.msg(player, ChatColor.YELLOW + "A block cannot be the sender AND the receiver!");

                }
                //Player clicked a valid receiver block
                else if (circuitManager.isValidReceiver(block)) {


                    if (pc.isReceiver(location)) {
                        // Player is sneaking, receiver will be removed.
                        if (player.isSneaking()) {
                            messageLogger.msg(player, messageLogger.getMessage("receiver_deleted"));
                            pc.delReceiver(location);
                        } else
                            messageLogger.msg(player, messageLogger.getMessage("receiver_already_added"));
                        return;
                    }


                    //Only allow circuits in the same world, sorry multiworld QCircuits :(
                    if (pc.getLocation().getWorld().equals(location.getWorld())) {
                        //Isn't going over max receivers
                        if (QuantumConnectors.MAX_RECEIVERS_PER_CIRCUIT == 0 // 0 == unlimited
                                || pc.getReceiversCount() < QuantumConnectors.MAX_RECEIVERS_PER_CIRCUIT
                                || player.hasPermission("QuantumConnectors.ignoreLimits")) {

                            if (ReceiverRegistry.isValidReceiver(block)) {

                                List<Class<? extends AbstractReceiver>> possibleReceivers = ReceiverRegistry.fromType(location);


                                // TODO: 20.01.2017 create inventory with possible Receivers
                                // TODO: 20.01.2017 move to inventory listener ->
                                //Add the receiver to our new/found circuit
                                // temp solution
                                if (possibleReceivers.size() > 0) {
                                    try {
                                        pc.addReceiver(possibleReceivers.get(0), location, pc.getDelay());
                                    } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                                        e.printStackTrace();
                                    } finally {
                                        messageLogger.msg(player, "Added a receiver! (#" + pc.getReceiversCount() + ")" + ChatColor.YELLOW + " ('/qc done', or add more)");
                                    }
                                }

                                // TODO: 20.01.2017 until here

                                // TODO: 20.01.2017 idea of pregenerating inventory for each MaterialType registered


                            }
                        }
                        //Went over max circuits
                        else {
                            messageLogger.msg(player, "You cannot add anymore receivers! (" + pc.getReceiversCount() + ")");
                            messageLogger.msg(player, "'/qc done' to finish circuit, or '/qc cancel' to void it");

                        }
                    }
                    //Receiver was in a different world
                    else {
                        messageLogger.msg(player, ChatColor.RED + "Receivers must be in the same world as their sender! Sorry :|");
                    }
                }
                //Player clicked an invalid receiver block
                else {
                    messageLogger.msg(player, ChatColor.RED + "Invalid receiver!");
                    messageLogger.msg(player, ChatColor.YELLOW + "Receivers: " + ChatColor.WHITE + circuitManager.getValidReceiversString());
                    messageLogger.msg(player, "('/qc done' to finish circuit, or '/qc cancel' to void it)");

                }
            }
        }
        //Clicked on a block that has a quantum circuit (sender) attached
        else if (circuitManager.circuitExists(location)) {

            if (ValidMaterials.OPENABLE.contains(block.getType())) {
                int current = circuitManager.getBlockCurrent(block);

                circuitManager.activateCircuit(location, current, current > 0 ? 0 : 15);
            } else if (block.getType() == Material.BOOKSHELF) {
                // send on
                circuitManager.activateCircuit(location, 0, 15);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent e) {
        InventoryHolder ih;
        try {
            ih = e.getInventory().getHolder();
        } catch (NullPointerException ex) {
            return;
        }

        if (ih instanceof Chest) {
            Location lChest = ((Chest) e.getInventory().getHolder()).getLocation();

            if (circuitManager.circuitExists(lChest)) {
                // send on
                circuitManager.activateCircuit(lChest, 5, 0);
            }
        } else if (ih instanceof DoubleChest) {
            activeDoubleChest((DoubleChest) ih);
        }
    }

    private void activeDoubleChest(DoubleChest dc) {

        InventoryHolder leftSide = dc.getLeftSide();

        if (leftSide != null) {
            Location lLeft = leftSide.getInventory().getLocation();

            if (lLeft != null && circuitManager.circuitExists(lLeft)) {
                // send off
                circuitManager.activateCircuit(lLeft, 0, 5);
            }
        }

        if (leftSide != null) {
            InventoryHolder rightSide = dc.getRightSide();

            Location lRight = ((Chest) dc.getRightSide()).getLocation();

            if (lRight != null && circuitManager.circuitExists(lRight)) {
                // send off
                circuitManager.activateCircuit(lRight, 0, 5);
            }
        }
    }


    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent e) {
        InventoryHolder ih;
        try {
            ih = e.getInventory().getHolder();
        } catch (NullPointerException ex) {
            return;
        }

        if (ih instanceof Chest) {
            Location lChest = ((Chest) ih).getLocation();

            if (circuitManager.circuitExists(lChest)) {
                // send off
                circuitManager.activateCircuit(lChest, 0, 5);
            }
        } else if (ih instanceof DoubleChest) {
            activeDoubleChest((DoubleChest) ih);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEnterBed(PlayerBedEnterEvent e) {
        Location location = SourceBlockUtil.getSourceBlock(e.getBed().getLocation());
        if (circuitManager.circuitExists(location)) {
            // send on
            circuitManager.activateCircuit(location, 5, 0);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onLeaveBed(PlayerBedLeaveEvent e) {

        Location location = SourceBlockUtil.getSourceBlock(e.getBed().getLocation());
        if (circuitManager.circuitExists(location)) {
            // send on
            circuitManager.activateCircuit(location, 0, 5);
        }
    }
}