package com.ne0nx3r0.quantum.circuits;

import com.ne0nx3r0.quantum.QuantumConnectors;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.SoundEffect;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.block.CraftBlock;
import org.bukkit.entity.Player;
import org.bukkit.material.Lever;

public final class CircuitManager{
    private static QuantumConnectors plugin;
    
// Lookup/Storage for circuits, and subsequently their receivers
    private static Map<World,Map<Location, Circuit>> worlds = new HashMap<World,Map<Location, Circuit>>();

// Temporary Holders for circuit creation
    public static Map<String, PendingCircuit> pendingCircuits;
    
// keepAlives - lamps/torches/etc that should stay powered regardless of redstone events
    public static ArrayList<Block> keepAlives;

// Allow circuitTypes/circuits
    public static Map<String, Integer> circuitTypes = new HashMap<String, Integer>();
    
    private static Material[] validSenders = new Material[]{
        Material.LEVER,
        Material.REDSTONE_WIRE,
        Material.STONE_BUTTON,
        Material.STONE_PLATE,
        Material.WOOD_PLATE,
        Material.REDSTONE_TORCH_OFF,
        Material.REDSTONE_TORCH_ON,
        Material.REDSTONE_LAMP_OFF,
        Material.REDSTONE_LAMP_ON,
        //Material.DIODE_BLOCK_OFF,
        //Material.DIODE_BLOCK_ON,//TODO: Figure out repeaters as senders
        Material.IRON_DOOR_BLOCK,
        Material.WOODEN_DOOR,
        Material.SPRUCE_DOOR,
        Material.BIRCH_DOOR,
        Material.JUNGLE_DOOR,
        Material.ACACIA_DOOR ,
        Material.DARK_OAK_DOOR,
        Material.TRAP_DOOR,
        Material.FENCE_GATE,
        Material.SPRUCE_FENCE_GATE,
        Material.BIRCH_FENCE_GATE,
        Material.JUNGLE_FENCE_GATE,
        Material.ACACIA_FENCE_GATE,
        Material.DARK_OAK_FENCE_GATE,
        Material.CHEST,
        Material.BOOKSHELF,
        Material.BED_BLOCK,
        Material.FURNACE,
        Material.WOOD_BUTTON,
        //Material.REDSTONE_COMPARATOR,
        //Material.REDSTONE_COMPARATOR_ON,
        //Material.REDSTONE_COMPARATOR_OFF,
        //Material.DAYLIGHT_DETECTOR,
        Material.DETECTOR_RAIL,
        Material.IRON_PLATE,
        Material.GOLD_PLATE,
        //Material.POWERED_RAIL,//TODO: Figure out powered rail as sender
        //Material.PISTON_BASE,
        //Material.PISTON_STICKY_BASE,//TODO: Pistons as senders
    };
    private static Material[] validReceivers = new Material[]{
        Material.LEVER,
        Material.IRON_DOOR_BLOCK,
        Material.WOODEN_DOOR,
        Material.SPRUCE_DOOR,
        Material.BIRCH_DOOR,
        Material.JUNGLE_DOOR,
        Material.ACACIA_DOOR ,
        Material.DARK_OAK_DOOR,
        Material.TRAP_DOOR,
        Material.POWERED_RAIL,
        Material.FENCE_GATE,
        Material.SPRUCE_FENCE_GATE,
        Material.BIRCH_FENCE_GATE,
        Material.JUNGLE_FENCE_GATE,
        Material.ACACIA_FENCE_GATE,
        Material.DARK_OAK_FENCE_GATE,
        Material.REDSTONE_LAMP_OFF,
        Material.REDSTONE_LAMP_ON,
        //Material.REDSTONE_TORCH_OFF,
        //Material.REDSTONE_TORCH_ON,
        //Material.PISTON_BASE,
        //Material.PISTON_STICKY_BASE,//TODO: Pistons as receivers
    };

