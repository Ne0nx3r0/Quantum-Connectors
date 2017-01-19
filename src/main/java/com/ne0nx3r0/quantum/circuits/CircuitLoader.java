package com.ne0nx3r0.quantum.circuits;

import com.ne0nx3r0.quantum.QuantumConnectors;
import com.ne0nx3r0.quantum.api.ICircuitLoader;
import com.ne0nx3r0.quantum.api.Receiver;
import com.ne0nx3r0.quantum.utils.MessageLogger;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Need a rewrite how data is loaded
 */
@Deprecated
public class CircuitLoader implements ICircuitLoader {

    private Map<World, Map<Location, Circuit>> worlds;
    private CircuitManager circuitManager;
    private QuantumConnectors plugin;
    private MessageLogger messageLogger;

    public CircuitLoader(QuantumConnectors plugin, Map<World, Map<Location, Circuit>> worlds, CircuitManager circuitManager, MessageLogger messageLogger) {
        this.plugin = plugin;
        this.worlds = worlds;
        this.circuitManager = circuitManager;
        this.messageLogger = messageLogger;
    }

    public void saveAllWorlds() {
        for (World world : worlds.keySet()) {
            saveWorld(world);
        }
        //huh, that was easy.
    }

    public void saveWorld(World world) {
        if (worlds.containsKey(world)) {
            //Alright let's do this!
            File ymlFile = new File(plugin.getDataFolder(), world.getName() + ".circuits.yml");
            if (!ymlFile.exists()) {
                try {
                    ymlFile.createNewFile();
                } catch (IOException ex) {
                    messageLogger.error("Could not create " + ymlFile.getName());
                }
            }
            FileConfiguration yml = YamlConfiguration.loadConfiguration(ymlFile);

            if (QuantumConnectors.VERBOSE_LOGGING)
                messageLogger.log(messageLogger.getMessage("saving").replace("%file", ymlFile.getName()));

            //Prep this world's data for saving
            List<Object> tempCircuits = new ArrayList<Object>();

            Map<String, Object> tempCircuitObj;
            Map<String, Object> tempReceiverObj;
            ArrayList<Map<String, Object>> tempReceiverObjs;
            Circuit currentCircuit;
            List<Receiver> currentReceivers;

            Map<Location, Circuit> currentWorldCircuits = worlds.get(world);

            for (Location cLoc : currentWorldCircuits.keySet()) {

                currentCircuit = currentWorldCircuits.get(cLoc);

                tempCircuitObj = new HashMap<>();

                tempCircuitObj.put("x", cLoc.getBlockX());
                tempCircuitObj.put("y", cLoc.getBlockY());
                tempCircuitObj.put("z", cLoc.getBlockZ());
                System.out.println(currentCircuit.getOwner().toString());
                tempCircuitObj.put("o", currentCircuit.getOwner().toString());
                tempCircuitObj.put("type", currentCircuit.getCircuitType().name);
                currentReceivers = currentCircuit.getReceivers();

                tempReceiverObjs = new ArrayList<>();
                Receiver r;
                for (int i = 0; i < currentReceivers.size(); i++) {
                    r = currentReceivers.get(i);

                    tempReceiverObj = new HashMap<>();

                    tempReceiverObj.put("z", r.getLocation().getBlockZ());
                    tempReceiverObj.put("y", r.getLocation().getBlockY());
                    tempReceiverObj.put("x", r.getLocation().getBlockX());

                    tempReceiverObj.put("d", r.getDelay());
                    tempReceiverObj.put("t", r.getType());

                    tempReceiverObjs.add(tempReceiverObj);
                }

                tempCircuitObj.put("r", tempReceiverObjs);

                tempCircuits.add(tempCircuitObj);
            }

            yml.set("fileVersion", "3");
            yml.set("circuits", tempCircuits);

            try {
                yml.save(ymlFile);

                if (QuantumConnectors.VERBOSE_LOGGING)
                    messageLogger.log(messageLogger.getMessage("saved").replace("%file", ymlFile.getName()));
            } catch (IOException IO) {
                messageLogger.error(messageLogger.getMessage("save_failed").replace("%world", world.getName()));
            }
        } else {
            messageLogger.error(messageLogger.getMessage("save_failed").replace("%world", world.getName()));
        }
    }

    @Override
    public void loadWorlds() {
        throw new NotImplementedException("load Worlds is not implemented atm.");
    }

