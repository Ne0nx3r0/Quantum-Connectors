package com.ne0nx3r0.quantum.receiver;

import com.ne0nx3r0.quantum.utils.ValidMaterials;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.material.Door;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

import java.util.List;
import java.util.Map;

@SerializableAs("OpenableReceiver")
public class OpenableReceiver extends AbstractReceiver {
    public OpenableReceiver(Location location) {
        super(location);
    }

    public OpenableReceiver(Map<String, Object> map) {
        super(map);
    }

    public OpenableReceiver(Location location, long delay) {
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

        BlockState state = location.getBlock().getState();
        MaterialData data = state.getData();
        ((Openable) data).setOpen(powerOn);
        state.setData(data);
        state.update();
        location.getWorld().playEffect(location, Effect.DOOR_TOGGLE, 0, 10);
    }

    @Override
    public boolean isValid() {
        return location.getBlock().getState().getData() instanceof Openable;
    }

    @Override
    public void calculateRealLocation() {
        MaterialData materialData = location.getBlock().getState().getData();
        if (materialData instanceof Door) {
            Door door = (Door) materialData;

            if (door.isTopHalf()) {
                this.location = location.add(0, -1, 0);
            }
        }
    }

    @Override
    public List<Material> getValidMaterials() {
        return ValidMaterials.OPENABLE;
    }
}
