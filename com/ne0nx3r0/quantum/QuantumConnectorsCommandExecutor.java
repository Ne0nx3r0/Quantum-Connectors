package com.ne0nx3r0.quantum;

import com.ne0nx3r0.quantum.circuits.CircuitManager;
import com.ne0nx3r0.quantum.circuits.PendingCircuit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuantumConnectorsCommandExecutor implements CommandExecutor {
    private QuantumConnectors plugin;
    
    public QuantumConnectorsCommandExecutor(QuantumConnectors plugin){
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
        if (!(cs instanceof Player)){
            plugin.log(plugin.getMessage("console_not_allowed:"));
        }
        
        if(args.length > 0){
            if(args[0].equalsIgnoreCase("q")) args[0] = "quantum";
            else if(args[0].equalsIgnoreCase("t")) args[0] = "toggle";
            else if(args[0].equalsIgnoreCase("r")) args[0] = "reverse";
            else if(args[0].equalsIgnoreCase("c")) args[0] = "c";
        }
        
        Player player = (Player) cs;
        
// Command was: "/qc"
        if(args.length == 0 || args[0].equalsIgnoreCase("?")){    
            plugin.msg(player, "qc_usage");

            String s = "";	  	
            for(String sKey : CircuitManager.getValidCircuitTypes().keySet()) {
                s += sKey + ", ";
            }

            plugin.msg(player, ChatColor.YELLOW + plugin.getMessage("available_circuits")+ChatColor.WHITE + s.substring(0, s.length() - 2));
        }
      
// Command was: "/qc cancel"
        else if(args[0].equalsIgnoreCase("cancel")){
        
        //Pending circuit exists
            if(CircuitManager.hasPendingCircuit(player)){
                
                CircuitManager.removePendingCircuit(player);
                
                plugin.msg(player, plugin.getMessage("cancelled"));
            }
        //No pending circuit
            else{
                plugin.msg(player, plugin.getMessage("no_pending_circuit"));
            }
        }

// Command was: "/qc done"
        else if(args[0].equalsIgnoreCase("done")){
        
        //They typed "/qc <circuit>"
            if(CircuitManager.hasPendingCircuit(player)){
                PendingCircuit pc = CircuitManager.getPendingCircuit(player);
            //They also setup a sender
                if(pc.hasSenderLocation()){
                //Finally, they also setup at least one receiver
                    if(pc.hasReceiver()){
                        CircuitManager.addCircuit(pc); 
                        
                    // I hate doors, I hate all the wooden doors.
                    // I just want to break them all, but I can't
                    // Can't break all wood doors.
                        if(pc.getSenderLocation().getBlock().getType() == Material.WOODEN_DOOR){
                            Block bDoor = pc.getSenderLocation().getBlock();
                            int iData = (int) bDoor.getData();
                            Block bOtherPiece = bDoor.getRelative((iData & 0x08) == 0x08 ? BlockFace.DOWN : BlockFace.UP);

                            //TODO: Clone instead of reference the circuit?
                            //TODO: On break check if the circuit has a twin
                            CircuitManager.addCircuit(bOtherPiece.getLocation(),pc.getCircuit());
                        }
                        
                        CircuitManager.removePendingCircuit(player);

                        plugin.msg(player, plugin.getMessage("circuit_created"));
                    }
                    //They have not setup at least one receiver
                    else{
                        plugin.msg(player,plugin.getMessage("no_receivers"));
                    }
                }
                //They didn't setup a sender
                else{
                   plugin.msg(player, plugin.getMessage("no_sender")); 
                }
            }else{
                plugin.msg(player,plugin.getMessage("no_pending_action"));
            }
        }
        
// Command was: "/qc <valid circuit type>"
        else if(CircuitManager.isValidCircuitType(args[0])){
            
        //Player has permission to create the circuit
            if(player.hasPermission("QuantumConnectors.create."+args[0])){

            //Figure out if there's a delay, or use 0 for no delay
                double dDelay = 0;

                if(args.length > 1){
                    try { 
                        dDelay = Double.parseDouble(args[1]);
                    }
                    catch (NumberFormatException e){
                        dDelay = -1;
                    }      

                    if(dDelay < 0 
                    || (dDelay > QuantumConnectors.MAX_DELAY_TIME && !player.hasPermission("QuantumConnectors.ignoreLimits"))){
                        dDelay = 0;
                        
                        plugin.msg(player,ChatColor.RED + plugin.getMessage("invalid_delay").replaceAll("%maxdelay%", new Integer(QuantumConnectors.MAX_DELAY_TIME).toString()));  
                    }
                }
                
                String sDelayMsg = " ("+args[0]+" "+dDelay+"s delay)";
                
                sDelayMsg = " ";
                
                int iDelayTicks = (int) Math.round(dDelay*20);
                
                if(!CircuitManager.hasPendingCircuit(player)){
                    CircuitManager.addPendingCircuit(
                            player,
                            CircuitManager.getCircuitType(args[0]),
                            iDelayTicks);
                    
                    plugin.msg(player,plugin.getMessage("circuit_ready")
                            .replace("%circuit%",args[0].toUpperCase())
                            .replace("%delay%",new Double(dDelay).toString()));
                }
                else{       
                    CircuitManager.getPendingCircuit(player).setCircuitType(
                            CircuitManager.getCircuitType(args[0]),
                            iDelayTicks);
                    
                    plugin.msg(player, plugin.getMessage("circuit_changed")
                            .replace("%circuit%",args[0].toUpperCase())
                            .replace("%delay%",new Double(dDelay).toString()));
                }
            }
            
        //Player doesn't have permission
            else{
                plugin.msg(player, ChatColor.RED + plugin.getMessage("no_permission").replace("%circuit",args[0].toUpperCase()));
            }
        }
        
// Command was invalid
        else{
            plugin.msg(player,plugin.getMessage("invalid_circuit"));
        }
       
        return true;
        
    }//End onCommand
}
