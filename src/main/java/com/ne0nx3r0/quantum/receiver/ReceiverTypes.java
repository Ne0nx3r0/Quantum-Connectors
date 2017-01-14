package com.ne0nx3r0.quantum.receiver;

import com.ne0nx3r0.quantum.ValidMaterials;
import com.ne0nx3r0.quantum.nmswrapper.QSWorld;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

/**
 * Created by ysl3000 on 14.01.17.
 */
public class ReceiverTypes {


    private QSWorld qsWorld;
    private List<Location> keepAlive;

    public ReceiverTypes(QSWorld qsWorld, List<Location> keepAlive) {
        this.qsWorld = qsWorld;
        this.keepAlive = keepAlive;
    }


    public com.ne0nx3r0.quantum.receiver.Receiver fromType(Location location, int type, int delay) {
        Material m = location.getBlock().getType();

        if (ValidMaterials.LAMP.contains(m)) {
            return new RedstoneLampReceiver(location, type, delay, keepAlive, qsWorld);
        } else if (ValidMaterials.OPENABLE.contains(m)) {
            return new OpenableReceiver(location, type, delay);
        } else if (ValidMaterials.LEVER.contains(m)) {
            return new LeverReceiver(location, type, delay);
        } else if (ValidMaterials.RAIL.contains(m)) {
            return new PoweredRailReceiver(location, type, delay);
        } else if (ValidMaterials.PISTON.contains(m)) {
            return new PistonReceiver(location, type, delay);
        }
        return null;

    }

}
