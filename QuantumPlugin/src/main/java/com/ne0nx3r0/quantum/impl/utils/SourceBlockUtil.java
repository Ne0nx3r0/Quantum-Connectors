package com.ne0nx3r0.quantum.impl.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.material.Bed;
import org.bukkit.material.Door;
import org.bukkit.material.MaterialData;

/**
 * Created by Yannick on 21.01.2017.
 */
public class SourceBlockUtil {

    public Location getSourceBlock(Location location) {

        Block block = location.getBlock();
        MaterialData materialData = block.getState().getData();
        if (materialData instanceof Door) {
            Door door = (Door) materialData;
            if (door.isTopHalf()) {
                return location.add(0, -1, 0);
            }
        } else if (materialData instanceof Bed) {
            Bed bed = (Bed) materialData;
            if (bed.isHeadOfBed()) {
                return block.getRelative(bed.getFacing().getOppositeFace()).getLocation();
            }
        } else if (block.getState() instanceof Chest) {
            Chest chest = (Chest) block.getState();

            Inventory inventory = chest.getBlockInventory();

            if (inventory instanceof DoubleChestInventory) {
                return ((DoubleChestInventory) inventory).getLeftSide().getLocation();
            }
            return ((Chest) inventory.getHolder()).getLocation();

        }
        return location;
    }


}
