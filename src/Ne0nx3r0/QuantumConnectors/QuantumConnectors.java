package Ne0nx3r0.QuantumConnectors;

import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import java.util.Random;
import org.bukkit.Location;

public class QuantumConnectors extends JavaPlugin{
    private final QuantumConnectorsBlockListener blockListener = new QuantumConnectorsBlockListener(this);
    private final QuantumConnectorsPlayerListener playerListener = new QuantumConnectorsPlayerListener(this);

    public static Map<String,Integer> circuitTypes = new HashMap<String,Integer>();

    public static CircuitManager circuits;

    private static int AUTO_SAVE_ID = -1;

    public int typeQuantum = 0;
    public int typeOn = 1;
    public int typeOff = 2;
    public int typeToggle = 3;
    public int typeReverse = 4;
    public int typeRandom = 5;

    //configurables
    private int MAX_CHAIN_LINKS = 3;
    private int AUTOSAVE_INTERVAL = 5 * 60 * 20;//minutes*seconds/minute*ticks/second

    public void onEnable(){
        //register events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.REDSTONE_CHANGE, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);

        //setup circuits
        circuitTypes.put("quantum",typeQuantum);
        circuitTypes.put("on",typeOn);
        circuitTypes.put("off",typeOff);
        circuitTypes.put("toggle",typeToggle);
        circuitTypes.put("reverse",typeReverse);
        circuitTypes.put("random",typeRandom);

        //setup circuit manager
        circuits = new CircuitManager(new File(this.getDataFolder(),"circuits.yml"),this);

        //scheduled saves
        AUTO_SAVE_ID = getServer().getScheduler().scheduleSyncRepeatingTask(
            this,autosaveCircuits,AUTOSAVE_INTERVAL,AUTOSAVE_INTERVAL);

        //enabled msg
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println("[Quantum Connectors] version " + pdfFile.getVersion() + " ENABLED");
    }

    public void msg(Player player,String sMessage){
        player.sendMessage(ChatColor.LIGHT_PURPLE+"[QC] "+ChatColor.WHITE+sMessage);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
    	if(!(sender instanceof Player)){
            System.out.println("This command has to be called by a player");
            return true;
    	}

        Player pSender = (Player) sender;

        if(args.length == 0 || args[0].equalsIgnoreCase("?")){
            msg(pSender,"To create a quantum circuit, use /qc <circuit>; and click   on a sender and then a receiver with redstone.");

            String sAvailableCircuits = "";
            for(String sKey : circuitTypes.keySet()){
                sAvailableCircuits += sKey+", ";
            }
            sAvailableCircuits = sAvailableCircuits.substring(0,sAvailableCircuits.length()-2);

            msg(pSender,ChatColor.YELLOW+"Available circuits: "+ChatColor.WHITE+sAvailableCircuits);

            return true;
        }else if(args[0].equalsIgnoreCase("cancel")){
            if(playerListener.pendingCircuits.containsKey(pSender)){
                playerListener.pendingCircuits.remove(pSender);
                playerListener.pendingSenders.remove(pSender);

                msg(pSender,"Pending circuit removed!");
            }else{
                msg(pSender,"No pending circuits");
            }
        }else if(args[0] != null){
            if(circuitTypes.containsKey(args[0])){
                playerListener.pendingCircuits.put(pSender,circuitTypes.get(args[0]));

                msg(pSender,"Circuit is ready to be created!");
            }else{
                msg(pSender,"Invalid circuit specified!");
            }
        }else{
            msg(pSender,"Invalid qc command.");
        }

        return true;
    }

    @Override
    public void onDisable(){
        circuits.Save();

        getServer().getScheduler().cancelTask(AUTO_SAVE_ID);
    }

    public void activateCircuit(Location lSender,int current){
        activateCircuit(lSender,current,0);
    }

    //would have preferred to put these someplace else, but I couldn't find a convenient spot
    public void activateCircuit(Location lSender,int current,int chain){
        Circuit circuit = circuits.getCircuit(lSender);

        Block bReceiver = circuit.reciever.getBlock();

        if(circuits.isValidReceiver(bReceiver)){
            int iType = circuit.type;

            if(iType == typeQuantum){
                if(current > 0){
                    setOn(bReceiver);
                }else{
                    setOff(bReceiver);
                }
            }else if(iType == typeOn){
                if(current > 0){
                    setOn(bReceiver);
                }
            }else if(iType == typeOff){
                if(current > 0){
                    setOff(bReceiver);
                }
            }else if(iType == typeToggle){
                if(current > 0){
                    setOn(bReceiver);
                }
            }else if(iType == typeReverse){
                if(current > 0){
                    setOff(bReceiver);
                }else{
                    setOn(bReceiver);
                }
            }else if(iType == typeRandom){
                if(current > 0){
                    Random randomGenerator = new Random();

                    if(randomGenerator.nextBoolean()){
                        setOn(bReceiver);
                    }else{
                        setOff(bReceiver);
                    }
                }
            }

            if(chain < MAX_CHAIN_LINKS && circuits.circuitExists(bReceiver.getLocation())){
                activateCircuit(bReceiver.getLocation(),current,chain+1);
            }
        }else{
            circuits.removeCircuit(lSender);
        }

    }

    private static void setOn(Block block){
        setReceiver(block,true);
    }
    private static void setOff(Block block){
        setReceiver(block,false);
    }

    private static void setReceiver(Block block,boolean on){
        Material mBlock = block.getType();
        int iData = (int) block.getData();

        if(mBlock == Material.LEVER || mBlock == Material.POWERED_RAIL){
            if(on && (iData&0x08) != 0x08){
                iData|=0x08;//send power on
                block.setData((byte) iData);
            }else if(!on && (iData&0x08) == 0x08){
                iData^=0x08;//send power off
                block.setData((byte) iData);
            }
        }else if(mBlock == Material.IRON_DOOR_BLOCK || mBlock == Material.WOODEN_DOOR){
            Block bOtherPiece;

            if((iData&0x08) == 0x08){
                bOtherPiece = block.getFace(BlockFace.DOWN);
            }else{
                bOtherPiece = block.getFace(BlockFace.UP);
            }
            int iOtherPieceData = (int) bOtherPiece.getData();

            if(on && (iData&0x04) != 0x04){
                iData|=0x04;//send open
                block.setData((byte) iData);

                iOtherPieceData|=0x04;//send open
                bOtherPiece.setData((byte) iOtherPieceData);
            }else if(!on && (iData&0x04) == 0x04){
                iData^=0x04;//send close
                block.setData((byte) iData);

                iOtherPieceData^=0x04;//send close
                bOtherPiece.setData((byte) iOtherPieceData);
            }
        }else if(mBlock == Material.TRAP_DOOR){
            if(on && (iData&0x04) != 0x04){
                iData|=0x04;//send open
                block.setData((byte) iData);
            }else if(!on && (iData&0x04) == 0x04){
                iData^=0x04;//send close
                block.setData((byte) iData);
            }
        }
    }

    //Scheduled save mechanism
    private Runnable autosaveCircuits = new Runnable() {
        public void run() {
            circuits.Save();
        }
    };
}