    public static boolean shouldLeaveReceiverOn(Block block) {
        return keepAlives.contains(block);
    }

// Main
    public CircuitManager(final QuantumConnectors qc)
    {
        CircuitManager.plugin = qc;
        CircuitManager.keepAlives = new ArrayList<Block>();
        
    //Setup available circuit types 
        for (CircuitTypes t : CircuitTypes.values()){
            circuitTypes.put(t.name, t.id);
        }
     
    //Create a holder for pending circuits
        pendingCircuits = new HashMap<String,PendingCircuit>();
    
    //Convert circuits.yml to new structure    
        if(new File(plugin.getDataFolder(),"circuits.yml").exists()){
            convertOldCircuitsYml();
        }
        
    //Init any loaded worlds
        for(World world: plugin.getServer().getWorlds()){
            loadWorld(world);
        }
    }
    
// Sender/Receiver checks
    public static boolean isValidSender(Block block) {
        Material mBlock = block.getType();
        for (int i = 0; i < validSenders.length; i++) {
            if (mBlock == validSenders[i]) {
                return true;
            }
        }

        return false;
    }

    public static String getValidSendersString() {
        String msg = "";
        for (int i = 0; i < validSenders.length; i++) {
            msg += (i != 0 ? ", " : "") + validSenders[i].name().toLowerCase().replace("_", " ");
        }

        return msg;
    }

    public static boolean isValidReceiver(Block block) {
        Material mBlock = block.getType();
        for (int i = 0; i < validReceivers.length; i++) {
            if (mBlock == validReceivers[i]) {
                return true;
            }
        }

        return false;
    }

    public static String getValidReceiversString() {
        String msg = "";
        for (int i = 0; i < validReceivers.length; i++) {
            msg += (i != 0 ? ", " : "") + validReceivers[i].name().toLowerCase().replace("_", " ");
        }

        return msg;
    }
    
    
// Circuit (sender) CRUD
    public static void addCircuit(Location circuitLocation, Circuit newCircuit){
        //Notably circuits are now created from a temporary copy, rather than piecemeal here. 
        worlds.get(circuitLocation.getWorld()).put(circuitLocation, newCircuit);
    }
    public static void addCircuit(PendingCircuit pc){    
       worlds.get(pc.getSenderLocation().getWorld())
               .put(pc.getSenderLocation(),pc.getCircuit());
    }
    
    public static boolean circuitExists(Location circuitLocation){
        return worlds.get(circuitLocation.getWorld()).containsKey(circuitLocation);
    } 
    
    public static Circuit getCircuit(Location circuitLocation){
        return worlds.get(circuitLocation.getWorld()).get(circuitLocation);
    }
    
    public static void removeCircuit(Location circuitLocation) {
        if(circuitExists(circuitLocation)){
            worlds.get(circuitLocation.getWorld()).remove(circuitLocation);
        }
    }
    
// Circuit activation    
    public static void activateCircuit(Location lSender, int oldCurrent, int newCurrent){
        activateCircuit(lSender, oldCurrent, newCurrent, 0);
    }
    
    public static void activateCircuit(Location lSender, int oldCurrent, int newCurrent, int chain){
        Circuit circuit = getCircuit(lSender);
        List receivers = circuit.getReceivers();
        
        if(!receivers.isEmpty()){
            int iType;
            int iDelay;

            Receiver r;
            for(int i = 0; i < receivers.size(); i++){
                r = (Receiver) receivers.get(i);
                iType = r.type;
                iDelay = r.delay;
                Block b = r.location.getBlock();

                if (isValidReceiver(b)){
                    if (iType == CircuitTypes.QUANTUM.getId()) {
                        setReceiver(b, newCurrent > 0 ? true : false,iDelay);
                    } else if (iType == CircuitTypes.ON.getId()) {
                        if (newCurrent > 0 && oldCurrent == 0) {
                            setReceiver(b, true, iDelay);
                        }
                    } else if (iType == CircuitTypes.OFF.getId()) {
                        if (newCurrent == 0 && oldCurrent > 0) {
                            setReceiver(b, false, iDelay);
                        }
                    } else if (iType == CircuitTypes.TOGGLE.getId()) {
                        if (newCurrent > 0 && oldCurrent == 0) {
                            setReceiver(b, getBlockCurrent(b) > 0 ? false : true,iDelay);
                        }
                    } else if (iType == CircuitTypes.REVERSE.getId()) {
                        if(oldCurrent == 0 || newCurrent == 0) {
                            setReceiver(b, newCurrent > 0 ? false : true,iDelay);
                        }
                    } else if (iType == CircuitTypes.RANDOM.getId()) {
                        if(newCurrent > 0 && (oldCurrent == 0 || newCurrent == 0)) {
                            setReceiver(b, new Random().nextBoolean() ? true : false,iDelay);
                        }
                    }

                    if (b.getType() == Material.TNT) { // TnT is one time use!
                        circuit.delReceiver(r);
                    }

                    if (QuantumConnectors.MAX_CHAIN_LINKS > 0) { //allow zero to be infinite
                        chain++;
                    }
                    if(chain <= QuantumConnectors.MAX_CHAIN_LINKS && circuitExists(b.getLocation())){
                        activateCircuit(r.location, getBlockCurrent(b), chain);
                    }
                }else{
                    circuit.delReceiver(r);
                }
            }
        } 
    }
    
