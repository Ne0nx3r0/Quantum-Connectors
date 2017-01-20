package com.ne0nx3r0.quantum.receiver;

import com.ne0nx3r0.quantum.nmswrapper.QSWorld;
import com.ne0nx3r0.quantum.utils.ValidMaterials;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.material.MaterialData;
import org.bukkit.material.PistonBaseMaterial;

import java.util.List;
import java.util.Map;

import static com.ne0nx3r0.quantum.circuits.CircuitManager.keepAlives;

@SerializableAs("PistonReceiver")
public class PistonReceiver extends AbstractReceiver {
    public PistonReceiver(Location location) {
        super(location);
    }

    public PistonReceiver(Map<String, Object> map) {
        super(map);
    }

    public PistonReceiver(Location location, long delay) {
        super(location, delay);
    }

    @Override
    public List<Material> getValidMaterials() {
        return ValidMaterials.PISTON;
    }

    @Override
    public String getType() {
        return "qc:" + getClass().getSimpleName();
    }

    @Override
    public boolean isActive() {
        return ((PistonBaseMaterial) location.getBlock().getState().getData()).isPowered();
    }

    @Override
    public void setActive(boolean powerOn) {

        BlockState state = location.getBlock().getState();
        MaterialData data = state.getData();


        PistonBaseMaterial pistonBaseMaterial = (PistonBaseMaterial) data;

        Block behindPiston = location.getBlock().getRelative(pistonBaseMaterial.getFacing().getOppositeFace());

        MaterialData tempData = behindPiston.getState().getData();


        if (isValid()) {
            if (isActive()) {
                if (!powerOn) {
                    keepAlives.remove(location.getBlock());
                    location.getBlock().getState().update(true);
                }

            } else {
                if (powerOn) {
                    behindPiston.getState().setData(new MaterialData(Material.REDSTONE_BLOCK));
                    keepAlives.add(location.getBlock());
                    QSWorld.instance.setStatic(location.getWorld(), true);
                    behindPiston.getState().setData(tempData);
                    QSWorld.instance.setStatic(location.getWorld(), false);

                }
            }
        }
    }

    @Override
    public boolean isValid() {
        return location.getBlock().getState().getData() instanceof PistonBaseMaterial;
    }
}
