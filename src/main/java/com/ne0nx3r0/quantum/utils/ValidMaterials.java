package com.ne0nx3r0.quantum.utils;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class ValidMaterials {

    public final static List<Material> validSenders = Arrays.asList(
            Material.LEVER,
            Material.REDSTONE_WIRE,
            Material.STONE_BUTTON,
            Material.STONE_PLATE,
            Material.WOOD_PLATE,
            Material.REDSTONE_TORCH_OFF,
            Material.REDSTONE_TORCH_ON,
            Material.REDSTONE_LAMP_OFF,
            Material.REDSTONE_LAMP_ON,
            //Material.DIODE_BLOCK_OFF,
            //Material.DIODE_BLOCK_ON,//TODO: Figure out repeaters as senders
            Material.IRON_DOOR_BLOCK,
            Material.WOODEN_DOOR,
            Material.SPRUCE_DOOR,
            Material.BIRCH_DOOR,
            Material.JUNGLE_DOOR,
            Material.ACACIA_DOOR,
            Material.DARK_OAK_DOOR,
            Material.TRAP_DOOR,
            Material.FENCE_GATE,
            Material.SPRUCE_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.ACACIA_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE,
            Material.CHEST,
            Material.BOOKSHELF,
            Material.BED_BLOCK,
            Material.FURNACE,
            Material.WOOD_BUTTON,
            //Material.REDSTONE_COMPARATOR,
            //Material.REDSTONE_COMPARATOR_ON,
            //Material.REDSTONE_COMPARATOR_OFF,
            //Material.DAYLIGHT_DETECTOR,
            Material.DETECTOR_RAIL,
            Material.IRON_PLATE,
            Material.GOLD_PLATE//,
            //Material.POWERED_RAIL,//TODO: Figure out powered rail as sender
            //Material.PISTON_BASE,
            //Material.PISTON_STICKY_BASE,//TODO: Pistons as senders
    );
    public final static List<Material> validReceivers = Arrays.asList(
            Material.LEVER,
            Material.IRON_DOOR_BLOCK,
            Material.WOODEN_DOOR,
            Material.SPRUCE_DOOR,
            Material.BIRCH_DOOR,
            Material.JUNGLE_DOOR,
            Material.ACACIA_DOOR,
            Material.DARK_OAK_DOOR,
            Material.TRAP_DOOR,
            Material.POWERED_RAIL,
            Material.FENCE_GATE,
            Material.SPRUCE_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.ACACIA_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE,
            Material.REDSTONE_LAMP_OFF,
            Material.REDSTONE_LAMP_ON//,
            //Material.REDSTONE_TORCH_OFF,
            //Material.REDSTONE_TORCH_ON,
            //Material.PISTON_BASE,
            //Material.PISTON_STICKY_BASE,//TODO: Pistons as receivers
    );

    public final static List<Material> OPENABLE = Arrays.asList(Material.IRON_DOOR_BLOCK,
            Material.WOODEN_DOOR,
            Material.SPRUCE_DOOR,
            Material.BIRCH_DOOR,
            Material.JUNGLE_DOOR,
            Material.ACACIA_DOOR,
            Material.DARK_OAK_DOOR,
            Material.TRAP_DOOR,
            Material.POWERED_RAIL,
            Material.FENCE_GATE,
            Material.SPRUCE_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.ACACIA_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE);

    public final static List<Material> LAMP = Arrays.asList(Material.REDSTONE_LAMP_OFF, Material.REDSTONE_LAMP_ON);
    public final static List<Material> LEVER = Arrays.asList(Material.LEVER);
    public final static List<Material> RAIL = Arrays.asList(Material.POWERED_RAIL);
    public final static List<Material> PISTON = Arrays.asList(Material.PISTON_BASE);


}
