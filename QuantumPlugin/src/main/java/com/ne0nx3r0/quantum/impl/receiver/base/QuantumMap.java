package com.ne0nx3r0.quantum.impl.receiver.base;

import org.bukkit.Material;

import java.util.*;

public class QuantumMap<T> {

    private final Map<String, Class<? extends T>> stringMap = new HashMap<>();
    private final Map<Material, List<Class<? extends T>>> materialMap = new HashMap<>();
    private final Map<Class<? extends T>, String> classMap = new HashMap<>();


    public Class<? extends T> put(String key, Class<? extends T> clazz) {
        return stringMap.put(key, clazz);
    }

    public Class<? extends T> put(Material material, Class<? extends T> receiver) {
        List<Class<? extends T>> abstClassList = materialMap.get(material);

        if (abstClassList == null) {
            abstClassList = new ArrayList<>();
            materialMap.put(material, abstClassList);
        }

        return abstClassList.add(receiver) ? receiver : null;
    }

    public Class<? extends T> remove(String key) {
        return stringMap.remove(key);
    }

    public String remove(Class<? extends T> clazz) {
        return this.classMap.remove(clazz);
    }

    public boolean remove(List<Material> materialList, Class<? extends T> clazz) {
        boolean success = true;
        for (Material material : materialList) {
            if (!materialMap.get(material).remove(clazz)) {
                success = false;
            }
        }
        return success;
    }


    public Set<Material> getMaterials() {
        return new HashSet<>(materialMap.keySet());
    }

    public Set<String> getStrings() {
        return new HashSet<>(stringMap.keySet());
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

    public void clear() {
        stringMap.clear();
        materialMap.clear();
        classMap.clear();
    }
}