    // TODO: 19.01.17 CircuitLoader needs a rewrite
    public void loadWorld(World world) {
        //at least create a blank holder
        worlds.put(world, new HashMap<>());

        File ymlFile = new File(plugin.getDataFolder(), world.getName() + ".circuits.yml");

        if (QuantumConnectors.VERBOSE_LOGGING)
            messageLogger.log(messageLogger.getMessage("loading").replace("%file%", ymlFile.getName()));

        if (!ymlFile.exists()) {
            if (QuantumConnectors.VERBOSE_LOGGING)
                messageLogger.error(messageLogger.getMessage("loading_not_found").replace("%file%", ymlFile.getName()));
            return;
        }

        FileConfiguration yml = YamlConfiguration.loadConfiguration(ymlFile);


        System.out.println(String.join(", ", yml.getKeys(true)));

        ConfigurationSection section = yml.getConfigurationSection("circuits");
        System.out.println(String.join(", ", section != null ? section.getKeys(true) : new ArrayList<String>()));

        // TODO: 19.01.17 read circuits

        List<Map<String, Object>> tempCircuits = new ArrayList(yml.getMapList("circuits"));
        System.out.println("Debug: Anzahl Schaltungen " + tempCircuits.size());

        if (tempCircuits.size() == 0) {
            messageLogger.log(messageLogger.getMessage("loading_no_circuits").replace("%file%", ymlFile.getName()));
            return;
        }

        Map<Location, Circuit> worldCircuits = new HashMap<>();
        Location tempCircuitObjLoc;
        Map<String, Object> tempReceiverObj;
        ArrayList<Receiver> tempReceiverObjs;
        Location tempReceiverLoc;

        for (Map<String, Object> tempCircuitObj : tempCircuits) {
            //dummy value of # for owners
            Circuit tempCircuit = new Circuit(
                    UUID.fromString(tempCircuitObj.get("o") == null ? "" : (String) tempCircuitObj.get("o")),
                    circuitManager,
                    CircuitTypes.getByName((String) tempCircuitObj.get("type"))
            );

            tempReceiverObjs = (ArrayList) tempCircuitObj.get("r");

            //TODO: circuit/receiver verification
            for (int i = 0; i < tempReceiverObjs.size(); i++) {
                tempReceiverObj = (Map<String, Object>) tempReceiverObjs.get(i);
                tempReceiverLoc = new Location(
                        world,
                        (Integer) tempReceiverObj.get("x"),
                        (Integer) tempReceiverObj.get("y"),
                        (Integer) tempReceiverObj.get("z"));

                if (circuitManager.isValidReceiver(tempReceiverLoc.getBlock())) {
                    tempCircuit.addReceiver(
                            tempReceiverLoc,
                            (Integer) tempReceiverObj.get("d")
                    );
                }


                //Invalid receiver block type
                else {
                    if (QuantumConnectors.VERBOSE_LOGGING) messageLogger.log(
                            messageLogger.getMessage("receiver_removed")
                                    .replace("%world%", world.getName())
                                    .replace("%block%", tempReceiverLoc.getBlock().getType().name())
                    );
                }
            }

            // Verify there is at least one valid receiver
            if (!tempCircuit.getReceivers().isEmpty()) {
                tempCircuitObjLoc = new Location(
                        world,
                        (Integer) tempCircuitObj.get("x"),
                        (Integer) tempCircuitObj.get("y"),
                        (Integer) tempCircuitObj.get("z"));

                //Verify the sender is a valid type
                if (circuitManager.isValidSender(tempCircuitObjLoc.getBlock())) {
                    worldCircuits.put(tempCircuitObjLoc, tempCircuit);
                }
                //Invalid sender type
                else {
                    if (QuantumConnectors.VERBOSE_LOGGING)
                        messageLogger.log(messageLogger.getMessage("circuit_removed_invalid")
                                .replace("%world", world.getName())
                                .replace("%block%", tempCircuitObjLoc.getBlock().getType().name()));
                }
            }
            // No valid receivers for this circuit
            else {
                if (QuantumConnectors.VERBOSE_LOGGING)
                    messageLogger.log(messageLogger.getMessage("circuit_removed_no_receivers")
                            .replace("%world%", world.getName()));
            }
        }

        // TODO: 19.01.17 to here

        worlds.put(world, worldCircuits);
    }


}
