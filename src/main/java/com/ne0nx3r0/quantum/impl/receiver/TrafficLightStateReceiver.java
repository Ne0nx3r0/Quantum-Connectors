package com.ne0nx3r0.quantum.impl.receiver;

import com.ne0nx3r0.quantum.api.receiver.AbstractStateReceiver;
import com.ne0nx3r0.quantum.api.receiver.ReceiverNotValidException;
import com.ne0nx3r0.quantum.api.receiver.ReceiverState;
import com.ne0nx3r0.quantum.api.receiver.ValueNotChangedException;
import com.ne0nx3r0.quantum.impl.utils.VariantWrapper;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TrafficLightStateReceiver extends AbstractStateReceiver {

    public static final ReceiverState ON = ReceiverState.getByDyeColor(DyeColor.GREEN);
    public static final ReceiverState OF = ReceiverState.getByDyeColor(DyeColor.RED);

    /**
     * only use to getValidMaterials
     */
    protected TrafficLightStateReceiver() {
        super();
    }

    public TrafficLightStateReceiver(Location location) {
        this(location, 0);
    }

    public TrafficLightStateReceiver(Location location, Integer delay) {
        super(location, delay);
    }

    public TrafficLightStateReceiver(Map<String, Object> map) {
        super(map);
    }

    /**
     * default receivers return "qc:SimpleClassName"
     * please define your own prefix to differentiate between plugins registering own Receiver
     *
     * @return the namedType of the receiver
     */
    @Override
    public String getType() {
        return "qc:TrafficLightStateReceiver";
    }

    @Override
    public boolean isActive() {
        return getState() == ON;
    }

    @Override
    public void setActive(boolean powerOn) {
        try {
            super.setActive(powerOn);
        } catch (ReceiverNotValidException | ValueNotChangedException e) {
            return;
        }
        setState(powerOn ? ON : OF);
    }

    @Override
    public ReceiverState getState() {
        return VariantWrapper.getState(location.getBlock());
    }

    @Override
    public void setState(ReceiverState state) {
        VariantWrapper.setState(location.getBlock(), state);
    }

    @Override
    public List<Material> getValidMaterials() {
        return Arrays.asList(Material.WOOL, Material.WOOD);
    }

    @Override
    public Map<String, Object> serialize() {
        return super.serialize();
    }
}
