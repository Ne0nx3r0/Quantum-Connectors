package com.ne0nx3r0.quantum;

import com.ne0nx3r0.quantum.api.QuantumConnectorsAPI;
import com.ne0nx3r0.quantum.api.circuit.AbstractCircuit;
import com.ne0nx3r0.quantum.api.receiver.AbstractReceiver;
import com.ne0nx3r0.quantum.impl.QuantumConnectorsAPIImplementation;
import com.ne0nx3r0.quantum.impl.QuantumConnectorsCommandExecutor;
import com.ne0nx3r0.quantum.impl.circuits.CircuitManager;
import com.ne0nx3r0.quantum.impl.extension.QuantumExtensionLoader;
import com.ne0nx3r0.quantum.impl.listeners.QuantumConnectorsBlockListener;
import com.ne0nx3r0.quantum.impl.listeners.QuantumConnectorsPlayerListener;
import com.ne0nx3r0.quantum.impl.listeners.QuantumConnectorsWorldListener;
import com.ne0nx3r0.quantum.impl.nmswrapper.ClassRegistry;
import com.ne0nx3r0.quantum.impl.receiver.base.Registry;
import com.ne0nx3r0.quantum.impl.utils.MessageLogger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QuantumConnectors extends JavaPlugin {

    // Configurables
    public static int MAX_CHAIN_LINKS = 3;
    public static int MAX_DELAY_TIME = 10;//in seconds
    public static int MAX_RECEIVERS_PER_CIRCUIT = 20;
    public static boolean VERBOSE_LOGGING = false;
    private static int AUTOSAVE_INTERVAL = 30;//specified here in minutes
    private static int AUTO_SAVE_ID = -1;


    public String apiVersion = ClassRegistry.instance.getApiVersion();
    private Registry<AbstractReceiver> receiverRegistry;
    private Registry<AbstractCircuit> circuitRegistry;
    private QuantumConnectorsAPIImplementation api;
    private Map<String, String> messages;
    private QuantumConnectorsWorldListener worldListener;
    private CircuitManager circuitManager;
    private QuantumConnectorsPlayerListener playerListener;
    private QuantumConnectorsBlockListener blockListener;
    private MessageLogger messageLogger;
    private boolean UPDATE_NOTIFICATIONS = false;
    private boolean updateAvailable = false;
    private String updateName;
    private Runnable autosaveCircuits = new Runnable() {
        @Override
        public void run() {
            circuitManager.getCircuitLoader().saveAllWorlds();
        }
    };

    private File extensionDir;
    private QuantumExtensionLoader loader;

    @Override
    public void onDisable() {
        if (circuitManager != null) {
            circuitManager.getCircuitLoader().saveAllWorlds();
        }

        this.loader.disable();
        this.api.unregisterAll();
    }

    @Override
    public void onEnable() {


        //This might be outdated...
        getDataFolder().mkdirs();

        //Load config options, localized messages
        setupConfig();

        this.messageLogger = new MessageLogger(this.getLogger(), messages);

        this.receiverRegistry = new Registry<>();
        this.circuitRegistry = new Registry<>();
        this.api = new QuantumConnectorsAPIImplementation(this.receiverRegistry, circuitRegistry);

        QuantumConnectorsAPI.setApi(this.api);

        this.extensionDir = new File(getDataFolder().getPath(), "extensions");

        this.extensionDir.mkdirs();

        this.loader = new QuantumExtensionLoader(api, messageLogger, this.extensionDir);

        this.loader.load();
        this.loader.enable();


        // TODO: 23.01.2017 should be loaded by extensionloader

        //Create a circuit manager
        this.circuitManager = new CircuitManager(messageLogger, this);

        this.worldListener = new QuantumConnectorsWorldListener(this.circuitManager.getCircuitLoader());
        this.blockListener = new QuantumConnectorsBlockListener(this, circuitManager, messageLogger);
        this.playerListener = new QuantumConnectorsPlayerListener(this, circuitManager, messageLogger, receiverRegistry);

        getCommand("qc").setExecutor(new QuantumConnectorsCommandExecutor(this, circuitManager, messageLogger));


        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(playerListener, this);
        pm.registerEvents(blockListener, this);
        pm.registerEvents(worldListener, this);


        AUTOSAVE_INTERVAL = AUTOSAVE_INTERVAL * 60 * 20;//convert to ~minutes

        AUTO_SAVE_ID = getServer().getScheduler().scheduleSyncRepeatingTask(
                this,
                autosaveCircuits,
                AUTOSAVE_INTERVAL,
                AUTOSAVE_INTERVAL);

    }

    private void setupConfig() {
        this.reloadConfig();

        FileConfiguration config = this.getConfig();
        config.options().copyDefaults(true);
        this.saveConfig();
        VERBOSE_LOGGING = config.getBoolean("verbose_logging", VERBOSE_LOGGING);
        MAX_CHAIN_LINKS = config.getInt("max_chain_links", MAX_CHAIN_LINKS);
        MAX_DELAY_TIME = config.getInt("max_delay_time", MAX_DELAY_TIME);
        MAX_RECEIVERS_PER_CIRCUIT = config.getInt("max_receivers_per_circuit", MAX_RECEIVERS_PER_CIRCUIT);
        AUTOSAVE_INTERVAL = config.getInt("autosave_interval_minutes", AUTOSAVE_INTERVAL);
        UPDATE_NOTIFICATIONS = config.getBoolean("update_notifications", UPDATE_NOTIFICATIONS);
        this.saveConfig();

        File messagesFile = new File(this.getDataFolder(), "messages.yml");

        messages = new HashMap<>();
        if (!messagesFile.exists()) {
            messagesFile.getParentFile().mkdirs();
            copy(this.getResource("messages.yml"), messagesFile);
        }

        FileConfiguration messagesYml = YamlConfiguration.loadConfiguration(messagesFile);

        Set<String> messageList = messagesYml.getKeys(false);

        for (String m : messageList) {
            messages.put(m, messagesYml.getString(m));
        }
    }

    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isUpdateAvailable() {
        return this.updateAvailable;
    }

    public String getUpdateName() {
        return updateName;
    }

}