    private static class DelayedSetReceiver implements Runnable
    {
        private final Block block;
        private final boolean on;

        DelayedSetReceiver(Block block, boolean on){
            this.block = block;
            this.on = on;
        }
        
        @Override
        public void run()
        {
            setReceiver(block,on);
        }
    }

    public static int getBlockCurrent(Block b) {
        Material mBlock = b.getType();
        int iData = (int) b.getData();

        if(mBlock == Material.LEVER
                || mBlock == Material.POWERED_RAIL){
            return (iData & 0x08) == 0x08 ? 15 : 0;
        }else if(mBlock == Material.IRON_DOOR_BLOCK
                || mBlock == Material.WOODEN_DOOR
                || mBlock == Material.SPRUCE_DOOR
                || mBlock == Material.BIRCH_DOOR
                || mBlock == Material.JUNGLE_DOOR
                || mBlock == Material.ACACIA_DOOR
                || mBlock == Material.DARK_OAK_DOOR
                || mBlock == Material.TRAP_DOOR
                || mBlock == Material.FENCE_GATE
                || mBlock == Material.SPRUCE_FENCE_GATE
                || mBlock == Material.BIRCH_FENCE_GATE
                || mBlock == Material.JUNGLE_FENCE_GATE
                || mBlock == Material.ACACIA_FENCE_GATE
                || mBlock == Material.DARK_OAK_FENCE_GATE){
            return (iData & 0x04) == 0x04 ? 15 : 0;
        }else if(mBlock == Material.REDSTONE_LAMP_OFF
                 || mBlock == Material.REDSTONE_LAMP_ON
                 || mBlock == Material.REDSTONE_TORCH_OFF
                 || mBlock == Material.REDSTONE_TORCH_ON){
            return keepAlives.contains(b) ? 15 : 0;
        }

        return b.getBlockPower();
    }
    
    private static void setReceiver(Block block, boolean on,int iDelay){
        if(iDelay == 0){
            setReceiver(block,on);
        }else{
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(
                plugin,
                new DelayedSetReceiver(block,on),
                iDelay); 
        }
    }
    
