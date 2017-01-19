package com.ne0nx3r0.quantum;

import com.ne0nx3r0.quantum.circuits.CircuitManager;
import com.ne0nx3r0.quantum.utils.MessageLogger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigConverter {


    private QuantumConnectors plugin;
    private MessageLogger messageLogger;

    public ConfigConverter(QuantumConnectors plugin, MessageLogger messageLogger) {
        this.plugin = plugin;
        this.messageLogger = messageLogger;
    }


    //1.2.3 circuits.yml Converter
    public void convertOldCircuitsYml() {
        File oldYmlFile = new File(plugin.getDataFolder(), "circuits.yml");
        if (oldYmlFile.exists()) {
            messageLogger.log(messageLogger.getMessage("found_old_file").replace("%file%", oldYmlFile.getName()));

            FileConfiguration oldYml = YamlConfiguration.loadConfiguration(oldYmlFile);

            for (String worldName : oldYml.getValues(false).keySet()) {
                ArrayList<Map<String, Object>> tempCircuitObjs = new ArrayList<Map<String, Object>>();

                for (int x = 0; ; x++) {
                    String path = worldName + ".circuit_" + x;

                    if (oldYml.get(path) == null) {
                        break;
                    }

                    Map<String, Object> tempCircuitObj = new HashMap<String, Object>();

                    String[] senderXYZ = oldYml.get(path + ".sender").toString().split(",");

                    tempCircuitObj.put("x", Integer.parseInt(senderXYZ[0]));
                    tempCircuitObj.put("y", Integer.parseInt(senderXYZ[1]));
                    tempCircuitObj.put("z", Integer.parseInt(senderXYZ[2]));

                    //they'll all be the same, should only ever be one anyway
                    String receiversType = oldYml.get(path + ".type").toString();

                    ArrayList<Map<String, Object>> tempReceiverObjs = new ArrayList<Map<String, Object>>();
                    for (Object receiver : oldYml.getList(path + ".receivers")) {
                        Map<String, Object> tempReceiverObj = new HashMap<String, Object>();

                        String[] sReceiverLoc = receiver.toString().split(",");

                        tempReceiverObj.put("x", Integer.parseInt(sReceiverLoc[0]));
                        tempReceiverObj.put("y", Integer.parseInt(sReceiverLoc[1]));
                        tempReceiverObj.put("z", Integer.parseInt(sReceiverLoc[2]));
                        tempReceiverObj.put("d", 0);
                        tempReceiverObj.put("t", Integer.parseInt(receiversType));

                        tempReceiverObjs.add(tempReceiverObj);
                    }

                    tempCircuitObj.put("r", tempReceiverObjs);

                    tempCircuitObjs.add(tempCircuitObj);
                }

                File newYmlFile = new File(plugin.getDataFolder(), worldName + ".circuits.yml");
                FileConfiguration newYml = YamlConfiguration.loadConfiguration(newYmlFile);

                newYml.set("fileVersion", 2);
                newYml.set("circuits", tempCircuitObjs);

                try {
                    newYml.save(newYmlFile);
                } catch (IOException ex) {
                    messageLogger.error(messageLogger.getMessage("unable_to_save").replace("%file%", newYmlFile.getName()));

                    Logger.getLogger(CircuitManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            File testFile = new File(plugin.getDataFolder(), "circuits.yml.bak");
            new File(plugin.getDataFolder(), "circuits.yml").renameTo(testFile);
        }
    }

}
