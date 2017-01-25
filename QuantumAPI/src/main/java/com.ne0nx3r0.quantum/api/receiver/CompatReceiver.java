package com.ne0nx3r0.quantum.api.receiver;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yannick on 24.01.2017.
 */
public final class CompatReceiver implements ConfigurationSerializable {

    private HashMap<String, Object> receiver;


    public CompatReceiver(HashMap<String, Object> map) {
        this.receiver = map;
    }

    @Override
    public Map<String, Object> serialize() {
        return receiver;
    }
}
