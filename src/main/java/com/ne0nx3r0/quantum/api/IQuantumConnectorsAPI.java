package com.ne0nx3r0.quantum.api;

import com.ne0nx3r0.quantum.api.circuit.AbstractCircuit;
import com.ne0nx3r0.quantum.api.receiver.AbstractReceiver;

public interface IQuantumConnectorsAPI {

    IRegistry<AbstractReceiver> getReceiverRegistry();

    IRegistry<AbstractCircuit> getCircuitRegistry();
}
