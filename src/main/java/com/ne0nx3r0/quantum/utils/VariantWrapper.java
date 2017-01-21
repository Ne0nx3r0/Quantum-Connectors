package com.ne0nx3r0.quantum.utils;

import org.bukkit.block.Block;

/**
 * Created by Yannick on 21.01.2017.
 */
public class VariantWrapper {


    public static BlockVariant getWrapperFromBlock(Block block) {


        switch (block.getType()) {

            case WOOL:
                return new WoolBlockVariant(block.getState());


            default:
                return null;

        }
    }
}
