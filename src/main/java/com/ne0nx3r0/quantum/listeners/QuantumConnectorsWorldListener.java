package com.ne0nx3r0.quantum.listeners;

import com.ne0nx3r0.quantum.circuits.CircuitLoader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class QuantumConnectorsWorldListener implements Listener {
    private CircuitLoader circuitLoader;

    public QuantumConnectorsWorldListener(CircuitLoader circuitLoader) {
        this.circuitLoader = circuitLoader;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(WorldLoadEvent event) {
        circuitLoader.loadWorld(event.getWorld());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldUnload(WorldUnloadEvent event) {
        circuitLoader.saveWorld(event.getWorld());
    }
    /*
  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onChunkUnload(ChunkUnloadEvent e)
  {
    for(Iterator i$ = QuantumConnectors.circuitManager.circuitLocations(e.getWorld()).iterator(); i$.hasNext(); )
    {
        Location loc = (Location)i$.next();
        
        for(Receiver_old r : CircuitManager.getCircuit(loc).getReceivers())
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
