package com.ne0nx3r0.quantum.listeners;

import com.ne0nx3r0.quantum.QuantumConnectors;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class QuantumConnectorsWorldListener implements Listener {
    private QuantumConnectors plugin;

    public QuantumConnectorsWorldListener(QuantumConnectors plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(WorldLoadEvent event) {
        QuantumConnectors.circuitManager.loadWorld(event.getWorld());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldUnload(WorldUnloadEvent event) {
        QuantumConnectors.circuitManager.saveWorld(event.getWorld());
    }
    /*
  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onChunkUnload(ChunkUnloadEvent e)
  {
    for(Iterator i$ = QuantumConnectors.circuitManager.circuitLocations(e.getWorld()).iterator(); i$.hasNext(); )
    {
        Location loc = (Location)i$.next();
        
        for(Receiver r : CircuitManager.getCircuit(loc).getReceivers())
        {
            Location l = r.location;
            
            int circuitChunkX = loc.getBlock().getChunk().getX();
            int chunkX = e.getChunk().getX();
            int circuitChunkZ = loc.getBlock().getChunk().getZ();
            int chunkZ = e.getChunk().getZ();

            if ((Math.abs(chunkX - circuitChunkX) <= this.plugin.getChunkUnloadRange()) && (Math.abs(chunkZ - circuitChunkZ) <= this.plugin.getChunkUnloadRange()))
            {
                e.setCancelled(true);
            }
        }
    }
  }*/
}
