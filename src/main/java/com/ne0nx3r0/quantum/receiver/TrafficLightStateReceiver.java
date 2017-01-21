package com.ne0nx3r0.quantum.receiver;

import com.ne0nx3r0.quantum.receiver.base.AbstractStateReceiver;
import com.ne0nx3r0.quantum.receiver.base.ReceiverState;
import com.ne0nx3r0.quantum.utils.BlockVariant;
import com.ne0nx3r0.quantum.utils.VariantWrapper;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Yannick on 21.01.2017.
 */
public class TrafficLightStateReceiver extends AbstractStateReceiver {

    public static final ReceiverState ON = ReceiverState.getByColor(DyeColor.GREEN);
    public static final ReceiverState OF = ReceiverState.getByColor(DyeColor.RED);


    private BlockVariant blockVariant;

    /**
     * only use to getValidMaterials
     */
    public TrafficLightStateReceiver() {
        super();
    }

    public TrafficLightStateReceiver(Location location) {
        this(location, 0);
    }

    public TrafficLightStateReceiver(Location location, Integer delay) {
        super(location, delay);
        this.blockVariant = VariantWrapper.getWrapperFromBlock(location.getBlock());
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
        setState(powerOn ? ON : OF);
    }

    @Override
    public boolean isValid() {
        return location.getBlock().getType() == Material.WOOL;
    }

    @Override
    public ReceiverState getState() {
        return blockVariant.getState();
    }

    @Override
    public void setState(ReceiverState state) {
        blockVariant.setState(state);
    }

    @Override
    public List<Material> getValidMaterials() {
        return Arrays.asList(Material.WOOL);
    }

    @Override
    public Map<String, Object> serialize() {
        return super.serialize();
    }
}
