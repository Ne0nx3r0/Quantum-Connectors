package com.ne0nx3r0.quantum.receiver;

import com.ne0nx3r0.quantum.nmswrapper.QSWorld;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.material.MaterialData;
import org.bukkit.material.PistonBaseMaterial;

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

        if (isValid()) {
            if (isActive()) {
                if (!powerOn) {
                    keepAlives.remove(location.getBlock());

                    ((PistonBaseMaterial) data).setPowered(!powerOn);
                    state.setData(data);
                    state.update();
                }

            } else {
                if (powerOn) {
                    keepAlives.add(location.getBlock());
                    QSWorld.instance.setStatic(location.getWorld(), true);
                    ((PistonBaseMaterial) data).setPowered(powerOn);
                    state.setData(data);
                    state.update();
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
