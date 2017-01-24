package com.ne0nx3r0.quantum.api;

import com.ne0nx3r0.quantum.api.circuit.AbstractCircuit;
import com.ne0nx3r0.quantum.api.receiver.AbstractReceiver;
import com.ne0nx3r0.quantum.api.receiver.ReceiverState;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public interface IQuantumConnectorsAPI {

    IRegistry<AbstractReceiver> getReceiverRegistry();

    IRegistry<AbstractCircuit> getCircuitRegistry();

    Location getSourceBlock(Location location);

    void setStatic(World world, boolean isStatic);

    void setState(Block block, ReceiverState receiverState);

    ReceiverState getState(Block block);
}
