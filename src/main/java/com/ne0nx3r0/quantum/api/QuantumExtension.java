package com.ne0nx3r0.quantum.api;

public abstract class QuantumExtension {


    public abstract void onEnable(IQuantumConnectorsAPI api);

    public abstract void onDisable();

    public final String getExtensionName() {
        return getClass().getSimpleName();
    }
}
