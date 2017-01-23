package com.ne0nx3r0.quantum.impl.receiver;

import com.ne0nx3r0.quantum.api.receiver.AbstractReceiver;
import com.ne0nx3r0.quantum.api.receiver.ReceiverNotValidException;
import com.ne0nx3r0.quantum.api.receiver.ValueNotChangedException;
import com.ne0nx3r0.quantum.impl.utils.ValidMaterials;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.material.Lever;

import java.util.List;
import java.util.Map;

public class LeverReceiver extends AbstractReceiver {

    public LeverReceiver(Location location) {
        this(location, 0);
    }

    public LeverReceiver(Location location, Integer delay) {
        super(location, delay);
    }

    public LeverReceiver(Map<String, Object> map) {
        super(map);
    }

    @Override
    public List<Material> getValidMaterials() {
        return ValidMaterials.LEVER;
    }

    /**
     * default receivers return "qc:LeverReceiver"
     *
     * @return the namedType of the receiver
     */
    @Override
    public String getType() {
        return "qc:" + getClass().getSimpleName();
    }

    @Override
    public boolean isActive() {
        return ((Lever) location.getBlock().getState().getData()).isPowered();
    }

    @Override
    public void setActive(boolean powerOn) {
        try {
            super.setActive(powerOn);
        } catch (ReceiverNotValidException | ValueNotChangedException e) {
            return;
        }
        BlockState state = location.getBlock().getState();
        Lever lever = (Lever) state.getData();
        lever.setPowered(powerOn);
        state.setData(lever);
        state.update();
    }
}
