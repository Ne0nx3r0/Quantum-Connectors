package com.ne0nx3r0.quantum.impl;

import com.ne0nx3r0.quantum.api.IQuantumConnectorsAPI;
import com.ne0nx3r0.quantum.api.IRegistry;
import com.ne0nx3r0.quantum.api.receiver.AbstractReceiver;

public class QuantumConnectorsAPIImplementation implements IQuantumConnectorsAPI {

    private IRegistry<AbstractReceiver> receiverRegistry;

    public QuantumConnectorsAPIImplementation(IRegistry<AbstractReceiver> receiverRegistry) {
        this.receiverRegistry = receiverRegistry;
    }

    @Override
    public IRegistry<AbstractReceiver> getReceiverRegistry() {
        return receiverRegistry;
    }
}
