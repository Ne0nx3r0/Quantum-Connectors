package com.ne0nx3r0.quantum.impl.receiver;

import com.ne0nx3r0.quantum.api.receiver.AbstractKeepAliveReceiver;
import com.ne0nx3r0.quantum.api.receiver.ReceiverNotValidException;
import com.ne0nx3r0.quantum.api.receiver.ValueNotChangedException;
import com.ne0nx3r0.quantum.impl.nmswrapper.QSWorld;
import com.ne0nx3r0.quantum.impl.utils.ValidMaterials;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;


public class RedstoneLampReceiver extends AbstractKeepAliveReceiver {


    private QSWorld qsWorld = QSWorld.instance;

    public RedstoneLampReceiver(Location location) {
        super(location);
    }

    public RedstoneLampReceiver(Location location, Integer delay) {
        super(location, delay);
    }

    public RedstoneLampReceiver(Map<String, Object> map) {
        super(map);
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
        try {
            super.setActive(powerOn);
        } catch (ValueNotChangedException | ReceiverNotValidException e) {
            return;
        }

        if (powerOn) {
            this.qsWorld.setStatic(location.getWorld(), true);
            location.getBlock().setType(Material.REDSTONE_LAMP_ON);
            this.qsWorld.setStatic(location.getWorld(), false);
        } else {
            this.getLocation().getBlock().setType(Material.REDSTONE_LAMP_OFF);
        }
    }


    @Override
    public boolean isValid() {
        return this.location.getBlock().getType() == Material.REDSTONE_LAMP_ON || this.location.getBlock().getType() == Material.REDSTONE_LAMP_OFF;
    }


}
