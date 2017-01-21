package com.ne0nx3r0.quantum.receiver;

import com.ne0nx3r0.quantum.receiver.base.AbstractReceiver;
import com.ne0nx3r0.quantum.utils.ValidMaterials;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.material.Lever;

import java.util.List;
import java.util.Map;

/**
 * Created by ysl3000 on 14.01.17.
 */
@SerializableAs("LeverReceiver")
public class LeverReceiver extends AbstractReceiver {
    /**
     * only use to getValidMaterials
     */
    public LeverReceiver() {
        super();
    }

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
        BlockState state = location.getBlock().getState();
        Lever lever = (Lever) state.getData();
        lever.setPowered(powerOn);
        state.setData(lever);
        state.update();
    }

    @Override
    public boolean isValid() {
        return location.getBlock().getState().getData() instanceof Lever;
    }

}
