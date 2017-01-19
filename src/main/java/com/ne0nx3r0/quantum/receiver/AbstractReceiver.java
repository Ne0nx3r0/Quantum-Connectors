package com.ne0nx3r0.quantum.receiver;

import com.ne0nx3r0.quantum.api.Receiver;
import org.bukkit.Location;

/**
 * Created by ysl3000 on 19.01.17.
 */
public abstract class AbstractReceiver implements Receiver {

    protected Location location;
    protected long delay;


    public AbstractReceiver(Location location) {
        this(location, 0);
    }

    public AbstractReceiver(Location location, long delay) {
        this.location = location;
        this.delay = delay;
    }

    @Override
    public Location getLocation() {
        return location;
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
