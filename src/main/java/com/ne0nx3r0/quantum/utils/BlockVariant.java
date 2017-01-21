package com.ne0nx3r0.quantum.utils;

import com.ne0nx3r0.quantum.receiver.base.ReceiverState;

/**
 * Created by Yannick on 21.01.2017.
 */
public interface BlockVariant {
    ReceiverState getState();

    void setState(ReceiverState receiverState);

}
