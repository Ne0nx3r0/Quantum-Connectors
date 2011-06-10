package Ne0nx3r0.QuantumConnectors;

import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.block.BlockFace;

public class QuantumConnectorsBlockListener extends BlockListener{
    private final QuantumConnectors plugin;

    private static BlockFace[] faces = {
        BlockFace.NORTH,
        BlockFace.SOUTH,
        BlockFace.EAST,
        BlockFace.WEST,
        BlockFace.UP,
        BlockFace.DOWN
    };

    public QuantumConnectorsBlockListener(final QuantumConnectors plugin){
        this.plugin = plugin;
    }

    @Override
    public void onBlockRedstoneChange(BlockRedstoneEvent event){
        if(plugin.circuits.circuitExists(event.getBlock().getLocation())){
            plugin.activateCircuit(event.getBlock().getLocation(),event.getNewCurrent());
        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event){
        if(plugin.circuits.circuitExists(event.getBlock().getLocation())){
            plugin.circuits.removeCircuit(event.getBlock().getLocation());
        }
    }
}