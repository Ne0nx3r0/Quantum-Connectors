package com.ne0nx3r0.quantum.circuits;

import org.bukkit.block.Block;

/**
 * Created by ysl3000 on 19.01.17.
 */
public interface ICircuitManager {

    boolean isValidReceiver(Block block);

    boolean isValidSender(Block block);


}
