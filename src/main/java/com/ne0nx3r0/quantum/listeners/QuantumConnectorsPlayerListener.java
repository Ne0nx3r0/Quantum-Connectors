package com.ne0nx3r0.quantum.listeners;

import com.ne0nx3r0.quantum.QuantumConnectors;
import com.ne0nx3r0.quantum.circuits.CircuitManager;
import com.ne0nx3r0.quantum.circuits.PendingCircuit;
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
import org.bukkit.material.Bed;

public class QuantumConnectorsPlayerListener implements Listener{
    private final QuantumConnectors plugin;
    
    public QuantumConnectorsPlayerListener(QuantumConnectors instance)
    {
        this.plugin = instance;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        
        if(player.hasPermission("QuantumConnectors.update") || player.isOp())
        {
            if(plugin.isUpdateAvailable())
            {
                player.sendMessage(ChatColor.RED+"[QC] An update is available: " + ChatColor.WHITE + plugin.getUpdateName());
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event)
    {  

    //Holding redstone, clicked a block, and has a pending circuit from /qc
        if(event.getItem() != null
        && event.getItem().getType() == Material.REDSTONE
        && event.getClickedBlock() != null
        && CircuitManager.hasPendingCircuit(event.getPlayer())){
            Player player = event.getPlayer();
            PendingCircuit pc = CircuitManager.getPendingCircuit(player);
            Block block = event.getClickedBlock();
            Location clickedLoc = block.getLocation();
                
        //No sender yet
            if(!pc.hasSenderLocation()){
            //Is this a valid block to act as a sender?
                if(CircuitManager.isValidSender(block)){
                //There is already a circuit there  
                    if(CircuitManager.circuitExists(clickedLoc)){
                        plugin.msg(player, ChatColor.YELLOW + "A circuit already sends from this location!");
                        plugin.msg(player, "Break the block to remove it.");
                    }
                //Set the sender location
                    else{
                        pc.setSenderLocation(clickedLoc);
                        
                        plugin.msg(player, "Sender saved!");
                    }
                }
            //Invalid sender
                else{
                    plugin.msg(player, ChatColor.RED + "Invalid sender!");
                    plugin.msg(player, ChatColor.YELLOW + "Senders: " + ChatColor.WHITE + CircuitManager.getValidSendersString());
                }
            }
        //Adding a receiver
            else{
            //Player clicked the sender block again
                if(pc.getSenderLocation().toString().equals(clickedLoc.toString())) {
                    plugin.msg(player, ChatColor.YELLOW + "A block cannot be the sender AND the receiver!");
                }
            //Player clicked a valid receiver block
                else if(CircuitManager.isValidReceiver(block)){
                    
                //Only allow circuits in the same world, sorry multiworld QCircuits :(
                    if(pc.getSenderLocation().getWorld().equals(clickedLoc.getWorld())){
                    //Isn't going over max receivers    
                        if(QuantumConnectors.MAX_RECEIVERS_PER_CIRCUIT == 0 // 0 == unlimited
                        || pc.getCircuit().getReceiversCount() < QuantumConnectors.MAX_RECEIVERS_PER_CIRCUIT
                        || player.hasPermission("QuantumConnectors.ignoreLimits")){
                        //Add the receiver to our new/found circuit
                            pc.addReceiver(clickedLoc);

                            plugin.msg(player, "Added a receiver! (#"+pc.getCircuit().getReceiversCount() +")" +ChatColor.YELLOW + " ('/qc done', or add more)");
                        }
                    //Went over max circuits
                        else{
                            plugin.msg(player, "You cannot add anymore receivers! ("+pc.getCircuit().getReceiversCount()+")");
                            plugin.msg(player, "'/qc done' to finish circuit, or '/qc cancel' to void it");
                        }
                    }
                //Receiver was in a different world
                    else{
                        plugin.msg(player,ChatColor.RED + "Receivers must be in the same world as their sender! Sorry :|");
                    }
                }
            //Player clicked an invalid receiver block
                else{
                    plugin.msg(player, ChatColor.RED + "Invalid receiver!");
                    plugin.msg(player, ChatColor.YELLOW + "Receivers: " + ChatColor.WHITE + CircuitManager.getValidReceiversString());            
                    plugin.msg(player, "('/qc done' if you are finished)");
                }
            }
        }  
    //Clicked on a block that has a quantum circuit (sender) attached
        else if(event.getClickedBlock() != null && CircuitManager.circuitExists(event.getClickedBlock().getLocation()))
        {
            Block block = event.getClickedBlock();

            if( block.getType() == Material.WOODEN_DOOR
             || block.getType() == Material.SPRUCE_DOOR
             || block.getType() == Material.BIRCH_DOOR
             || block.getType() == Material.JUNGLE_DOOR
             || block.getType() == Material.ACACIA_DOOR
             || block.getType() == Material.DARK_OAK_DOOR
             || block.getType() == Material.TRAP_DOOR
             || block.getType() == Material.FENCE_GATE
             || block.getType() == Material.SPRUCE_FENCE_GATE
             || block.getType() == Material.BIRCH_FENCE_GATE
             || block.getType() == Material.JUNGLE_FENCE_GATE
             || block.getType() == Material.ACACIA_FENCE_GATE
             || block.getType() == Material.DARK_OAK_FENCE_GATE){
                int current = CircuitManager.getBlockCurrent(block);
                
                CircuitManager.activateCircuit(event.getClickedBlock().getLocation(), current, current > 0 ? 0 : 15 );
            }
            else if(block.getType() == Material.BOOKSHELF)
            {
                // send on
                CircuitManager.activateCircuit(event.getClickedBlock().getLocation(), 5, 0);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent e)
    {
    	InventoryHolder ih;
    	try{
            ih = e.getInventory().getHolder();
    	}catch(NullPointerException ex){
    		return;
    	}
        
        if(ih instanceof Chest){
            Location lChest = ((Chest) e.getInventory().getHolder()).getLocation();
            
            if(CircuitManager.circuitExists(lChest)){
                // send on
                CircuitManager.activateCircuit(lChest, 5, 0);
            }
        }else if(ih instanceof DoubleChest){
            DoubleChest dc = (DoubleChest) ih;
            
            Location lLeft = null;
            try{ 
                lLeft = ((Chest) dc.getLeftSide()).getLocation(); 
            }catch(NullPointerException npe){}
            
            if(lLeft != null && CircuitManager.circuitExists(lLeft)){
                // send off
                CircuitManager.activateCircuit(lLeft, 0, 5);
            }      
            
            Location lRight = null;
            try{ 
                lRight = ((Chest) dc.getRightSide()).getLocation(); 
            }catch(NullPointerException npe){}
            
            if(lRight != null && CircuitManager.circuitExists(lRight)){
                // send off
                CircuitManager.activateCircuit(lRight, 0, 5);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent e)
    {
    	InventoryHolder ih;
    	try{
            ih = e.getInventory().getHolder();
    	}catch(NullPointerException ex){
    		return;
    	}
        
        if(ih instanceof Chest){
            Location lChest = ((Chest) ih).getLocation();
            
            if(CircuitManager.circuitExists(lChest)){
                // send off
                CircuitManager.activateCircuit(lChest, 0, 5);
            }
        }else if(ih instanceof DoubleChest){
            DoubleChest dc = (DoubleChest) ih;
            
            Location lLeft = null;
            try{ 
                lLeft = ((Chest) dc.getLeftSide()).getLocation(); 
            }catch(NullPointerException npe){}
            
            if(lLeft != null && CircuitManager.circuitExists(lLeft)){
                // send off
                CircuitManager.activateCircuit(lLeft, 0, 5);
            }      
            
            Location lRight = null;
            try{ 
                lRight = ((Chest) dc.getRightSide()).getLocation(); 
            }catch(NullPointerException npe){}
            
            if(lRight != null && CircuitManager.circuitExists(lRight)){
                // send off
                CircuitManager.activateCircuit(lRight, 0, 5);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEnterBed(PlayerBedEnterEvent e)
    {
        if(CircuitManager.circuitExists(e.getBed().getLocation())){
            // send on
            CircuitManager.activateCircuit(e.getBed().getLocation(), 5, 0);
        }
        if(CircuitManager.circuitExists(this.getTwinLocation(e.getBed()))){
            // send on
            CircuitManager.activateCircuit(this.getTwinLocation(e.getBed()), 5, 0);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onLeaveBed(PlayerBedLeaveEvent e)
    {        
        if(CircuitManager.circuitExists(e.getBed().getLocation())){
            // send off
            CircuitManager.activateCircuit(e.getBed().getLocation(), 0, 5);
        }
        if(CircuitManager.circuitExists(this.getTwinLocation(e.getBed()))){
            // send off
            CircuitManager.activateCircuit(this.getTwinLocation(e.getBed()), 0, 5);
        }
    }
    
    private Location getTwinLocation(Block b)
    {
        Bed bed = (Bed) b.getState().getData();
        if(bed.isHeadOfBed()){
            return b.getRelative(bed.getFacing().getOppositeFace()).getLocation();
        }else{
            return b.getRelative(bed.getFacing()).getLocation();
        }
    }
}