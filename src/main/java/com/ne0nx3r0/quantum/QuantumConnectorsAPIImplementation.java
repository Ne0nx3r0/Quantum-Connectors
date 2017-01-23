package com.ne0nx3r0.quantum;

import com.ne0nx3r0.quantum.api.IQuantumConnectorsAPI;
import com.ne0nx3r0.quantum.api.IReceiverRegistry;
import com.ne0nx3r0.quantum.receiver.base.ReceiverRegistry;

/**
 * Created by Yannick on 23.01.2017.
 */
public class QuantumConnectorsAPIImplementation implements IQuantumConnectorsAPI {

    private ReceiverRegistry receiverRegistry;

    public QuantumConnectorsAPIImplementation(ReceiverRegistry receiverRegistry) {
        this.receiverRegistry = receiverRegistry;
    }

    @Override
    public IReceiverRegistry getRegistry() {
        return receiverRegistry;
    }
}
