package com.ne0nx3r0.quantum;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import com.ne0nx3r0.quantum.circuits.Circuit;
import com.ne0nx3r0.quantum.circuits.CircuitManager;
import com.ne0nx3r0.quantum.circuits.CircuitTypes;
import com.ne0nx3r0.quantum.listeners.QuantumConnectorsBlockListener;
import com.ne0nx3r0.quantum.listeners.QuantumConnectorsPlayerListener;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class QuantumConnectors extends JavaPlugin{    
//Grab the logger
    public static final Logger log = Logger.getLogger("Minecraft");
    
//Register events
    private final QuantumConnectorsPlayerListener playerListener = new QuantumConnectorsPlayerListener(this);
    private final QuantumConnectorsBlockListener blockListener = new QuantumConnectorsBlockListener(this);
    
//List of circuit types
    public static Map<String, Integer> circuitTypes = new HashMap<String, Integer>();

//Circuit Manager
    public static CircuitManager circuits;
    
//Configurables
    public int MAX_CHAIN_LINKS = 3;
    private int AUTOSAVE_INTERVAL = 10;//specified here in minutes
    private static int AUTO_SAVE_ID = -1;
    
//Holders
    public static Map<Player, Circuit> tempCircuits;
    public static Map<Player, Location> tempCircuitLocations;
    public static Map<Player, Integer> tempCircuitTypes;
    
    @Override
    public void onDisable(){
        circuits.save();
        
        log.info("[QuantumConnectors] Disabled");
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
        circuits = new CircuitManager(new File(this.getDataFolder(),"circuits_v2.yml"),this);
        
    //Initialize holders
        tempCircuits = new HashMap<Player, Circuit>();
        tempCircuitLocations = new HashMap<Player, Location>();
        tempCircuitTypes = new HashMap<Player, Integer>();
        
    //Register qc command
        getCommand("qc").setExecutor(new QuantumConnectorsCommandExecutor(this));   
        
    //Register listeners
        PluginManager pm = getServer().getPluginManager();
        
        pm.registerEvents(playerListener, this);
        pm.registerEvents(blockListener, this);
        
    //Schedule saves
        AUTOSAVE_INTERVAL = AUTOSAVE_INTERVAL*60*20;//convert to minutes
        
        AUTO_SAVE_ID = getServer().getScheduler().scheduleSyncRepeatingTask(
            this,
            autosaveCircuits,
            AUTOSAVE_INTERVAL,
            AUTOSAVE_INTERVAL);
        
    //All done!
        log.info("[QuantumConnectors] Enabled");
    }	
    
    public void msg(Player player, String sMessage) {
        player.sendMessage(ChatColor.LIGHT_PURPLE + "[QC] " + ChatColor.WHITE + sMessage);
    }
    
    //Scheduled save mechanism
    private Runnable autosaveCircuits = new Runnable() {
        public void run() {
            circuits.save();
        }
    };
}
