package com.ne0nx3r0.quantum.receiver.base;

import org.bukkit.DyeColor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yannick on 21.01.2017.
 */
public enum ReceiverState {
    S0, S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11, S12, S13, S14, S15;


    private static final Map<String, ReceiverState> BY_NAME = new HashMap<>();
    private static final Map<ReceiverState, DyeColor> DYECOLOR = new HashMap<>();
    private static final Map<DyeColor, ReceiverState> DYE_COLOR_RECEIVER_STATE_MAP = new HashMap<>();

    static {
        for (ReceiverState state : values()) {
            BY_NAME.put(state.name(), state);
            DYECOLOR.put(state, DyeColor.values()[state.ordinal()]);
            DYE_COLOR_RECEIVER_STATE_MAP.put(state.getDyColor(), state);
        }


    }

    public static ReceiverState getByName(String name) {
        return BY_NAME.get(name);
    }

    public static ReceiverState getByColor(DyeColor dyeColor) {
        return DYE_COLOR_RECEIVER_STATE_MAP.get(dyeColor);
    }

    public DyeColor getDyColor() {
        return DYECOLOR.get(this);
    }

    public ReceiverState getOpposite() {
        return values()[(values().length - 1) - ordinal()];
    }

}
