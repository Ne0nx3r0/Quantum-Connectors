package com.ne0nx3r0.quantum.utils;

import com.ne0nx3r0.quantum.receiver.base.ReceiverState;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;

public class VariantWrapper {

    public static void setState(Block block, ReceiverState receiverState) {

        BlockState blockState = block.getState();
        MaterialData materialData = blockState.getData();

        switch (block.getType()) {
            case WOOL:
                ((Wool) materialData).setColor(receiverState.getDyColor());
                break;
        }
        blockState.setData(materialData);
        blockState.update();
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
