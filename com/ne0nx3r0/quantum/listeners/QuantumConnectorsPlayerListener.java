package com.ne0nx3r0.quantum.listeners;

import com.ne0nx3r0.quantum.QuantumConnectors;
import com.ne0nx3r0.quantum.circuits.Circuit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class QuantumConnectorsPlayerListener implements Listener{
    private final QuantumConnectors plugin;
    
    public QuantumConnectorsPlayerListener(QuantumConnectors instance){
        this.plugin = instance;
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event){   

    //Clicked on a block that has a quantum circuit (sender) attached
        if (event.getClickedBlock() != null && QuantumConnectors.circuits.circuitExists(event.getClickedBlock().getLocation())) {
            Block block = event.getClickedBlock();

            if (block.getType() == Material.WOODEN_DOOR || block.getType() == Material.TRAP_DOOR) {
                QuantumConnectors.circuits.activateCircuit(event.getClickedBlock().getLocation(), QuantumConnectors.circuits.getBlockCurrent(block));
            }
        }
        
    //Holding redstone, clicked a block, and has a pending circuit from /qc
        else if (event.getItem() != null
        && event.getItem().getType() == Material.REDSTONE
        && event.getClickedBlock() != null
        && QuantumConnectors.tempCircuitTypes.containsKey(event.getPlayer())){

            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
                
        //Setting up a sender
            if(!QuantumConnectors.tempCircuits.containsKey(player)){
            //Is this a valid block to act as a sender?
                if(QuantumConnectors.circuits.isValidSender(block)){
                //There is already a circuit there  
                    if(QuantumConnectors.circuits.circuitExists(block.getLocation())) {
                        plugin.msg(player, ChatColor.YELLOW + "A circuit already sends from this location!");
                        plugin.msg(player, "You can break the block to remove it.");
                    }
                //Create a temporary circuit
                    else{
                        QuantumConnectors.tempCircuits.put(player,new Circuit());
                        QuantumConnectors.tempCircuitLocations.put(player,event.getClickedBlock().getLocation());
                        plugin.msg(player, "Sender saved!");
                    }
                }
            //Invalid sender
                else{
                    plugin.msg(player, ChatColor.RED + "Invalid sender!");
                    plugin.msg(player, ChatColor.YELLOW + "Senders: " + ChatColor.WHITE + QuantumConnectors.circuits.getValidSendersString());
                }
            }
        //Adding a receiver
            else{
            //Player clicked the sender block again
                if(QuantumConnectors.tempCircuitLocations.get(player).toString().equals(event.getClickedBlock().getLocation().toString())) {
                    plugin.msg(player, ChatColor.YELLOW + "A block cannot be the sender AND the receiver!");
                }
            //Player clicked a valid receiver block
                else if(QuantumConnectors.circuits.isValidReceiver(block)){
                    
                //Add the receiver to our new/found circuit
                    QuantumConnectors.tempCircuits.get(player).addReceiver(
                        event.getClickedBlock().getLocation(),//lLocation
                        QuantumConnectors.tempCircuitTypes.get(player)//iType
                    );
                    
                    plugin.msg(player, "Added a receiver!" +ChatColor.YELLOW + " ('/qc done', or add more receivers)");
                }
            //Player clicked an invalid receiver block
                else{
                    plugin.msg(player, ChatColor.RED + "Invalid receiver!");
                    plugin.msg(player, ChatColor.YELLOW + "Receivers: " + ChatColor.WHITE + QuantumConnectors.circuits.getValidReceiversString());            
                    plugin.msg(player, "('/qc done' if you are finished)");
                }
            }
        }        
    }
}
