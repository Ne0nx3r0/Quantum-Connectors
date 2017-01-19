package com.ne0nx3r0.quantum.api;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("Receiver")
public interface Receiver extends ConfigurationSerializable {

    Location getLocation();

    /**
     * default receivers return "qc:SimpleClassName"
     * please define your own prefix to differentiate between plugins registering own Receiver
     *
     * @return the namedType of the receiver
     */
    String getType();

    long getDelay();

    boolean isActive();

    void setActive(boolean powerOn);

    boolean isValid();


}
