package com.ne0nx3r0.quantum.utils;

import com.ne0nx3r0.quantum.receiver.base.ReceiverState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.Colorable;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.material.Redstone;

import static com.ne0nx3r0.quantum.circuits.CircuitManager.keepAlives;

public class VariantWrapper {

    public static void setState(Block block, ReceiverState receiverState) {
        BlockState blockState = block.getState();
        MaterialData md = blockState.getData();
        if (md instanceof Colorable) {
            ((Colorable) md).setColor(receiverState.getDyColor());
        }
        blockState.setData(md);
        blockState.update();
    }
    public static ReceiverState getState(Block block) {
        Material material = block.getType();
        MaterialData md = block.getState().getData();
        if (md instanceof Redstone) {
            return ((Redstone) md).isPowered() ? ReceiverState.S15 : ReceiverState.S0;
        } else if (md instanceof Openable) {
            return ((Openable) md).isOpen() ? ReceiverState.S15 : ReceiverState.S0;
        } else if (ValidMaterials.LAMP.contains(material)) {
            return keepAlives.contains(block) ? ReceiverState.S15 : ReceiverState.S0;
        } else if (md instanceof Colorable) {
            return ReceiverState.getByColor(((Colorable) md).getColor());
        }

        return ReceiverState.values()[block.getBlockPower()];

    }
}
