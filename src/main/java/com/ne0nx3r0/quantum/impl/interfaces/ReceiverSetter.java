package com.ne0nx3r0.quantum.impl.interfaces;

import com.ne0nx3r0.quantum.api.receiver.Receiver;
import org.bukkit.block.Block;

/**
 * Created by Yannick on 21.01.2017.
 */
public interface ReceiverSetter {
    void setReceiver(Receiver receiver, boolean power);

    int getBlockCurrent(Block block);
}
