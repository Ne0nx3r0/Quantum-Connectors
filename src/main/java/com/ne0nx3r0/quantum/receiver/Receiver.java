package com.ne0nx3r0.quantum.receiver;

import com.ne0nx3r0.quantum.circuits.CircuitTypes;
import org.bukkit.Location;

public interface Receiver {

    Location getLocation();

    /**
     * default receivers return "qc:SimpleClassName"
     * please define your own prefix to differentiate between plugins registering own Receiver
     *
     * @return the namedType of the receiver
     */
    String getType();

    CircuitTypes getCircuitType();

    long getDelay();

    @Deprecated
    int getBlockMaterial();

    @Deprecated
    byte getBlockData();

    boolean isActive();

    void setActive(boolean powerOn);

    boolean isValid();


}
