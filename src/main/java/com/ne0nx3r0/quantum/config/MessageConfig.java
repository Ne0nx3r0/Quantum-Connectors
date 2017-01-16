package com.ne0nx3r0.quantum.config;

import com.ne0nx3r0.quantum.utils.YamlConfigLoader;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by ysl3000 on 14.01.17.
 */
public class MessageConfig extends YamlConfigLoader {


    public MessageConfig(JavaPlugin plugin) {
        super(plugin.getDataFolder().getPath(), "messages.yml");
    }

    public String getMessageFromKey(String key) {
        return config.getString(key);
    }
}
