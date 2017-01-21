package com.ne0nx3r0.quantum.api;

import com.ne0nx3r0.quantum.receiver.base.ReceiverState;
import org.bukkit.block.Block;

/**
 * Created by Yannick on 21.01.2017.
 */
public interface RecieverSetter {
    void setReceiver(Receiver receiver, boolean power);

    ReceiverState getState(Block block);
}
