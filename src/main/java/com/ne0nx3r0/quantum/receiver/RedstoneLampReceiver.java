package com.ne0nx3r0.quantum.receiver;

import com.ne0nx3r0.quantum.nmswrapper.QSWorld;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;


public class RedstoneLampReceiver extends com.ne0nx3r0.quantum.receiver.Receiver {


    private List<Block> keepAlives;
    private QSWorld qsWorld;

    public RedstoneLampReceiver(Location location, int type, int delay, List<Block> keepAlives, QSWorld qsWorld) {
        super(location, type, delay);
        this.keepAlives = keepAlives;
        this.qsWorld = qsWorld;
    }

    public RedstoneLampReceiver(Location location, int type) {
        super(location, type);
    }

    @Override
    public void setActive(boolean powerOn) {
        if (this.location.getBlock().getType() == Material.REDSTONE_LAMP_ON) {
            if (!powerOn) {

                keepAlives.remove(location.getBlock());
                this.getLocation().getBlock().setType(Material.REDSTONE_LAMP_OFF);
            }
        } else if (this.getLocation().getBlock().getType() == Material.REDSTONE_LAMP_OFF) {
            if (powerOn) {

                keepAlives.add(location.getBlock());
                this.qsWorld.setStatic(location.getWorld(), true);
                location.getBlock().setType(Material.REDSTONE_LAMP_ON);
                this.qsWorld.setStatic(location.getWorld(), false);
            }
        }


    }


}
