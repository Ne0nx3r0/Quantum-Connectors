package com.ne0nx3r0.quantum;

import com.ne0nx3r0.quantum.circuits.CircuitManager;
import com.ne0nx3r0.quantum.listeners.QuantumConnectorsBlockListener;
import com.ne0nx3r0.quantum.listeners.QuantumConnectorsPlayerListener;
import com.ne0nx3r0.quantum.listeners.QuantumConnectorsWorldListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class QuantumConnectors extends JavaPlugin{    

// Register events
    private final QuantumConnectorsPlayerListener playerListener = new QuantumConnectorsPlayerListener(this);
    private final QuantumConnectorsBlockListener blockListener = new QuantumConnectorsBlockListener(this);
    private final QuantumConnectorsWorldListener worldListener = new QuantumConnectorsWorldListener(this);

// Circuit Manager
    public static CircuitManager circuitManager;
    
// Configurables
    public static int MAX_CHAIN_LINKS = 3;
    public static int MAX_DELAY_TIME = 10;//in seconds
    public static int MAX_RECEIVERS_PER_CIRCUIT = 20;
    public static boolean VERBOSE_LOGGING = false;
    private boolean UPDATE_NOTIFICATIONS = false;

    private static int AUTOSAVE_INTERVAL = 30;//specified here in minutes
    private static int AUTO_SAVE_ID = -1;
    
// Localized Messages
    private static Map<String,String> messages;
    
// Updater
    private boolean updateAvailable = false;
    private String updateName;

// Version
    private String apiVersion;
    private String apiSupportedVersion = "v1_6_R2";
    private boolean outdated = false;
    
    @Override
    public void onDisable()
    {
        if(circuitManager != null)
        {
            circuitManager.saveAllWorlds();
        }
    }
    
    @Override
    public void onEnable()
    {

    //This might be outdated...
        getDataFolder().mkdirs();

    //Load config options, localized messages
        setupConfig();

    //Create a circuit manager
        circuitManager = new CircuitManager(this);

    //Register qc command
        getCommand("qc").setExecutor(new QuantumConnectorsCommandExecutor(this));   

    //Register listeners
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(playerListener, this);
        pm.registerEvents(blockListener, this);
        pm.registerEvents(worldListener, this);

    //Schedule saves
        AUTOSAVE_INTERVAL = AUTOSAVE_INTERVAL * 60 * 20;//convert to ~minutes

        AUTO_SAVE_ID = getServer().getScheduler().scheduleSyncRepeatingTask(
            this,
            autosaveCircuits,
            AUTOSAVE_INTERVAL,
            AUTOSAVE_INTERVAL);
        
        String packageName = getServer().getClass().getPackage().getName();
        this.apiVersion = packageName.substring(packageName.lastIndexOf('.') + 1);
    }	
    
    public void msg(Player player, String sMessage) {
        player.sendMessage(ChatColor.LIGHT_PURPLE + "[QC] " + ChatColor.WHITE + sMessage);
    }

//Generic wrappers for console messages
    public void log(Level level,String sMessage){
        if(!sMessage.equals(""))
            getLogger().log(level,sMessage);
    }
    public void log(String sMessage){
        log(Level.INFO,sMessage);
    }
    public void error(String sMessage){
        log(Level.WARNING,sMessage);
    }
    
//Wrapper for getting localized messages
    public String getMessage(String sMessageName){
        return messages.get(sMessageName);
    }
    
    //Scheduled save mechanism
    private Runnable autosaveCircuits = new Runnable(){
        @Override
        public void run() {
            circuitManager.saveAllWorlds();
        }
    };

    private void setupConfig(){
        this.reloadConfig();
        
        File configFile = new File(this.getDataFolder(), "config.yml");
        
        if(!configFile.exists()){
            copy(this.getResource("config.yml"),configFile);
        }
        
        FileConfiguration config = this.getConfig();
        
        VERBOSE_LOGGING           = config.getBoolean("verbose_logging",VERBOSE_LOGGING);
        MAX_CHAIN_LINKS           = config.getInt("max_chain_links", MAX_CHAIN_LINKS);
        MAX_DELAY_TIME            = config.getInt("max_delay_time", MAX_DELAY_TIME);
        MAX_RECEIVERS_PER_CIRCUIT = config.getInt("max_receivers_per_circuit", MAX_RECEIVERS_PER_CIRCUIT);
        AUTOSAVE_INTERVAL         = config.getInt("autosave_interval_minutes", AUTOSAVE_INTERVAL);
        UPDATE_NOTIFICATIONS      = config.getBoolean("update_notifications", UPDATE_NOTIFICATIONS);
   
        messages = new HashMap<String,String>();
        
        File messagesFile = new File(this.getDataFolder(), "messages.yml");   
        
        if(!messagesFile.exists()){
            messagesFile.getParentFile().mkdirs();
            copy(this.getResource("messages.yml"), messagesFile);
        }
        
        FileConfiguration messagesYml = YamlConfiguration.loadConfiguration(messagesFile);

        Set<String> messageList = messagesYml.getKeys(false);
        
        for(String m : messageList){
            messages.put(m, messagesYml.getString(m));
        }        
    }
    
    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isUpdateAvailable()
    {
        return this.updateAvailable;
    }
    
    public String getUpdateName()
    {
        return updateName;
    }

    public String getAPIVersion()
    {
        return this.apiVersion;
    }

    public boolean isApiOudated()
    {
        return this.outdated;
    }
}
