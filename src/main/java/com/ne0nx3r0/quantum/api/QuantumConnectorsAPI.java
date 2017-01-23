package com.ne0nx3r0.quantum.api;

/**
 * Created by Yannick on 23.01.2017.
 */
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

    public static IReceiverRegistry getRegistry() {
        return api.getRegistry();
    }

}
