package com.ne0nx3r0.quantum.impl.receiver;

import com.ne0nx3r0.quantum.api.receiver.AbstractReceiver;
import com.ne0nx3r0.quantum.api.receiver.ReceiverNotValidException;
import com.ne0nx3r0.quantum.api.receiver.ValueNotChangedException;
import com.ne0nx3r0.quantum.impl.utils.SourceBlockUtil;
import com.ne0nx3r0.quantum.impl.utils.ValidMaterials;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

import java.util.List;
import java.util.Map;

public class OpenableReceiver extends AbstractReceiver {
    /**
     * only use to getValidMaterials
     */
    public OpenableReceiver() {
        super();
    }

    public OpenableReceiver(Location location) {
        super(location);
    }

    public OpenableReceiver(Map<String, Object> map) {
        super(map);
    }

    public OpenableReceiver(Location location, Integer delay) {
        super(location, delay);
    }

    @Override
    public String getType() {
        return "qc:" + getClass().getSimpleName();
    }

    @Override
    public boolean isActive() {
        return ((Openable) location.getBlock().getState().getData()).isOpen();
    }

    @Override
    public void setActive(boolean powerOn) {

        try {
            super.setActive(powerOn);
        } catch (ReceiverNotValidException | ValueNotChangedException e) {
            return;
        }

        BlockState state = location.getBlock().getState();
        MaterialData data = state.getData();
        ((Openable) data).setOpen(powerOn);
        state.setData(data);
        state.update();
        location.getWorld().playEffect(location, Effect.DOOR_TOGGLE, 0, 10);
    }

    @Override
    public void calculateRealLocation() {
        this.location = SourceBlockUtil.getSourceBlock(location);
    }

    @Override
    public List<Material> getValidMaterials() {
        return ValidMaterials.OPENABLE;
    }
}
