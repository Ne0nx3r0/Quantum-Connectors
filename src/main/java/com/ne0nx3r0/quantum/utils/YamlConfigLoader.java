package com.ne0nx3r0.quantum.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by ysl3000 on 14.01.17.
 */
public abstract class YamlConfigLoader {
    protected YamlConfiguration config;
    private File file;

    public YamlConfigLoader(final String path, final String file) {
        new File("./plugins/" + path + "/").mkdir();
        this.file = new File("./plugins/" + path + "/" + file);
        this.loadConfig();
    }

    public final void loadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public void addDefault(final String path, final Object value) {
        if (!this.config.contains(path)) {
            this.config.set(path, value);
            this.saveConfig();
        }
    }

    public final boolean saveConfig() {
        try {
            this.config.save(this.file);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }


}
