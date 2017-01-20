package com.ne0nx3r0.quantum.receiver;

import com.ne0nx3r0.quantum.nmswrapper.QSWorld;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.Map;

import static com.ne0nx3r0.quantum.circuits.CircuitManager.keepAlives;


@SerializableAs("RedstoneLampReceiver")
public class RedstoneLampReceiver extends AbstractReceiver {


    private QSWorld qsWorld;

    public RedstoneLampReceiver(Location location, long delay, QSWorld qsWorld) {
        super(location, delay);
        this.qsWorld = qsWorld;
    }

    public RedstoneLampReceiver(Map<String, Object> map) {
        super(map);
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

        if (isValid()) {

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
    }

    @Override
    public boolean isValid() {
        return this.location.getBlock().getType() == Material.REDSTONE_LAMP_ON || this.location.getBlock().getType() == Material.REDSTONE_LAMP_OFF;
    }


}
