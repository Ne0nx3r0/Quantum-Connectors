package com.ne0nx3r0.quantum.receiver;

import com.ne0nx3r0.quantum.circuits.CircuitTypes;
import org.bukkit.Location;

/**
 * Created by ysl3000 on 19.01.17.
 */
public abstract class AbstractReceiver implements Receiver {

    protected Location location;
    protected CircuitTypes circuitTypes;
    protected long delay;


    public AbstractReceiver(Location location, int type) {
        this(location, type, 0);
    }

    public AbstractReceiver(Location location, int type, int delay) {
        this(location, CircuitTypes.getByID(type), delay);
    }

    public AbstractReceiver(Location location, CircuitTypes circuitTypes, long delay) {
        this.location = location;
        this.circuitTypes = circuitTypes;
        this.delay = delay;
    }


    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public CircuitTypes getCircuitType() {
        return this.circuitTypes;
    }

    @Override
    public long getDelay() {
        return delay;
    }

    @Deprecated
    public int getBlockMaterial() {
        return location.getBlock().getTypeId();
    }

    @Deprecated
    public byte getBlockData() {
        return location.getBlock().getData();
    }
}
