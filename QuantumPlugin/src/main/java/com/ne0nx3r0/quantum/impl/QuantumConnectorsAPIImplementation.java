package com.ne0nx3r0.quantum.impl;

import com.ne0nx3r0.quantum.api.IQuantumConnectorsAPI;
import com.ne0nx3r0.quantum.api.IRegistry;
import com.ne0nx3r0.quantum.api.circuit.AbstractCircuit;
import com.ne0nx3r0.quantum.api.receiver.AbstractReceiver;
import com.ne0nx3r0.quantum.api.receiver.ReceiverState;
import com.ne0nx3r0.quantum.impl.nmswrapper.QSWorld;
import com.ne0nx3r0.quantum.impl.receiver.base.Registry;
import com.ne0nx3r0.quantum.impl.utils.SourceBlockUtil;
import com.ne0nx3r0.quantum.impl.utils.VariantWrapper;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class QuantumConnectorsAPIImplementation implements IQuantumConnectorsAPI {

    private Registry<AbstractReceiver> receiverRegistry;
    private Registry<AbstractCircuit> circuitRegistry;
    private SourceBlockUtil sourceBlockUtil;
    private QSWorld qsWorld;
    private VariantWrapper variantWrapper;

    public QuantumConnectorsAPIImplementation(Registry<AbstractReceiver> receiverRegistry, Registry<AbstractCircuit> circuitRegistry, SourceBlockUtil sourceBlockUtil, QSWorld qsWorld, VariantWrapper variantWrapper) {
        this.receiverRegistry = receiverRegistry;
        this.circuitRegistry = circuitRegistry;
        this.sourceBlockUtil = sourceBlockUtil;
        this.qsWorld = qsWorld;
        this.variantWrapper = variantWrapper;
    }

    @Override
    public IRegistry<AbstractReceiver> getReceiverRegistry() {
        return receiverRegistry;
    }

    @Override
    public IRegistry<AbstractCircuit> getCircuitRegistry() {
        return circuitRegistry;
    }

    @Override
    public Location getSourceBlock(Location location) {
        return sourceBlockUtil.getSourceBlock(location);
    }

    @Override
    public void setStatic(World world, boolean isStatic) {
        qsWorld.setStatic(world, isStatic);
    }

    @Override
    public void setState(Block block, ReceiverState receiverState) {
        variantWrapper.setState(block, receiverState);
    }

    @Override
    public ReceiverState getState(Block block) {
        return variantWrapper.getState(block);
    }

    public void unregisterAll() {
        this.receiverRegistry.unregisterAll();
        this.circuitRegistry.unregisterAll();
    }
}
