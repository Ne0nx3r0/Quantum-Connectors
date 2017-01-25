package com.ne0nx3r0.quantum.impl.utils;

import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ysl3000 on 19.01.17.
 */
public class Normalizer {

    public final static Replacer<Enum, String> NORMALIZER = new MaterialNormalizer();
    public final static Replacer<Material, String> MATERIAL_NAME = new MaterialName();

    public static Set<String> normalizeEnumNames(Set<? extends Enum> enums, Replacer<Enum, String> replacer) {
        Set<String> names = new HashSet<>();
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
