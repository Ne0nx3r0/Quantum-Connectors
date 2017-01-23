package com.ne0nx3r0.quantum.impl.circuits;

import com.ne0nx3r0.quantum.QuantumConnectors;
import com.ne0nx3r0.quantum.impl.interfaces.ICircuitLoader;
import com.ne0nx3r0.quantum.impl.utils.MessageLogger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            Map<Location, Circuit> currentWorldCircuits = worlds.get(world);

            List<Map<String, Object>> mapList = new ArrayList<>();

            for (Map.Entry<Location, Circuit> entry : currentWorldCircuits.entrySet()) {
                mapList.add(entry.getValue().serialize());
            }

            yml.set("fileVersion", "3");
            yml.set("circuits", mapList);

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
        for (World world : Bukkit.getWorlds()) {
            loadWorld(world);
        }
    }

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

        List<Map<?, ?>> tempCircuits = yml.getMapList("circuits");


        System.out.println("Debug: Anzahl Schaltungen " + tempCircuits.size());

        if (tempCircuits.size() == 0) {
            messageLogger.log(messageLogger.getMessage("loading_no_circuits").replace("%file%", ymlFile.getName()));
            return;
        }

        Map<Location, Circuit> worldCircuits = new HashMap<>();
        for (Map<?, ?> tempCircuitObj : tempCircuits) {

            Map<String, ?> tempCircuitMap = (Map<String, ?>) tempCircuitObj;

            //dummy value of # for owners
            Circuit tempCircuit = new Circuit(tempCircuitMap);

            // Verify there is at least one valid receiver
            if (!tempCircuit.getReceivers().isEmpty()) {


                //Verify the sender is a valid type
                if (circuitManager.isValidSender(tempCircuit.getLocation().getBlock())) {
                    worldCircuits.put(tempCircuit.getLocation(), tempCircuit);
                }
                //Invalid sender type
                else {
                    if (QuantumConnectors.VERBOSE_LOGGING)
                        messageLogger.log(messageLogger.getMessage("circuit_removed_invalid")
                                .replace("%world", world.getName())
                                .replace("%block%", tempCircuit.getLocation().getBlock().getType().name()));
                }
            }
            // No valid receivers for this circuit
            else {
                if (QuantumConnectors.VERBOSE_LOGGING)
                    messageLogger.log(messageLogger.getMessage("circuit_removed_no_receivers")
                            .replace("%world%", world.getName()));
            }
        }

        worlds.put(world, worldCircuits);
    }


}
