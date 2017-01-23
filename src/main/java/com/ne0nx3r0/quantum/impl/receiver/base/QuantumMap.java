package com.ne0nx3r0.quantum.impl.receiver.base;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuantumMap<T> {

    private final Map<String, Class<? extends T>> stringMap = new HashMap<>();
    private final Map<Material, List<Class<? extends T>>> materialMap = new HashMap<>();
    private final Map<Class<? extends T>, String> classMap = new HashMap<>();


    public Class<? extends T> put(String key, Class<? extends T> receiver) {
        return stringMap.put(key, receiver);
    }

    public Class<? extends T> put(Material material, Class<? extends T> receiver) {
        List<Class<? extends T>> abstClassList = materialMap.get(material);

        if (abstClassList == null) {
            abstClassList = new ArrayList<>();
            materialMap.put(material, abstClassList);
        }

        return abstClassList.add(receiver) ? receiver : null;
    }

    public String put(Class<? extends T> clazz, String type) {
        return classMap.put(clazz, type);
    }

    public List<Class<? extends T>> get(Material type) {
        return materialMap.get(type);
    }

    public Class<? extends T> get(String type) {
        return stringMap.get(type);
    }

    public String get(Class<? extends T> clazz) {
        return classMap.get(clazz);
    }

    public boolean contains(Material material) {
        return materialMap.containsKey(material);
    }

    public boolean contains(Class<? extends T> clazz) {
        return classMap.containsKey(clazz);
    }

    public boolean contains(String type) {
        return stringMap.containsKey(type);
    }


}
