package com.ne0nx3r0.quantum.utils;

import com.ne0nx3r0.quantum.receiver.base.ReceiverState;
import org.bukkit.block.Block;
import org.bukkit.material.Wool;

/**
 * Created by Yannick on 21.01.2017.
 */
public class VariantWrapper {

    public static void setState(Block block, ReceiverState receiverState) {

        switch (block.getType()) {
            case WOOL:
                ((Wool) block.getState().getData()).setColor(receiverState.getDyColor());
                break;
        }
    }


    public static ReceiverState getState(Block block) {

        switch (block.getType()) {

            case WOOL:
                return ReceiverState.getByColor(((Wool) block.getState().getData()).getColor());
            default:
                return ReceiverState.S0;
        }
    }
}
