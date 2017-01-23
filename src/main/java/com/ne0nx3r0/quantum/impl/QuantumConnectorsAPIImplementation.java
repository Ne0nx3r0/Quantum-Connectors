package com.ne0nx3r0.quantum.impl;

import com.ne0nx3r0.quantum.api.IQuantumConnectorsAPI;
import com.ne0nx3r0.quantum.api.IRegistry;
import com.ne0nx3r0.quantum.api.circuit.AbstractCircuit;
import com.ne0nx3r0.quantum.api.receiver.AbstractReceiver;
import com.ne0nx3r0.quantum.impl.receiver.base.Registry;

public class QuantumConnectorsAPIImplementation implements IQuantumConnectorsAPI {

    private Registry<AbstractReceiver> receiverRegistry;
    private Registry<AbstractCircuit> circuitRegistry;

    public QuantumConnectorsAPIImplementation(Registry<AbstractReceiver> receiverRegistry, Registry<AbstractCircuit> circuitRegistry) {
        this.receiverRegistry = receiverRegistry;
        this.circuitRegistry = circuitRegistry;
    }

    @Override
    public IRegistry<AbstractReceiver> getReceiverRegistry() {
        return receiverRegistry;
    }

    @Override
    public IRegistry<AbstractCircuit> getCircuitRegistry() {
        return circuitRegistry;
    }

    public void unregisterAll() {
        this.receiverRegistry.unregisterAll();
        this.circuitRegistry.unregisterAll();
    }
}
