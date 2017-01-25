package com.ne0nx3r0.quantum.impl.receiver.base;

import com.ne0nx3r0.quantum.api.circuit.Circuit;
import com.ne0nx3r0.quantum.api.receiver.AbstractReceiver;
import com.ne0nx3r0.quantum.api.receiver.CompatReceiver;
import com.ne0nx3r0.quantum.api.receiver.Receiver;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DelayedCircuit implements Circuit {

    private JavaPlugin javaPlugin;
    private Circuit circuit;

    public DelayedCircuit(JavaPlugin javaPlugin, Circuit circuit) {
        this.javaPlugin = javaPlugin;
        this.circuit = circuit;
    }

    @Override
    public Location getLocation() {
        return circuit.getLocation();
    }

    @Override
    public void setLocation(Location location) {
        circuit.setLocation(location);
    }


    @Override
    public String getType() {
        return circuit.getType();
    }

    @Override
    public void addReceiver(Class<? extends AbstractReceiver> receiverClass, Location loc, int delay) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        circuit.addReceiver(receiverClass, loc, delay);
    }

    @Override
    public void addReceiver(AbstractReceiver receiver) {
        circuit.addReceiver(receiver);
    }

    @Override
    public void addReceiver(Collection<AbstractReceiver> receivers) {
        circuit.addReceiver(receivers);
    }

    @Override
    public void addReceiver(AbstractReceiver... receivers) {
        circuit.addReceiver(receivers);
    }

    @Override
    public List<AbstractReceiver> getReceivers() {
        return circuit.getReceivers();
    }

    @Override
    public int getReceiversCount() {
        return circuit.getReceiversCount();
    }

    @Override
    public List<CompatReceiver> getInValidReceivers() {
        return circuit.getInValidReceivers();
    }

    @Override
    public int getWholeReceiverCount() {
        return circuit.getWholeReceiverCount();
    }

    @Override
    public int getInValidReceiversCount() {
        return circuit.getInValidReceiversCount();
    }

    @Override
    public void delReceiver(Receiver r) {
        circuit.delReceiver(r);
    }

    @Override
    public void delReceiver(Location location) {
        circuit.delReceiver(location);
    }

    @Override
    public boolean isReceiver(Location location) {
        return circuit.isReceiver(location);
    }

    @Override
    public UUID getOwner() {
        return circuit.getOwner();
    }

    @Override
    public void setOwner(UUID playerUUID) {
        circuit.setOwner(playerUUID);
    }

    @Override
    public int getDelay() {
        return circuit.getDelay();
    }

    @Override
    public void setDelay(int delay) {
        circuit.setDelay(delay);
    }

    @Override
    public void calculate(Receiver receiver, int oldCurrent, int newCurrent) {
        circuit.calculate(receiver, oldCurrent, newCurrent);
    }

    @Override
    public void actvate(int oldCurrent, int newCurrent, int chain) {
        Bukkit.getScheduler().runTaskLater(this.javaPlugin, new Runnable() {
            @Override
            public void run() {
                circuit.actvate(oldCurrent, newCurrent, chain);

            }
        }, circuit.getDelay() * 20);
    }

    @Override
    public boolean isValid() {
        return circuit.isValid();
    }

    @Override
    public int getBlockCurrent() {
        return circuit.getBlockCurrent();
    }

    @Override
    public Map<String, Object> serialize() {
        return circuit.serialize();
    }

    @Override
    public List<Material> getValidMaterials() {
        return circuit.getValidMaterials();
    }
}