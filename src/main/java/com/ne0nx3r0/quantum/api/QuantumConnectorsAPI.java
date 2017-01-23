package com.ne0nx3r0.quantum.api;

import com.ne0nx3r0.quantum.api.receiver.AbstractReceiver;

public class QuantumConnectorsAPI {
    private static IQuantumConnectorsAPI api;

    private QuantumConnectorsAPI() {
    }

    public static IQuantumConnectorsAPI getQuantumConnectors() {
        return api;
    }

    public static void setApi(IQuantumConnectorsAPI quantumConnectors) {
        if (QuantumConnectorsAPI.api != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton Server");
        } else {
            QuantumConnectorsAPI.api = quantumConnectors;
        }
    }

    public static IRegistry<AbstractReceiver> getReceiverRegistry() {
        return api.getReceiverRegistry();
    }

}
