package com.ne0nx3r0.quantum.impl.receiver;

import com.ne0nx3r0.quantum.api.circuit.Circuit;
import com.ne0nx3r0.quantum.api.receiver.AbstractReceiver;
import com.ne0nx3r0.quantum.api.receiver.CompatReceiver;
import com.ne0nx3r0.quantum.api.receiver.Receiver;
import org.bukkit.Location;
import org.bukkit.Material;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by Yannick on 24.01.2017.
 */
public final class CompatCircuit implements Circuit {

    private HashMap<String, Object> circuit;


    public CompatCircuit(HashMap<String, Object> map) {
        this.circuit = map;
    }


    @Override
    public List<Material> getValidMaterials() {
        return null;
    }

    @Override
    public Map<String, Object> serialize() {
        return circuit;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public void addReceiver(Class<? extends AbstractReceiver> receiverClass, Location loc, int delay) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

    }

    @Override
    public void addReceiver(AbstractReceiver receiver) {

    }

    @Override
    public void addReceiver(Collection<AbstractReceiver> receivers) {

    }

    @Override
    public void addReceiver(AbstractReceiver... receivers) {

    }

    @Override
    public List<AbstractReceiver> getReceivers() {
        return null;
    }

    @Override
    public int getReceiversCount() {
        return 0;
    }

    @Override
    public List<CompatReceiver> getInValidReceivers() {
        return null;
    }

    @Override
    public int getWholeReceiverCount() {
        return 0;
    }

    @Override
    public int getInValidReceiversCount() {
        return 0;
    }

    @Override
    public void delReceiver(Receiver r) {

    }

    @Override
    public void delReceiver(Location location) {

    }

    @Override
    public boolean isReceiver(Location location) {
        return false;
    }

    @Override
    public UUID getOwner() {
        return null;
    }

    @Override
    public void setOwner(UUID playerUUID) {

    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public int getBlockCurrent() {
        return 0;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public void setLocation(Location location) {

    }

    @Override
    public int getDelay() {
        return 0;
    }

    @Override
    public void setDelay(int delay) {

    }

    @Override
    public void calculate(Receiver receiver, int oldCurrent, int newCurrent) {
    }

    @Override
    public void actvate(int oldCurrent, int newCurrent, int chain) {

    }
}
