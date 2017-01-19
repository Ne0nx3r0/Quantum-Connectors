package com.ne0nx3r0.quantum.utils;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ysl3000 on 19.01.17.
 */
public class Normalizer {

    public final static Replacer<Enum, String> NORMALIZER = new MaterialNormalizer();
    public final static Replacer<Material, String> MATERIAL_NAME = new MaterialName();

    public static List<String> normalizeEnumNames(List<? extends Enum> enums, Replacer<Enum, String> replacer) {
        List<String> names = new ArrayList<>();
        for (Enum material : enums)
            names.add(replacer.replace(material));
        return names;
    }

    private static class MaterialNormalizer implements Replacer<Enum, String> {
        @Override
        public String replace(Enum in) {
            return in.name().toLowerCase().replace("_", " ");
        }
    }

    private static class MaterialName implements Replacer<Material, String> {
        @Override
        public String replace(Material in) {
            return in.name();
        }
    }


}
