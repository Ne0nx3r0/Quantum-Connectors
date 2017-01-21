package com.ne0nx3r0.quantum.utils;

import com.ne0nx3r0.quantum.receiver.base.ReceiverState;
import org.bukkit.block.BlockState;
import org.bukkit.material.Wool;

/**
 * Created by Yannick on 21.01.2017.
 */
public class WoolBlockVariant implements BlockVariant {

    private BlockState blockState;
    private Wool wool;

    public WoolBlockVariant(BlockState blockState) {
        this.blockState = blockState;
        this.wool = (Wool) blockState.getData();
    }

    @Override
    public ReceiverState getState() {
        return ReceiverState.getByColor(wool.getColor());
    }

    @Override
    public void setState(ReceiverState receiverState) {
        wool.setColor(receiverState.getDyColor());
        blockState.setData(wool);
        blockState.update(true);
    }
}
