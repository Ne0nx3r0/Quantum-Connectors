package com.ne0nx3r0.quantum.api.circuit;

import com.ne0nx3r0.quantum.api.IQuantumConnectorsAPI;
import com.ne0nx3r0.quantum.api.IRegistry;
import com.ne0nx3r0.quantum.api.QuantumConnectorsAPI;
import com.ne0nx3r0.quantum.api.receiver.AbstractKeepAliveReceiver;
import com.ne0nx3r0.quantum.api.receiver.AbstractReceiver;
import com.ne0nx3r0.quantum.api.receiver.CompatReceiver;
import com.ne0nx3r0.quantum.api.receiver.Receiver;
import com.ne0nx3r0.quantum.api.util.ValidMaterials;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.material.Redstone;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by Yannick on 23.01.2017.
 */
public abstract class AbstractCircuit implements Circuit {

    protected final IQuantumConnectorsAPI api = QuantumConnectorsAPI.getAPI();

    protected List<Receiver> invalidReceivers = new ArrayList<>();
    protected Map<Location, AbstractReceiver> receivers = new HashMap<>();
    protected UUID playerUUID;
    protected Location location;
    protected int delay;


    public AbstractCircuit(UUID playerUUID, int delay) {
        this.playerUUID = playerUUID;
        this.delay = delay;
    }

    public AbstractCircuit(Map<?, ?> map) {
        this.playerUUID = UUID.fromString((String) map.get("owner"));
        this.location = new Location(Bukkit.getWorld((String) map.get("world")), (Integer) map.get("x"), (Integer) map.get("y"), (Integer) map.get("z"));
        this.delay = (Integer) map.get("delay");
        List<?> reciverObjectList = (List<?>) map.get("receiver");

        for (Object receiverObject : reciverObjectList) {

            Map<?, ?> receiverMap = (Map<?, ?>) receiverObject;

            String type = (String) receiverMap.get("type");
            try {

                IRegistry<AbstractReceiver> receiverIRegistry = QuantumConnectorsAPI.getReceiverRegistry();

                Constructor<? extends AbstractReceiver> receiverConstructor = receiverIRegistry.getInstance(type);
                if (receiverConstructor == null) {

                    Receiver receiver = new CompatReceiver((HashMap<String, Object>) receiverMap);
                    invalidReceivers.add(receiver);

                    System.out.println("There is no receiver registered with this type: " + type);
                    continue;
                }

                AbstractReceiver receiver = receiverConstructor.newInstance(receiverMap);

                if (receiver.isValid()) {
                    receivers.put(receiver.getLocation(), receiver);
                }

            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public final String getType() {
        return QuantumConnectorsAPI.getCircuitRegistry().getUniqueKey(this.getClass());
    }


    @Override
    public void addReceiver(Class<? extends AbstractReceiver> receiverClass, Location loc, int delay) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        IRegistry<AbstractReceiver> receiverIRegistry = QuantumConnectorsAPI.getReceiverRegistry();
        addReceiver(receiverIRegistry.instantiateFrom(receiverClass, loc, delay));
    }

    @Override
    public void addReceiver(Collection<AbstractReceiver> receivers) {
        receivers.addAll(receivers);
    }

    @Override
    public void addReceiver(AbstractReceiver... receivers) {
        addReceiver(Arrays.asList(receivers));
    }


    public void addInvalidReceiver(Receiver... receivers) {
        addInvalidReceiver(Arrays.asList(receivers));
    }

    public void addInvalidReceiver(Collection<Receiver> receivers) {
        invalidReceivers.addAll(receivers);
    }


    @Override
    public List<AbstractReceiver> getReceivers() {
        return new ArrayList<>(receivers.values());
    }

    @Override
    public int getReceiversCount() {
        return receivers.size();
    }

    @Override
    public List<Receiver> getInValidReceivers() {
        return new ArrayList<>(invalidReceivers);
    }

    @Override
    public int getWholeReceiverCount() {
        return invalidReceivers.size() + receivers.size();
    }

    @Override
    public int getInValidReceiversCount() {
        return invalidReceivers.size();
    }

    @Override
    public void delReceiver(Receiver r) {
        delReceiver(r.getLocation());
    }

    @Override
    public void delReceiver(Location location) {
        receivers.remove(location);
    }


    @Override
    public boolean isReceiver(Location location) {
        return receivers.containsKey(location);
    }

    @Override
    public UUID getOwner() {
        return playerUUID;
    }

    @Override
    public void setOwner(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }


    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("owner", playerUUID.toString());
        map.put("type", getType());

        List<Map<String, ?>> receiverMap = new ArrayList<>();

        for (Receiver receiver : receivers.values()) {
            receiverMap.add(receiver.serialize());
        }
        for (Receiver receiver : invalidReceivers) {
            receiverMap.add(receiver.serialize());
        }
        map.put("receiver", receiverMap);
        map.put("world", location.getWorld().getName());
        map.put("x", location.getBlockX());
        map.put("y", location.getBlockY());
        map.put("z", location.getBlockZ());
        map.put("delay", delay);
        return map;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int getDelay() {
        return delay;
    }

    @Override
    public void setDelay(int delay) {
        this.delay = delay;
    }


    @Override
    public void actvate(int oldCurrent, int newCurrent, int chain) {

        List<Receiver> receivers = new ArrayList<>(this.getReceivers());

        for (Receiver receiver : receivers) {

            if (receiver.isValid()) {

                int receiverOldCurrent = receiver.getBlockCurrent();
                //
                calculate(receiver, oldCurrent, newCurrent);

                if (receiver.getLocation().getBlock().getType() == Material.TNT) { // TnT is one time use!
                    this.delReceiver(receiver);
                }

                final int maxChainLinks = api.getMaxChainLinks();
                if (chain <= maxChainLinks - 2 && api.circuitExists(receiver.getLocation())) {
                    if (maxChainLinks > 0) { //allow zero to be infinite
                        chain++;
                    }


                    api.activateCircuit(receiver.getLocation(), receiverOldCurrent, receiver.getBlockCurrent(), chain);
                }
            } else {
                this.delReceiver(receiver);
            }
        }


    }

    @Override
    public int getBlockCurrent() {
        Block b = location.getBlock();
        Material material = b.getType();
        MaterialData md = b.getState().getData();
        if (md instanceof Redstone) {
            return ((Redstone) md).isPowered() ? 15 : 0;
        } else if (md instanceof Openable) {
            return ((Openable) md).isOpen() ? 15 : 0;
        } else if (ValidMaterials.LAMP.contains(material)) {
            return AbstractKeepAliveReceiver.keepAlives.contains(b) ? 15 : 0;
        }
        return b.getBlockPower();
    }

    @Override
    public void addReceiver(AbstractReceiver receiver) {
        receivers.put(receiver.getLocation(), receiver);
    }

    @Override
    public boolean isValid() {
        return getValidMaterials().contains(location.getBlock().getType());
    }

    @Override
    public List<Material> getValidMaterials() {
        return ValidMaterials.validSenders;
    }
}
