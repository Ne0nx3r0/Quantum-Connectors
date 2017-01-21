package com.ne0nx3r0.quantum.receiver;

import com.ne0nx3r0.quantum.nmswrapper.QSWorld;
import com.ne0nx3r0.quantum.receiver.base.AbstractReceiver;
import com.ne0nx3r0.quantum.utils.ValidMaterials;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

import static com.ne0nx3r0.quantum.circuits.CircuitManager.keepAlives;


public class RedstoneLampReceiver extends AbstractReceiver {


    private QSWorld qsWorld = QSWorld.instance;

    /**
     * only use to getValidMaterials
     */
    public RedstoneLampReceiver() {
        super();
    }

    public RedstoneLampReceiver(Location location) {
        super(location);
    }

    public RedstoneLampReceiver(Location location, Integer delay) {
        super(location, delay);
    }

    public RedstoneLampReceiver(Map<String, Object> map) {
        super(map);

        if (isActive())
            keepAlives.add(location.getBlock());
    }

    @Override
    public List<Material> getValidMaterials() {
        return ValidMaterials.LAMP;
    }

    @Override
    public String getType() {
        return "qc:" + getClass().getSimpleName();
    }

    @Override
    public boolean isActive() {
        return this.location.getBlock().getType() == Material.REDSTONE_LAMP_ON;
    }

    @Override
    public void setActive(boolean powerOn) {
        if (!isValid()) return;
        if (isActive() == powerOn) return;


            if (isActive()) {
                if (!powerOn) {
                    keepAlives.remove(location.getBlock());
                    this.getLocation().getBlock().setType(Material.REDSTONE_LAMP_OFF);
                }
            } else if (!isActive()) {
                if (powerOn) {
                    keepAlives.add(location.getBlock());
                    this.qsWorld.setStatic(location.getWorld(), true);
                    location.getBlock().setType(Material.REDSTONE_LAMP_ON);
                    this.qsWorld.setStatic(location.getWorld(), false);
                }
            }
    }

    @Override
    public boolean isValid() {
        return this.location.getBlock().getType() == Material.REDSTONE_LAMP_ON || this.location.getBlock().getType() == Material.REDSTONE_LAMP_OFF;
    }


}
