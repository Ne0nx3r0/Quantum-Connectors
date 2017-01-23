package com.ne0nx3r0.quantum.api.receiver;

import org.bukkit.Location;

import java.util.Map;

public abstract class AbstractStateReceiver extends AbstractReceiver implements StateReceiver {

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

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        map.put("receiverstate", getState().name());
        return map;
    }
}
