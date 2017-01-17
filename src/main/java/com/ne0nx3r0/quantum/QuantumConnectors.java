package com.ne0nx3r0.quantum;

import com.ne0nx3r0.quantum.circuits.CircuitManager;
import com.ne0nx3r0.quantum.listeners.QuantumConnectorsBlockListener;
import com.ne0nx3r0.quantum.listeners.QuantumConnectorsPlayerListener;
import com.ne0nx3r0.quantum.listeners.QuantumConnectorsWorldListener;
import com.ne0nx3r0.quantum.nmswrapper.ClassRegistry;
import com.ne0nx3r0.quantum.nmswrapper.QSWorld;
import com.ne0nx3r0.quantum.utils.MessageLogger;
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
    // Localized Messages
    private Map<String, String> messages;
    private QuantumConnectorsWorldListener worldListener;
    // Circuit Manager
    private CircuitManager circuitManager;
    // Register events
    private QuantumConnectorsPlayerListener playerListener;
    private QuantumConnectorsBlockListener blockListener;
    private MessageLogger messageLogger;


    private boolean UPDATE_NOTIFICATIONS = false;
    // Updater
    private boolean updateAvailable = false;
    private String updateName;

    // Version
    public String apiVersion;
    //private boolean outdated = false;
    //Scheduled save mechanism
    private Runnable autosaveCircuits = new Runnable() {
        @Override
        public void run() {
            circuitManager.getCircuitLoader().saveAllWorlds();
        }
    };

    private QSWorld qsWorld;
    private ClassRegistry classRegistry;


    @Override
    public void onDisable() {
        if (circuitManager != null) {
            circuitManager.getCircuitLoader().saveAllWorlds();

        }
    }

    @Override
    public void onEnable() {
        // TODO: 14.01.17 move to Configloader
        //This might be outdated...
        getDataFolder().mkdirs();

        // TODO: 14.01.17 move to Configloader

        //Load config options, localized messages
        setupConfig();


        String packageName = getServer().getClass().getPackage().getName();
        this.apiVersion = packageName.substring(packageName.lastIndexOf('.') + 1);
        try {
            this.classRegistry = new ClassRegistry(apiVersion);
        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        this.qsWorld = new QSWorld(this.classRegistry);

        this.messageLogger = new MessageLogger(this.getLogger(), messages);
        //Create a circuit manager
        this.circuitManager = new CircuitManager(messageLogger, this, qsWorld);

        this.worldListener = new QuantumConnectorsWorldListener(this.circuitManager.getCircuitLoader());
        this.blockListener = new QuantumConnectorsBlockListener(this, circuitManager);
        this.playerListener = new QuantumConnectorsPlayerListener(this, circuitManager, messageLogger);

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


        messages = new HashMap<>();

        File messagesFile = new File(this.getDataFolder(), "messages.yml");

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
