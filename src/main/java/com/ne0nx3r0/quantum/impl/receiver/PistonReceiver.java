package com.ne0nx3r0.quantum.impl.receiver;

import com.ne0nx3r0.quantum.api.receiver.AbstractKeepAliveReceiver;
import com.ne0nx3r0.quantum.impl.nmswrapper.QSWorld;
import com.ne0nx3r0.quantum.impl.utils.ValidMaterials;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;
import org.bukkit.material.PistonBaseMaterial;

import java.util.List;
import java.util.Map;


public class PistonReceiver extends AbstractKeepAliveReceiver {

    /**
     * only use to getValidMaterials
     */
    public PistonReceiver() {
        super();
    }

    public PistonReceiver(Location location) {
        super(location);
    }

    public PistonReceiver(Map<String, Object> map) {
        super(map);
    }

    public PistonReceiver(Location location, Integer delay) {
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


        if (!isValid()) return;
        if (isActive() == powerOn) return;


        BlockState state = location.getBlock().getState();
        MaterialData data = state.getData();


        PistonBaseMaterial pistonBaseMaterial = (PistonBaseMaterial) data;

        Block behindPiston = location.getBlock().getRelative(pistonBaseMaterial.getFacing().getOppositeFace());

        MaterialData tempData = behindPiston.getState().getData();


        if (isActive() && !powerOn) {
            keepAlives.remove(location.getBlock());
            location.getBlock().getState().update(true);
        } else {
            behindPiston.getState().setData(new MaterialData(Material.REDSTONE_BLOCK));
            keepAlives.add(location.getBlock());
            QSWorld.instance.setStatic(location.getWorld(), true);
            behindPiston.getState().setData(tempData);
            QSWorld.instance.setStatic(location.getWorld(), false);
        }
    }

    @Override
    public boolean isValid() {
        return location.getBlock().getState().getData() instanceof PistonBaseMaterial;
    }
}
