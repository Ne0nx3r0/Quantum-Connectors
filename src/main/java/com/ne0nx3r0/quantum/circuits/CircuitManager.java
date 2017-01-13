package com.ne0nx3r0.quantum.circuits;

import com.ne0nx3r0.quantum.QuantumConnectors;
import com.ne0nx3r0.quantum.nmswrapper.QSWorld;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.material.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CircuitManager {
    // Temporary Holders for circuit creation
    public Map<String, PendingCircuit> pendingCircuits;
    // keepAlives - lamps/torches/etc that should stay powered regardless of redstone events
    public ArrayList<Block> keepAlives;
    // Allow circuitTypes/circuits
    public Map<String, Integer> circuitTypes = new HashMap<String, Integer>();
    private QuantumConnectors plugin;
    private QSWorld qsWorld;
    // Lookup/Storage for circuits, and subsequently their receivers
    private Map<World, Map<Location, Circuit>> worlds = new HashMap<World, Map<Location, Circuit>>();
    private Material[] validSenders = new Material[]{
            Material.LEVER,
            Material.REDSTONE_WIRE,
            Material.STONE_BUTTON,
            Material.STONE_PLATE,
            Material.WOOD_PLATE,
            Material.REDSTONE_TORCH_OFF,
            Material.REDSTONE_TORCH_ON,
            Material.REDSTONE_LAMP_OFF,
            Material.REDSTONE_LAMP_ON,
            //Material.DIODE_BLOCK_OFF,
            //Material.DIODE_BLOCK_ON,//TODO: Figure out repeaters as senders
            Material.IRON_DOOR_BLOCK,
            Material.WOODEN_DOOR,
            Material.SPRUCE_DOOR,
            Material.BIRCH_DOOR,
            Material.JUNGLE_DOOR,
            Material.ACACIA_DOOR,
            Material.DARK_OAK_DOOR,
            Material.TRAP_DOOR,
            Material.FENCE_GATE,
            Material.SPRUCE_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.ACACIA_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE,
            Material.CHEST,
            Material.BOOKSHELF,
            Material.BED_BLOCK,
            Material.FURNACE,
            Material.WOOD_BUTTON,
            //Material.REDSTONE_COMPARATOR,
            //Material.REDSTONE_COMPARATOR_ON,
            //Material.REDSTONE_COMPARATOR_OFF,
            //Material.DAYLIGHT_DETECTOR,
            Material.DETECTOR_RAIL,
            Material.IRON_PLATE,
            Material.GOLD_PLATE,
            //Material.POWERED_RAIL,//TODO: Figure out powered rail as sender
            //Material.PISTON_BASE,
            //Material.PISTON_STICKY_BASE,//TODO: Pistons as senders
    };
    private Material[] validReceivers = new Material[]{
            Material.LEVER,
            Material.IRON_DOOR_BLOCK,
            Material.WOODEN_DOOR,
            Material.SPRUCE_DOOR,
            Material.BIRCH_DOOR,
            Material.JUNGLE_DOOR,
            Material.ACACIA_DOOR,
            Material.DARK_OAK_DOOR,
            Material.TRAP_DOOR,
            Material.POWERED_RAIL,
            Material.FENCE_GATE,
            Material.SPRUCE_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.ACACIA_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE,
            Material.REDSTONE_LAMP_OFF,
            Material.REDSTONE_LAMP_ON,
            //Material.REDSTONE_TORCH_OFF,
            //Material.REDSTONE_TORCH_ON,
            //Material.PISTON_BASE,
            //Material.PISTON_STICKY_BASE,//TODO: Pistons as receivers
    };

    // Main
    public CircuitManager(final QuantumConnectors qc, QSWorld qsWorld) {
        this.plugin = qc;
        this.qsWorld = qsWorld;
        this.keepAlives = new ArrayList<>();

        //Setup available circuit types
        for (CircuitTypes t : CircuitTypes.values()) {
            circuitTypes.put(t.name, t.id);
        }

        //Create a holder for pending circuits
        pendingCircuits = new HashMap<>();

        //Convert circuits.yml to new structure
        if (new File(plugin.getDataFolder(), "circuits.yml").exists()) {
            convertOldCircuitsYml();
        }

        //Init any loaded worlds
        for (World world : plugin.getServer().getWorlds()) {
            loadWorld(world);
        }
    }

    public boolean isValidReceiver(Block block) {
        Material mBlock = block.getType();
        for (int i = 0; i < validReceivers.length; i++) {
            if (mBlock == validReceivers[i]) {
                return true;
            }
        }

        return false;
    }

    // Sender/Receiver checks
    public boolean isValidSender(Block block) {
        Material mBlock = block.getType();
        for (int i = 0; i < validSenders.length; i++) {
            if (mBlock == validSenders[i]) {
                return true;
            }
        }

        return false;
    }

    public boolean shouldLeaveReceiverOn(Block block) {
        return keepAlives.contains(block);
    }

    public String getValidSendersString() {
        String msg = "";
        for (int i = 0; i < validSenders.length; i++) {
            msg += (i != 0 ? ", " : "") + validSenders[i].name().toLowerCase().replace("_", " ");
        }

        return msg;
    }

    public String getValidReceiversString() {
        String msg = "";
        for (int i = 0; i < validReceivers.length; i++) {
            msg += (i != 0 ? ", " : "") + validReceivers[i].name().toLowerCase().replace("_", " ");
        }

        return msg;
    }

    // Circuit (sender) CRUD
    public void addCircuit(Location circuitLocation, Circuit newCircuit) {
        //Notably circuits are now created from a temporary copy, rather than piecemeal here.
        worlds.get(circuitLocation.getWorld()).put(circuitLocation, newCircuit);
    }

    public void addCircuit(PendingCircuit pc) {
        worlds.get(pc.getSenderLocation().getWorld())
                .put(pc.getSenderLocation(), pc.getCircuit());
    }

    public Circuit getCircuit(Location circuitLocation) {
        return worlds.get(circuitLocation.getWorld()).get(circuitLocation);
    }

    public void removeCircuit(Location circuitLocation) {
        if (circuitExists(circuitLocation)) {
            worlds.get(circuitLocation.getWorld()).remove(circuitLocation);
        }
    }

    public boolean circuitExists(Location circuitLocation) {
        return worlds.get(circuitLocation.getWorld()).containsKey(circuitLocation);
    }

    // Circuit activation
    public void activateCircuit(Location lSender, int oldCurrent, int newCurrent) {
        activateCircuit(lSender, oldCurrent, newCurrent, 0);
    }

    public void activateCircuit(Location lSender, int oldCurrent, int newCurrent, int chain) {
        Circuit circuit = getCircuit(lSender);
        List receivers = circuit.getReceivers();

        if (!receivers.isEmpty()) {
            int iType;
            int iDelay;

            Receiver r;
            for (int i = 0; i < receivers.size(); i++) {
                r = (Receiver) receivers.get(i);
                iType = r.type;
                iDelay = r.delay;
                Block b = r.location.getBlock();

                if (isValidReceiver(b)) {
                    if (iType == CircuitTypes.QUANTUM.getId()) {
                        setReceiver(b, newCurrent > 0, iDelay);
                    } else if (iType == CircuitTypes.ON.getId()) {
                        if (newCurrent > 0 && oldCurrent == 0) {
                            setReceiver(b, true, iDelay);
                        }
                    } else if (iType == CircuitTypes.OFF.getId()) {
                        if (newCurrent == 0 && oldCurrent > 0) {
                            setReceiver(b, false, iDelay);
                        }
                    } else if (iType == CircuitTypes.TOGGLE.getId()) {
                        if (newCurrent > 0 && oldCurrent == 0) {
                            setReceiver(b, getBlockCurrent(b) <= 0, iDelay);
                        }
                    } else if (iType == CircuitTypes.REVERSE.getId()) {
                        if (oldCurrent == 0 || newCurrent == 0) {
                            setReceiver(b, newCurrent <= 0, iDelay);
                        }
                    } else if (iType == CircuitTypes.RANDOM.getId()) {
                        if (newCurrent > 0 && (oldCurrent == 0 || newCurrent == 0)) {
                            setReceiver(b, new Random().nextBoolean(), iDelay);
                        }
                    }

                    if (b.getType() == Material.TNT) { // TnT is one time use!
                        circuit.delReceiver(r);
                    }

                    if (QuantumConnectors.MAX_CHAIN_LINKS > 0) { //allow zero to be infinite
                        chain++;
                    }
                    if (chain <= QuantumConnectors.MAX_CHAIN_LINKS && circuitExists(b.getLocation())) {
                        activateCircuit(r.location, getBlockCurrent(b), chain);
                    }
                } else {
                    circuit.delReceiver(r);
                }
            }
        }
    }

    public int getBlockCurrent(Block b) {
        Material mBlock = b.getType();
        MaterialData md = b.getState().getData();
        if (md instanceof Redstone) {
            return ((Redstone) md).isPowered() ? 15 : 0;
        } else if (md instanceof Openable) {
            return ((Openable) md).isOpen() ? 15 : 0;
        } else if (mBlock == Material.REDSTONE_LAMP_OFF
                || mBlock == Material.REDSTONE_LAMP_ON) {
            return keepAlives.contains(b) ? 15 : 0;
        }

        return b.getBlockPower();
    }

    private void setReceiver(Block block, boolean on, int iDelay) {
        if (iDelay == 0) {
            setReceiver(block, on);
        } else {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(
                    plugin,
                    new DelayedSetReceiver(block, on),
                    iDelay);
        }
    }

    private void setReceiver(Block block, boolean powerOn) {
        Material mBlock = block.getType();

        BlockState state = block.getState();
        MaterialData data = state.getData();


        if (data instanceof Lever) {
            Lever lever = (Lever) data;
            lever.setPowered(powerOn);
            state.setData(lever);
            state.update();
        } else if (data instanceof PoweredRail) {

            PoweredRail poweredRail = (PoweredRail) data;
            poweredRail.setPowered(powerOn);
            state.setData(poweredRail);
            state.update();

        } else if (data instanceof Openable) {
            ((Openable) data).setOpen(powerOn);
            state.setData(data);
            state.update();
            block.getWorld().playEffect(block.getLocation(), Effect.DOOR_TOGGLE, 0, 10);
        } else if (data instanceof PistonBaseMaterial) {
            ((PistonBaseMaterial) data).setPowered(powerOn);
            state.setData(data);
        } else if (mBlock == Material.REDSTONE_LAMP_ON) {
            if (!powerOn) {
                keepAlives.remove(block);
                block.setType(Material.REDSTONE_LAMP_OFF);
            }
        } else if (mBlock == Material.REDSTONE_LAMP_OFF) {
            if (powerOn) {
                keepAlives.add(block);
                this.qsWorld.setStatic(state.getWorld(), true);
                block.setType(Material.REDSTONE_LAMP_ON);
                this.qsWorld.setStatic(state.getWorld(), false);
            }
        }
    }

    // Temporary circuit stuff
// I really don't know what order this deserves among the existing class methods
    public PendingCircuit addPendingCircuit(Player player, int type, int delay) {
        PendingCircuit pc = new PendingCircuit(player.getName(), type, delay);

        pendingCircuits.put(player.getName(), pc);

        return pc;
    }

    public PendingCircuit getPendingCircuit(Player player) {
        return pendingCircuits.get(player.getName());
    }

    public boolean hasPendingCircuit(Player player) {
        return pendingCircuits.containsKey(player.getName());
    }

    public void removePendingCircuit(Player player) {
        pendingCircuits.remove(player.getName());
    }

    //Circuit Types
    public boolean isValidCircuitType(String type) {
        return circuitTypes.containsKey(type);
    }

    public int getCircuitType(String sType) {
        return circuitTypes.get(sType);
    }

    public Map<String, Integer> getValidCircuitTypes() {
        return circuitTypes;
    }

    //1.2.3 circuits.yml Converter
    public void convertOldCircuitsYml() {
        File oldYmlFile = new File(plugin.getDataFolder(), "circuits.yml");
        if (oldYmlFile.exists()) {
            plugin.log(plugin.getMessage("found_old_file").replace("%file%", oldYmlFile.getName()));

            FileConfiguration oldYml = YamlConfiguration.loadConfiguration(oldYmlFile);

            for (String worldName : oldYml.getValues(false).keySet()) {
                ArrayList tempCircuitObjs = new ArrayList();

                for (int x = 0; ; x++) {
                    String path = worldName + ".circuit_" + x;

                    if (oldYml.get(path) == null) {
                        break;
                    }

                    Map<String, Object> tempCircuitObj = new HashMap<String, Object>();

                    String[] senderXYZ = oldYml.get(path + ".sender").toString().split(",");

                    tempCircuitObj.put("x", Integer.parseInt(senderXYZ[0]));
                    tempCircuitObj.put("y", Integer.parseInt(senderXYZ[1]));
                    tempCircuitObj.put("z", Integer.parseInt(senderXYZ[2]));

                    //they'll all be the same, should only ever be one anyway
                    String receiversType = oldYml.get(path + ".type").toString();

                    ArrayList tempReceiverObjs = new ArrayList();
                    for (Object receiver : oldYml.getList(path + ".receivers")) {
                        Map<String, Object> tempReceiverObj = new HashMap<String, Object>();

                        String[] sReceiverLoc = receiver.toString().split(",");

                        tempReceiverObj.put("x", Integer.parseInt(sReceiverLoc[0]));
                        tempReceiverObj.put("y", Integer.parseInt(sReceiverLoc[1]));
                        tempReceiverObj.put("z", Integer.parseInt(sReceiverLoc[2]));
                        tempReceiverObj.put("d", 0);
                        tempReceiverObj.put("t", Integer.parseInt(receiversType));

                        tempReceiverObjs.add(tempReceiverObj);
                    }

                    tempCircuitObj.put("r", tempReceiverObjs);

                    tempCircuitObjs.add(tempCircuitObj);
                }

                File newYmlFile = new File(plugin.getDataFolder(), worldName + ".circuits.yml");
                FileConfiguration newYml = YamlConfiguration.loadConfiguration(newYmlFile);

                newYml.set("fileVersion", 2);
                newYml.set("circuits", tempCircuitObjs);

                try {
                    newYml.save(newYmlFile);
                } catch (IOException ex) {
                    plugin.error(plugin.getMessage("unable_to_save").replace("%file%", newYmlFile.getName()));

                    Logger.getLogger(CircuitManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            //I dunno man. Java file operations are a mystery to me. These lines worked.
            File testFile = new File(plugin.getDataFolder(), "circuits.yml.bak");
            new File(plugin.getDataFolder(), "circuits.yml").renameTo(testFile);
        }
    }

    public void loadWorld(World world) {
        //at least create a blank holder
        worlds.put(world, new HashMap<Location, Circuit>());

        File ymlFile = new File(plugin.getDataFolder(), world.getName() + ".circuits.yml");

        if (QuantumConnectors.VERBOSE_LOGGING)
            plugin.log(plugin.getMessage("loading").replace("%file%", ymlFile.getName()));

        if (!ymlFile.exists()) {
            if (QuantumConnectors.VERBOSE_LOGGING)
                plugin.error(plugin.getMessage("loading_not_found").replace("%file%", ymlFile.getName()));
            return;
        }

        FileConfiguration yml = YamlConfiguration.loadConfiguration(ymlFile);

        List<Map<String, Object>> tempCircuits = (List<Map<String, Object>>) yml.get("circuits");

        if (tempCircuits == null) {
            plugin.log(plugin.getMessage("loading_no_circuits").replace("%file%", ymlFile.getName()));
            return;
        }

        Map<Location, Circuit> worldCircuits = new HashMap<Location, Circuit>();

        Location tempCircuitObjLoc;

        ArrayList tempReceiverObjs;
        Map<String, Object> tempReceiverObj;
        Location tempReceiverLoc;

        for (Map<String, Object> tempCircuitObj : tempCircuits) {
            //dummy value of # for owners
            Circuit tempCircuit = new Circuit(tempCircuitObj.get("o") == null ? "" : (String) tempCircuitObj.get("o"));
            tempReceiverObjs = (ArrayList) tempCircuitObj.get("r");

            //TODO: circuit/receiver verification
            for (int i = 0; i < tempReceiverObjs.size(); i++) {
                tempReceiverObj = (Map<String, Object>) tempReceiverObjs.get(i);
                tempReceiverLoc = new Location(
                        world,
                        (Integer) tempReceiverObj.get("x"),
                        (Integer) tempReceiverObj.get("y"),
                        (Integer) tempReceiverObj.get("z"));

                if (isValidReceiver(tempReceiverLoc.getBlock())) {
                    tempCircuit.addReceiver(
                            tempReceiverLoc,
                            (Integer) tempReceiverObj.get("t"),
                            (Integer) tempReceiverObj.get("d"));
                }
                //Invalid receiver block type
                else {
                    if (QuantumConnectors.VERBOSE_LOGGING) plugin.log(
                            plugin.getMessage("receiver_removed")
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
                if (isValidSender(tempCircuitObjLoc.getBlock())) {
                    worldCircuits.put(tempCircuitObjLoc, tempCircuit);
                }
                //Invalid sender type
                else {
                    if (QuantumConnectors.VERBOSE_LOGGING)
                        plugin.log(plugin.getMessage("circuit_removed_invalid")
                                .replace("%world", world.getName())
                                .replace("%block%", tempCircuitObjLoc.getBlock().getType().name()));
                }
            }
            // No valid receivers for this circuit
            else {
                if (QuantumConnectors.VERBOSE_LOGGING)
                    plugin.log(plugin.getMessage("circuit_removed_no_receivers")
                            .replace("%world%", world.getName()));
            }
        }

        worlds.put(world, worldCircuits);
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
                    plugin.error("Could not create " + ymlFile.getName());
                }
            }
            FileConfiguration yml = YamlConfiguration.loadConfiguration(ymlFile);

            if (QuantumConnectors.VERBOSE_LOGGING)
                plugin.log(plugin.getMessage("saving").replace("%file", ymlFile.getName()));

            //Prep this world's data for saving
            List<Object> tempCircuits = new ArrayList<Object>();

            Map<String, Object> tempCircuitObj;
            Map<String, Object> tempReceiverObj;
            ArrayList tempReceiverObjs;
            Circuit currentCircuit;
            List<Receiver> currentReceivers;

            Map<Location, Circuit> currentWorldCircuits = worlds.get(world);

            for (Location cLoc : currentWorldCircuits.keySet()) {

                currentCircuit = currentWorldCircuits.get(cLoc);

                tempCircuitObj = new HashMap<String, Object>();

                tempCircuitObj.put("x", cLoc.getBlockX());
                tempCircuitObj.put("y", cLoc.getBlockY());
                tempCircuitObj.put("z", cLoc.getBlockZ());

                tempCircuitObj.put("o", currentCircuit.getOwner());

                currentReceivers = currentCircuit.getReceivers();

                tempReceiverObjs = new ArrayList();
                Receiver r;
                for (int i = 0; i < currentReceivers.size(); i++) {
                    r = currentReceivers.get(i);

                    tempReceiverObj = new HashMap<String, Object>();

                    tempReceiverObj.put("z", r.location.getBlockZ());
                    tempReceiverObj.put("y", r.location.getBlockY());
                    tempReceiverObj.put("x", r.location.getBlockX());

                    tempReceiverObj.put("d", r.delay);
                    tempReceiverObj.put("t", r.type);

                    tempReceiverObjs.add(tempReceiverObj);
                }

                tempCircuitObj.put("r", tempReceiverObjs);

                tempCircuits.add(tempCircuitObj);
            }

            yml.set("fileVersion", "2");
            yml.set("circuits", tempCircuits);

            try {
                yml.save(ymlFile);

                if (QuantumConnectors.VERBOSE_LOGGING)
                    plugin.log(plugin.getMessage("saved").replace("%file", ymlFile.getName()));
            } catch (IOException IO) {
                plugin.error(plugin.getMessage("save_failed").replace("%world", world.getName()));
            }
        } else {
            plugin.error(plugin.getMessage("save_failed").replace("%world", world.getName()));
        }
    }

    public Set<Location> circuitLocations(World w) {
        return worlds.get(w).keySet();
    }

    private class DelayedSetReceiver implements Runnable {
        private final Block block;
        private final boolean on;

        DelayedSetReceiver(Block block, boolean on) {
            this.block = block;
            this.on = on;
        }

        @Override
        public void run() {
            setReceiver(block, on);
        }
    }
}
