package com.ne0nx3r0.quantum.circuits;

import com.ne0nx3r0.quantum.ConfigConverter;
import com.ne0nx3r0.quantum.QuantumConnectors;
import com.ne0nx3r0.quantum.ValidMaterials;
import com.ne0nx3r0.quantum.nmswrapper.QSWorld;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.material.Redstone;

import java.io.File;
import java.util.*;

public final class CircuitManager {
    // Temporary Holders for circuit creation
    private Map<String, PendingCircuit> pendingCircuits;
    // keepAlives - lamps/torches/etc that should stay powered regardless of redstone events
    private ArrayList<Block> keepAlives;
    // Allow circuitTypes/circuits
    private Map<String, Integer> circuitTypes = new HashMap<String, Integer>();
    private QuantumConnectors plugin;
    private QSWorld qsWorld;
    // Lookup/Storage for circuits, and subsequently their receivers
    private Map<World, Map<Location, Circuit>> worlds = new HashMap<World, Map<Location, Circuit>>();


    private CircuitLoader circuitLoader;
    // Main
    public CircuitManager(final QuantumConnectors qc, QSWorld qsWorld) {
        this.plugin = qc;
        this.qsWorld = qsWorld;
        this.keepAlives = new ArrayList<>();
        this.circuitLoader = new CircuitLoader(qc, worlds, this);

        //Setup available circuit types
        for (CircuitTypes t : CircuitTypes.values()) {
            circuitTypes.put(t.name, t.id);
        }

        //Create a holder for pending circuits
        this.pendingCircuits = new HashMap<>();

        //Convert circuits.yml to new structure
        if (new File(plugin.getDataFolder(), "circuits.yml").exists()) {
            new ConfigConverter(plugin).convertOldCircuitsYml();
        }

        //Init any loaded worlds
        for (World world : plugin.getServer().getWorlds()) {
            circuitLoader.loadWorld(world);
        }
    }

    public boolean isValidReceiver(Block block) {
        Material mBlock = block.getType();
        for (int i = 0; i < ValidMaterials.validReceivers.length; i++) {
            if (mBlock == ValidMaterials.validReceivers[i]) {
                return true;
            }
        }

        return false;
    }

    // Sender/Receiver checks
    public boolean isValidSender(Block block) {
        Material mBlock = block.getType();
        for (int i = 0; i < ValidMaterials.validSenders.length; i++) {
            if (mBlock == ValidMaterials.validSenders[i]) {
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
        for (int i = 0; i < ValidMaterials.validSenders.length; i++) {
            msg += (i != 0 ? ", " : "") + ValidMaterials.validSenders[i].name().toLowerCase().replace("_", " ");
        }

        return msg;
    }

    public String getValidReceiversString() {
        String msg = "";
        for (int i = 0; i < ValidMaterials.validReceivers.length; i++) {
            msg += (i != 0 ? ", " : "") + ValidMaterials.validReceivers[i].name().toLowerCase().replace("_", " ");
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
        List<Receiver> receivers = circuit.getReceivers();

        if (!receivers.isEmpty()) {
            int iType;
            int iDelay;

            Receiver r;
            for (int i = 0; i < receivers.size(); i++) {
                r = receivers.get(i);
                iType = r.getType();
                iDelay = r.getDelay();
                Block b = r.getLocation().getBlock();

                if (isValidReceiver(b)) {
                    if (iType == CircuitTypes.QUANTUM.getId()) {
                        setReceiver(r, newCurrent > 0, iDelay);
                    } else if (iType == CircuitTypes.ON.getId()) {
                        if (newCurrent > 0 && oldCurrent == 0) {
                            setReceiver(r, true, iDelay);
                        }
                    } else if (iType == CircuitTypes.OFF.getId()) {
                        if (newCurrent == 0 && oldCurrent > 0) {
                            setReceiver(r, false, iDelay);
                        }
                    } else if (iType == CircuitTypes.TOGGLE.getId()) {
                        if (newCurrent > 0 && oldCurrent == 0) {
                            setReceiver(r, getBlockCurrent(b) <= 0, iDelay);
                        }
                    } else if (iType == CircuitTypes.REVERSE.getId()) {
                        if (oldCurrent == 0 || newCurrent == 0) {
                            setReceiver(r, newCurrent <= 0, iDelay);
                        }
                    } else if (iType == CircuitTypes.RANDOM.getId()) {
                        if (newCurrent > 0 && (oldCurrent == 0 || newCurrent == 0)) {
                            setReceiver(r, new Random().nextBoolean(), iDelay);
                        }
                    }

                    if (b.getType() == Material.TNT) { // TnT is one time use!
                        circuit.delReceiver(r);
                    }

                    if (QuantumConnectors.MAX_CHAIN_LINKS > 0) { //allow zero to be infinite
                        chain++;
                    }
                    if (chain <= QuantumConnectors.MAX_CHAIN_LINKS && circuitExists(b.getLocation())) {
                        activateCircuit(r.getLocation(), getBlockCurrent(b), chain);
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

    private void setReceiver(Receiver receiver, boolean on, int iDelay) {
        if (iDelay == 0) {
            setReceiver(receiver, on);
        } else {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(
                    plugin,
                    new DelayedSetReceiver(receiver, on),
                    iDelay);
        }
    }

    private void setReceiver(Receiver receiver, boolean powerOn) {
        receiver.setActive(powerOn);
    }

    // Temporary circuit stuff
// I really don't know what order this deserves among the existing class methods
    public PendingCircuit addPendingCircuit(Player player, int type, int delay) {
        PendingCircuit pc = new PendingCircuit(player.getUniqueId(), type, delay);

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


    public CircuitLoader getCircuitLoader() {
        return circuitLoader;
    }

    public Set<Location> circuitLocations(World w) {
        return worlds.get(w).keySet();
    }

    private class DelayedSetReceiver implements Runnable {
        private final Receiver receiver;
        private final boolean on;

        DelayedSetReceiver(Receiver receiver, boolean on) {
            this.receiver = receiver;
            this.on = on;
        }

        @Override
        public void run() {
            setReceiver(receiver, on);
        }
    }
}
