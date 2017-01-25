package com.ne0nx3r0.quantum.impl.circuits;

import com.ne0nx3r0.quantum.QuantumConnectors;
import com.ne0nx3r0.quantum.api.IRegistry;
import com.ne0nx3r0.quantum.api.QuantumConnectorsAPI;
import com.ne0nx3r0.quantum.api.circuit.AbstractCircuit;
import com.ne0nx3r0.quantum.api.circuit.Circuit;
import com.ne0nx3r0.quantum.impl.interfaces.ICircuitLoader;
import com.ne0nx3r0.quantum.impl.receiver.CompatCircuit;
import com.ne0nx3r0.quantum.impl.receiver.base.Registry;
import com.ne0nx3r0.quantum.impl.utils.MessageLogger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CircuitLoader implements ICircuitLoader {

    private Map<World, Map<Location, AbstractCircuit>> worlds;
    private Map<World, List<Circuit>> invalidCicuitsWorld = new HashMap<>();
    private CircuitManager circuitManager;
    private QuantumConnectors plugin;
    private MessageLogger messageLogger;

    public CircuitLoader(QuantumConnectors plugin, Map<World, Map<Location, AbstractCircuit>> worlds, CircuitManager circuitManager, MessageLogger messageLogger) {
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

            Map<Location, AbstractCircuit> currentWorldCircuits = worlds.get(world);
            List<Circuit> currentInvalidCircuits = invalidCicuitsWorld.get(world);

            List<Map<String, Object>> mapList = new ArrayList<>();

            for (Map.Entry<Location, AbstractCircuit> entry : currentWorldCircuits.entrySet()) {
                mapList.add(entry.getValue().serialize());
            }
            for (Circuit invalidCircuit : currentInvalidCircuits) {
                mapList.add(invalidCircuit.serialize());
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
        Map<Location, AbstractCircuit> worldCircuits = new HashMap<>();
        worlds.put(world, worldCircuits);
        List<Circuit> invalidCircuits = new ArrayList<>();
        invalidCicuitsWorld.put(world, invalidCircuits);

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


        if (tempCircuits.size() == 0) {
            messageLogger.log(messageLogger.getMessage("loading_no_circuits").replace("%file%", ymlFile.getName()));
            return;
        }


        for (Map<?, ?> tempCircuitObj : tempCircuits) {

            Map<String, ?> tempCircuitMap = (Map<String, ?>) tempCircuitObj;

            String circuitType = (String) tempCircuitMap.get("type");

            try {

                IRegistry<AbstractCircuit> circuitRegistry = QuantumConnectorsAPI.getCircuitRegistry();
                Constructor<? extends AbstractCircuit> circuitConstructor = (circuitRegistry instanceof Registry) ? ((Registry<AbstractCircuit>) circuitRegistry).getInstance(circuitType) : null;
                if (circuitConstructor == null) {

                    Circuit circuit = new CompatCircuit((HashMap<String, Object>) tempCircuitMap);
                    invalidCircuits.add(circuit);

                    System.out.println("There is no receiver registered with this type: " + circuitType);
                    continue;
                }

                AbstractCircuit receiver = circuitConstructor.newInstance(tempCircuitMap);

                if (receiver.isValid()) {
                    worldCircuits.put(receiver.getLocation(), receiver);
                }

            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                System.out.println(e.getMessage());
                continue;
            }
        }
        System.out.println("Debug: Anzahl der geladenene Schaltungen in Welt " + world.getName() + ": " + worldCircuits.size());

    }
}
