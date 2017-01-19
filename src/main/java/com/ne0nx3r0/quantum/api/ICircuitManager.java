package com.ne0nx3r0.quantum.api;

import org.bukkit.block.Block;

public interface ICircuitManager {

    boolean isValidReceiver(Block block);

    boolean isValidSender(Block block);


}
