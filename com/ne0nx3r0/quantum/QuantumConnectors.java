package com.ne0nx3r0.quantum;

import com.ne0nx3r0.quantum.circuits.Circuit;
import com.ne0nx3r0.quantum.circuits.CircuitManager;
import com.ne0nx3r0.quantum.circuits.CircuitTypes;
import com.ne0nx3r0.quantum.listeners.QuantumConnectorsBlockListener;
import com.ne0nx3r0.quantum.listeners.QuantumConnectorsPlayerListener;
import com.ne0nx3r0.quantum.listeners.QuantumConnectorsWorldListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class QuantumConnectors extends JavaPlugin{    
//Grab the logger
    public static final Logger logger = Logger.getLogger("Minecraft");
    
//Register events
    private final QuantumConnectorsPlayerListener playerListener = new QuantumConnectorsPlayerListener(this);
    private final QuantumConnectorsBlockListener blockListener = new QuantumConnectorsBlockListener(this);
    private final QuantumConnectorsWorldListener worldListener = new QuantumConnectorsWorldListener(this);
    
//List of circuit types
    public static Map<String, Integer> circuitTypes = new HashMap<String, Integer>();

//Circuit Manager
    public static CircuitManager circuitManager;
    
//Configurables
    public static int MAX_CHAIN_LINKS = 3;
    private static int AUTOSAVE_INTERVAL = 30;//specified here in minutes
    private static int AUTO_SAVE_ID = -1;
    
//Holders
    public static Map<Player, Circuit> tempCircuits;
    public static Map<Player, Location> tempCircuitLocations;
    public static Map<Player, Integer> tempCircuitTypes;
    public static Map<Player, Integer> tempCircuitDelays;
    
    @Override
    public void onDisable(){
        circuitManager.saveAllWorlds();
        
        log("Disabled");
    }
    
    @Override
    public void onEnable(){
    //This might be outdated...
        getDataFolder().mkdirs();
        
    //Setup available circuit types 
        for (CircuitTypes t : CircuitTypes.values()){
            circuitTypes.put(t.name, t.id);
        }
        
    //Create a circuit manager
        circuitManager = new CircuitManager(this);
        
    //Initialize holders
        tempCircuits = new HashMap<Player, Circuit>();
        tempCircuitLocations = new HashMap<Player, Location>();
        tempCircuitTypes = new HashMap<Player, Integer>();
        tempCircuitDelays = new HashMap<Player, Integer>();
        
    //Register qc command
        getCommand("qc").setExecutor(new QuantumConnectorsCommandExecutor(this));   
        
    //Register listeners
        PluginManager pm = getServer().getPluginManager();
        
        pm.registerEvents(playerListener, this);
        pm.registerEvents(blockListener, this);
        pm.registerEvents(worldListener, this);
        
    //Schedule saves
        AUTOSAVE_INTERVAL = AUTOSAVE_INTERVAL * 60 * 20;//convert to minutes
        
        AUTO_SAVE_ID = getServer().getScheduler().scheduleSyncRepeatingTask(
            this,
            autosaveCircuits,
            AUTOSAVE_INTERVAL,
            AUTOSAVE_INTERVAL);
        
    //All done!
        log("Enabled");
    }	
    
    public void msg(Player player, String sMessage) {
        player.sendMessage(ChatColor.LIGHT_PURPLE + "[QC] " + ChatColor.WHITE + sMessage);
    }

//Generic wrappers for console messages
    public void log(Level level,String sMessage){
        if(!sMessage.equals(""))
            logger.log(level,"[QuantumConnectors] " + sMessage);
    }
    public void log(String sMessage){
        log(Level.INFO,sMessage);
    }
    public void error(String sMessage){
        log(Level.WARNING,sMessage);
    }
    
    //Scheduled save mechanism
    private Runnable autosaveCircuits = new Runnable() {
        public void run() {
            circuitManager.saveAllWorlds();
        }
    };
}
