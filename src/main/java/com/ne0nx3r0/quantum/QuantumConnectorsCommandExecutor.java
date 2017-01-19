package com.ne0nx3r0.quantum;

import com.ne0nx3r0.quantum.circuits.CircuitManager;
import com.ne0nx3r0.quantum.circuits.CircuitTypes;
import com.ne0nx3r0.quantum.circuits.PendingCircuit;
import com.ne0nx3r0.quantum.utils.MessageLogger;
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
    private CircuitManager circuitManager;
    private MessageLogger messageLogger;

    public QuantumConnectorsCommandExecutor(QuantumConnectors plugin, CircuitManager circuitManager, MessageLogger messageLogger) {
        this.plugin = plugin;
        this.circuitManager = circuitManager;
        this.messageLogger = messageLogger;

    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
        if (!(cs instanceof Player)) {
            messageLogger.log(messageLogger.getMessage("console_not_allowed"));

            return true;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("q") || args[0].equalsIgnoreCase("normal"))
                args[0] = "quantum";
            else if (args[0].equalsIgnoreCase("t")) args[0] = "toggle";
            else if (args[0].equalsIgnoreCase("r")) args[0] = "reverse";
        }

        Player player = (Player) cs;

// Command was: "/qc"
        if (args.length == 0 || args[0].equalsIgnoreCase("?")) {
            messageLogger.msg(player, messageLogger.getMessage("usage"));

            String s = "";
            for (String sKey : circuitManager.getValidCircuitTypes().keySet()) {
                s += sKey + ", ";
            }

            messageLogger.msg(player, ChatColor.YELLOW + messageLogger.getMessage("available_circuits") + ChatColor.WHITE + s.substring(0, s.length() - 2));
        }

// Command was: "/qc cancel" or "/qc abort"
        else if (args[0].equalsIgnoreCase("cancel") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("abort")) {

            //Pending circuit exists
            if (circuitManager.hasPendingCircuit(player)) {

                circuitManager.removePendingCircuit(player);

                messageLogger.msg(player, messageLogger.getMessage("cancelled"));

            }
            //No pending circuit
            else {
                messageLogger.msg(player, messageLogger.getMessage("no_pending_circuit"));
            }
        }

// Command was: "/qc done"
        else if (args[0].equalsIgnoreCase("done")) {

            //They typed "/qc <circuit>"
            if (circuitManager.hasPendingCircuit(player)) {
                PendingCircuit pc = circuitManager.getPendingCircuit(player);
                //They also setup a sender
                if (pc.hasSenderLocation()) {
                    //Finally, they also setup at least one receiver
                    if (pc.hasReceiver()) {
                        circuitManager.addCircuit(pc);

                        // I hate doors, I hate all the wooden doors.
                        // I just want to break them all, but I can't
                        // Can't break all wood doors.
                        if (pc.getSenderLocation().getBlock().getType() == Material.WOODEN_DOOR
                                || pc.getSenderLocation().getBlock().getType() == Material.SPRUCE_DOOR
                                || pc.getSenderLocation().getBlock().getType() == Material.BIRCH_DOOR
                                || pc.getSenderLocation().getBlock().getType() == Material.JUNGLE_DOOR
                                || pc.getSenderLocation().getBlock().getType() == Material.ACACIA_DOOR
                                || pc.getSenderLocation().getBlock().getType() == Material.DARK_OAK_DOOR) {

                            Block bDoor = pc.getSenderLocation().getBlock();
                            int iData = (int) bDoor.getData();
                            Block bOtherPiece = bDoor.getRelative((iData & 0x08) == 0x08 ? BlockFace.DOWN : BlockFace.UP);

                            //TODO: Clone instead of reference the circuit?
                            //TODO: On break check if the circuit has a twin
                            circuitManager.addCircuit(bOtherPiece.getLocation(), pc.getCircuit());
                        }


                        circuitManager.removePendingCircuit(player);

                        messageLogger.msg(player, messageLogger.getMessage("circuit_created")
                                .replace("%circuit%", CircuitTypes.getName(pc.getCurrentType())));
                    }
                    //They have not setup at least one receiver
                    else {
                        messageLogger.msg(player, messageLogger.getMessage("no_receivers"));
                    }
                }
                //They didn't setup a sender
                else {
                    messageLogger.msg(player, messageLogger.getMessage("no_sender"));
                }
            } else {
                messageLogger.msg(player, messageLogger.getMessage("no_pending_action"));
            }
        }

// Command was: "/qc <valid circuit type>"
        else if (circuitManager.isValidCircuitType(args[0])) {

            //Player has permission to create the circuit
            if (player.hasPermission("QuantumConnectors.create." + args[0])) {


                //Figure out if there's a delay, or use 0 for no delay
                double dDelay = 0;

                if (args.length > 1) {
                    try {
                        dDelay = Double.parseDouble(args[1]);
                    } catch (NumberFormatException e) {
                        dDelay = -1;
                    }

                    if (dDelay < 0
                            || (dDelay > QuantumConnectors.MAX_DELAY_TIME && !player.hasPermission("QuantumConnectors.ignoreLimits"))) {
                        dDelay = 0;

                        messageLogger.msg(player, ChatColor.RED + messageLogger.getMessage("invalid_delay").replaceAll("%maxdelay%", new Integer(QuantumConnectors.MAX_DELAY_TIME).toString()));
                    }
                }

                String sDelayMsg = " (" + args[0] + " " + dDelay + "s delay)";


                int iDelayTicks = (int) Math.round(dDelay * 20);


                if (!circuitManager.hasPendingCircuit(player)) {
                    circuitManager.addPendingCircuit(
                            player,
                            circuitManager.getCircuitType(args[0]),
                            iDelayTicks);

                    messageLogger.msg(player, messageLogger.getMessage("circuit_ready")
                            .replace("%circuit%", args[0].toUpperCase())
                            .replace("%delay%", Double.toString(dDelay)));
                } else {

                    circuitManager.getPendingCircuit(player).setCircuitType(
                            circuitManager.getCircuitType(args[0]),
                            iDelayTicks);

                    messageLogger.msg(player, messageLogger.getMessage("circuit_changed")
                            .replace("%circuit%", args[0].toUpperCase())
                            .replace("%delay%", Double.toString(dDelay)));
                }
            }

            //Player doesn't have permission
            else {
                messageLogger.msg(player, ChatColor.RED + messageLogger.getMessage("no_permission").replace("%circuit", args[0].toUpperCase()));
            }
        }

// Command was invalid
        else {
            messageLogger.msg(player, messageLogger.getMessage("invalid_circuit"));
        }

        return true;

    }//End onCommand
}
