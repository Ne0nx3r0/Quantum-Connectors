package com.ne0nx3r0.quantum.receiver;

import com.ne0nx3r0.quantum.nmswrapper.QSWorld;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.List;


@SerializableAs("RedstoneLampReceiver")
public class RedstoneLampReceiver extends AbstractReceiver {


    private List<Block> keepAlives;
    private QSWorld qsWorld;

    public RedstoneLampReceiver(Location location, long delay, List<Block> keepAlives, QSWorld qsWorld) {
        super(location, delay);
        this.keepAlives = keepAlives;
        this.qsWorld = qsWorld;
    }

    public RedstoneLampReceiver(Location location, long delay) {
        super(location, delay);
    }

    public RedstoneLampReceiver(Location location) {
        super(location);
    }

    @Override
    public String getType() {
        return "qc:" + getClass().getSimpleName();
    }

    @Override
    public boolean isActive() {
        return isValid() && this.location.getBlock().getType() == Material.REDSTONE_LAMP_ON;
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
