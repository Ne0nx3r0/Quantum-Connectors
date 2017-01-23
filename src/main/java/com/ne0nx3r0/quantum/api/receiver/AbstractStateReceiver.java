package com.ne0nx3r0.quantum.api.receiver;

import org.bukkit.Location;

import java.util.Map;

/**
 * Created by Yannick on 21.01.2017.
 */
public abstract class AbstractStateReceiver extends AbstractReceiver {

    /**
     * only use to getValidMaterials
     */
    public AbstractStateReceiver() {
        super();
    }

    public AbstractStateReceiver(Location location) {
        super(location);
    }

    public AbstractStateReceiver(Location location, Integer delay) {
        super(location, delay);
    }

    public AbstractStateReceiver(Map<String, Object> map) {
        super(map);
        ReceiverState state = ReceiverState.getByName((String) map.get("receiverstate"));
        if (state != null) {
            setState(state);
        }
    }

    public abstract ReceiverState getState();

    public abstract void setState(ReceiverState state);

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("receiverstate", getState().name());
        return map;
    }
}