    private static void setReceiver(Block block, boolean powerOn){
        Material mBlock = block.getType();
        int iData = (int) block.getData();

        if(mBlock == Material.LEVER)
        {          
            if(!plugin.isApiOudated())
            {                
                if ((powerOn && (iData & 0x08) != 0x08) || (!powerOn && (iData & 0x08) == 0x08))
                {
                    CraftBlock cbBlock = (CraftBlock) block;
                    BlockState cbState = cbBlock.getState();
                    net.minecraft.server.v1_10_R1.WorldServer w = ((CraftWorld) block.getWorld()).getHandle();


                    Location l = block.getLocation();

                    int blockX = l.getBlockX();
                    int blockY = l.getBlockY();
                    int blockZ = l.getBlockZ();

                    BlockPosition bp = new BlockPosition(blockX,blockY,blockZ);

                    int blockData = cbState.getRawData();
                    int j1 = blockData & 7;
                    int k1 = 8 - (blockData & 8);

                    net.minecraft.server.v1_10_R1.Block netBlock =  w.getType(bp).getBlock();

                    w.setTypeAndData(bp,netBlock.fromLegacyData(j1 + k1),3);

                    //w.makeSound((double) blockX + 0.5D, (double) blockY + 0.5D, (double) blockZ + 0.5D, "random.click", 0.3F, k1 > 0 ? 0.6F : 0.5F);

                    w.applyPhysics(bp, netBlock);

                    if (j1 == 1) {
                        w.applyPhysics(new BlockPosition(blockX - 1, blockY, blockZ), netBlock);
                    }
                    else if (j1 == 2) {
                        w.applyPhysics(new BlockPosition(blockX + 1, blockY, blockZ), netBlock);
                    }
                    else if (j1 == 3) {
                        w.applyPhysics(new BlockPosition(blockX, blockY, blockZ - 1), netBlock);
                    }
                    else if (j1 == 4) {
                        w.applyPhysics(new BlockPosition(blockX, blockY, blockZ + 1), netBlock);
                    }
                    else if (j1 != 5 && j1 != 6) {
                        if(j1 == 0 || j1 == 7) {
                            w.applyPhysics(new BlockPosition(blockX, blockY + 1, blockZ), netBlock);
                        }
                    }
                    else {
                        w.applyPhysics(new BlockPosition(blockX, blockY - 1, blockZ), netBlock);
                    }
                }
            }
            else
            {
                BlockState state = block.getState();  
                Lever lever = (Lever) state.getData();
                lever.setPowered(powerOn);
                state.setData(lever);
                state.update();
            }
        }
        else if (mBlock == Material.POWERED_RAIL)
        {
            if (powerOn && (iData & 0x08) != 0x08) {
                iData |= 0x08; //send power powerOn
            } else if (!powerOn && (iData & 0x08) == 0x08) {
                iData ^= 0x08; //send power off
            }
            block.setData((byte) iData);
        }
        else if(mBlock == Material.IRON_DOOR_BLOCK 
               || mBlock == Material.WOODEN_DOOR
                || mBlock == Material.SPRUCE_DOOR
                || mBlock == Material.BIRCH_DOOR
                || mBlock == Material.JUNGLE_DOOR
                || mBlock == Material.ACACIA_DOOR
                || mBlock == Material.DARK_OAK_DOOR) {
            Block bOtherPiece = block.getRelative(((iData & 0x08) == 0x08) ? BlockFace.DOWN : BlockFace.UP);
            int iOtherPieceData = (int) bOtherPiece.getData();

            if (powerOn && (iData & 0x04) != 0x04) {
                iData |= 0x04;
                iOtherPieceData |= 0x04;
            } else if (!powerOn && (iData & 0x04) == 0x04) {
                iData ^= 0x04;
                iOtherPieceData ^= 0x04;
            }
            block.setData((byte) iData);
            bOtherPiece.setData((byte) iOtherPieceData);
            block.getWorld().playEffect(block.getLocation(), Effect.DOOR_TOGGLE, 0, 10);
        }
        else if(mBlock == Material.TRAP_DOOR
               || mBlock == Material.FENCE_GATE
                || mBlock == Material.SPRUCE_FENCE_GATE
                || mBlock == Material.BIRCH_FENCE_GATE
                || mBlock == Material.JUNGLE_FENCE_GATE
                || mBlock == Material.ACACIA_FENCE_GATE
                || mBlock == Material.DARK_OAK_FENCE_GATE){
            if (powerOn && (iData & 0x04) != 0x04) {
                iData |= 0x04;//send open
            } else if (!powerOn && (iData & 0x04) == 0x04) {
                iData ^= 0x04;//send close
            }
            block.setData((byte) iData);
        } 
        else if (mBlock == Material.PISTON_BASE || mBlock == Material.PISTON_STICKY_BASE) {
            // Makeshift piston code... Doesn't work!
            if (powerOn && (iData & 0x08) != 0x08) {
                iData |= 0x08; //send power powerOn
            } else if (!powerOn && (iData & 0x08) == 0x08) {
                iData ^= 0x08; //send power off
            }
            block.setData((byte) iData);
            //net.minecraft.server.Block.PISTON.doPhysics(((CraftWorld)block.getWorld()).getHandle(), block.getX(), block.getY(), block.getZ(), -1);
        } /*else if (mBlock == Material.REDSTONE_TORCH_ON) {
            if (!powerOn) {
                keepAlives.remove(block);
                block.setType(Material.REDSTONE_TORCH_OFF);
            }
        } else if (mBlock == Material.REDSTONE_TORCH_OFF) {
            if (powerOn) {
                keepAlives.add(block);
                block.setType(Material.REDSTONE_TORCH_ON);
            }
        }*/
        else if (mBlock == Material.REDSTONE_LAMP_ON) {
            if (!powerOn) {
                keepAlives.remove(block);
                block.setType(Material.REDSTONE_LAMP_OFF);
            }
        } else if (mBlock == Material.REDSTONE_LAMP_OFF) {
            if (powerOn) {
                keepAlives.add(block);

                net.minecraft.server.v1_10_R1.World w = ((CraftWorld) block.getWorld()).getHandle();

                try {
                    setStaticStatus(w, true);
                    block.setType(Material.REDSTONE_LAMP_ON);
                    setStaticStatus(w, false);
                }
                catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void setStaticStatus(net.minecraft.server.v1_10_R1.World w, boolean isStatic) throws NoSuchFieldException, IllegalAccessException {
        java.lang.reflect.Field field = net.minecraft.server.v1_10_R1.World.class.getDeclaredField("isClientSide");


        field.setAccessible(true);

        field.set(w, isStatic);
    }

    public void saveWorld(World world){
        if(worlds.containsKey(world)){
        //Alright let's do this!
            File ymlFile = new File(plugin.getDataFolder(),world.getName()+".circuits.yml");
            if(!ymlFile.exists()) {
                try {
                    ymlFile.createNewFile();
                } catch(IOException ex) {
                    plugin.error("Could not create "+ymlFile.getName());
                }
            }
            FileConfiguration yml = YamlConfiguration.loadConfiguration(ymlFile);
            
            if(QuantumConnectors.VERBOSE_LOGGING) plugin.log(plugin.getMessage("saving").replace("%file",ymlFile.getName()));
            
        //Prep this world's data for saving
            List<Object> tempCircuits = new ArrayList<Object>();

            Map<String,Object> tempCircuitObj;
            Map<String,Object> tempReceiverObj;
            ArrayList tempReceiverObjs;
            Circuit currentCircuit;
            List<Receiver> currentReceivers;

            Map<Location,Circuit> currentWorldCircuits = worlds.get(world);
            
            for(Location cLoc : currentWorldCircuits.keySet()){

                currentCircuit = currentWorldCircuits.get(cLoc);

                tempCircuitObj = new HashMap<String,Object>();

                tempCircuitObj.put("x",cLoc.getBlockX());
                tempCircuitObj.put("y",cLoc.getBlockY());
                tempCircuitObj.put("z",cLoc.getBlockZ());
                
                tempCircuitObj.put("o", currentCircuit.getOwner());

                currentReceivers = currentCircuit.getReceivers();

                tempReceiverObjs = new ArrayList();
                Receiver r;
                for(int i = 0; i < currentReceivers.size(); i++){
                    r = currentReceivers.get(i);
                    
                    tempReceiverObj = new HashMap<String,Object>();
                    
                    tempReceiverObj.put("z",r.location.getBlockZ());
                    tempReceiverObj.put("y",r.location.getBlockY());
                    tempReceiverObj.put("x",r.location.getBlockX());
                    
                    tempReceiverObj.put("d",r.delay);
                    tempReceiverObj.put("t",r.type);
                    
                    tempReceiverObjs.add(tempReceiverObj);
                }

                tempCircuitObj.put("r",tempReceiverObjs);

                tempCircuits.add(tempCircuitObj);
            }
            
            yml.set("fileVersion","2");
            yml.set("circuits", tempCircuits);

            try{
                yml.save(ymlFile);

                if(QuantumConnectors.VERBOSE_LOGGING) plugin.log(plugin.getMessage("saved").replace("%file",ymlFile.getName()));
            }catch(IOException IO) {
                plugin.error(plugin.getMessage("save_failed").replace("%world", world.getName()));
            }  
        }else{
            plugin.error(plugin.getMessage("save_failed").replace("%world", world.getName()));
        }
    }
    
    public void saveAllWorlds(){
        for(World world : worlds.keySet()){
            saveWorld(world);
        }
        //huh, that was easy.
    }

    public void loadWorld(World world){
        //at least create a blank holder
        worlds.put(world,new HashMap<Location,Circuit>());
        
        File ymlFile = new File(plugin.getDataFolder(),world.getName()+".circuits.yml");
        
        if(QuantumConnectors.VERBOSE_LOGGING) plugin.log(plugin.getMessage("loading").replace("%file%",ymlFile.getName()));

        if(!ymlFile.exists()) {
            if(QuantumConnectors.VERBOSE_LOGGING) plugin.error(plugin.getMessage("loading_not_found").replace("%file%",ymlFile.getName()));
            return;
        }
        
        FileConfiguration yml = YamlConfiguration.loadConfiguration(ymlFile);
        
        List<Map<String,Object>> tempCircuits = (List<Map<String,Object>>) yml.get("circuits");

        if(tempCircuits == null){
            plugin.log(plugin.getMessage("loading_no_circuits").replace("%file%",ymlFile.getName()));
            return;
        }
        
        Map<Location,Circuit> worldCircuits = new HashMap<Location,Circuit>();
        
        Location tempCircuitObjLoc;
        
        ArrayList tempReceiverObjs;
        Map<String,Object> tempReceiverObj;
        Location tempReceiverLoc;

        for(Map<String,Object> tempCircuitObj : tempCircuits){          
            //dummy value of # for owners
            Circuit tempCircuit = new Circuit((String) (tempCircuitObj.get("o") == null ? "" : tempCircuitObj.get("o")));
            tempReceiverObjs = (ArrayList) tempCircuitObj.get("r");
            
        //TODO: circuit/receiver verification
            for(int i = 0; i < tempReceiverObjs.size(); i++) {
                tempReceiverObj = (Map<String, Object>) tempReceiverObjs.get(i);
                tempReceiverLoc = new Location(
                        world,
                        (Integer) tempReceiverObj.get("x"),
                        (Integer) tempReceiverObj.get("y"),
                        (Integer) tempReceiverObj.get("z"));
                
                if(CircuitManager.isValidReceiver(tempReceiverLoc.getBlock())){
                    tempCircuit.addReceiver(
                            tempReceiverLoc,
                            (Integer) tempReceiverObj.get("t"),
                            (Integer) tempReceiverObj.get("d"));
                }
                //Invalid receiver block type
                else{
                    if(QuantumConnectors.VERBOSE_LOGGING) plugin.log(
                        plugin.getMessage("receiver_removed")
                            .replace("%world%",world.getName())
                            .replace("%block%",tempReceiverLoc.getBlock().getType().name())
                    );
                }
            }

            // Verify there is at least one valid receiver
                if(!tempCircuit.getReceivers().isEmpty()){
                    tempCircuitObjLoc = new Location(
                                world,
                                (Integer) tempCircuitObj.get("x"),
                                (Integer) tempCircuitObj.get("y"),
                                (Integer) tempCircuitObj.get("z"));
                    
                    //Verify the sender is a valid type
                    if(CircuitManager.isValidSender(tempCircuitObjLoc.getBlock())){
                        worldCircuits.put(tempCircuitObjLoc,tempCircuit); 
                    }
                    //Invalid sender type
                    else{
                        if(QuantumConnectors.VERBOSE_LOGGING) plugin.log(plugin.getMessage("circuit_removed_invalid")
                                .replace("%world",world.getName())
                                .replace("%block%",tempCircuitObjLoc.getBlock().getType().name()));
                    }
                }
            // No valid receivers for this circuit
                else{
                    if(QuantumConnectors.VERBOSE_LOGGING) plugin.log(plugin.getMessage("circuit_removed_no_receivers")
                            .replace("%world%",world.getName()));
                }
        }
        
        worlds.put(world,worldCircuits);
    }

// Temporary circuit stuff 
// I really don't know what order this deserves among the existing class methods
    public static PendingCircuit addPendingCircuit(Player player,int type,int delay){
        PendingCircuit pc = new PendingCircuit(player.getName(),type,delay);
        
        pendingCircuits.put(player.getName(), pc);
        
        return pc;
    }
    
    public static PendingCircuit getPendingCircuit(Player player){
        return pendingCircuits.get(player.getName());
    }
    
    public static boolean hasPendingCircuit(Player player){
        return pendingCircuits.containsKey(player.getName());
    }
    
    public static void removePendingCircuit(Player player){
        pendingCircuits.remove(player.getName());
    }
    
//Circuit Types
    public static boolean isValidCircuitType(String type){
        return circuitTypes.containsKey(type);
    }
    
    public static int getCircuitType(String sType){
        return circuitTypes.get(sType);
    }
    
    public static Map<String, Integer> getValidCircuitTypes(){
        return circuitTypes;
    }
    
//1.2.3 circuits.yml Converter
    public void convertOldCircuitsYml(){
        File oldYmlFile = new File(plugin.getDataFolder(),"circuits.yml");
        if(oldYmlFile.exists()){
            plugin.log(plugin.getMessage("found_old_file").replace("%file%",oldYmlFile.getName()));

            FileConfiguration oldYml = YamlConfiguration.loadConfiguration(oldYmlFile);

            for(String worldName : oldYml.getValues(false).keySet()){
                ArrayList tempCircuitObjs = new ArrayList();

                for (int x = 0;; x++){
                    String path = worldName + ".circuit_" + x;
                    
                    if (oldYml.get(path) == null) {
                        break;
                    }

                    Map<String,Object> tempCircuitObj = new HashMap<String,Object>();
                    
                    String[] senderXYZ = oldYml.get(path + ".sender").toString().split(",");
                    
                    tempCircuitObj.put("x", Integer.parseInt(senderXYZ[0]));
                    tempCircuitObj.put("y", Integer.parseInt(senderXYZ[1]));
                    tempCircuitObj.put("z", Integer.parseInt(senderXYZ[2]));

                    //they'll all be the same, should only ever be one anyway
                    String receiversType = oldYml.get(path+".type").toString();
                    
                   ArrayList tempReceiverObjs = new ArrayList();
                    for(Object receiver : oldYml.getList(path + ".receivers")){
                        Map<String,Object> tempReceiverObj = new HashMap<String,Object>();
                        
                        String[] sReceiverLoc = receiver.toString().split(",");
                        
                        tempReceiverObj.put("x", Integer.parseInt(sReceiverLoc[0]));
                        tempReceiverObj.put("y", Integer.parseInt(sReceiverLoc[1]));
                        tempReceiverObj.put("z", Integer.parseInt(sReceiverLoc[2]));
                        tempReceiverObj.put("d", 0);
                        tempReceiverObj.put("t", Integer.parseInt(receiversType));

                        tempReceiverObjs.add(tempReceiverObj);
                    }
                    
                    tempCircuitObj.put("r",tempReceiverObjs);
                    
                    tempCircuitObjs.add(tempCircuitObj);
                }
                
                File newYmlFile = new File(plugin.getDataFolder(),worldName+".circuits.yml");
                FileConfiguration newYml = YamlConfiguration.loadConfiguration(newYmlFile);
                
                newYml.set("fileVersion", 2);
                newYml.set("circuits", tempCircuitObjs);
                
                try {
                    newYml.save(newYmlFile);
                } catch (IOException ex) {
                    plugin.error(plugin.getMessage("unable_to_save").replace("%file%",newYmlFile.getName()));
                    
                    Logger.getLogger(CircuitManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }    

            //I dunno man. Java file operations are a mystery to me. These lines worked.
            File testFile = new File(plugin.getDataFolder(),"circuits.yml.bak");
            new File(plugin.getDataFolder(),"circuits.yml").renameTo(testFile);
        }
    }
    
    public Set<Location> circuitLocations(World w) {
        return this.worlds.get(w).keySet();
    }
}
