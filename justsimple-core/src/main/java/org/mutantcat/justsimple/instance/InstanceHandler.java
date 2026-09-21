package org.mutantcat.justsimple.instance;

import java.util.concurrent.ConcurrentHashMap;

// 单例实例存取
public class InstanceHandler {
    // 用于存储实例的哈希表
    private static final ConcurrentHashMap<String, Object> INSTANCES = new ConcurrentHashMap<>();

    private InstanceHandler() {
    }

    // 获取实例
    @SuppressWarnings("unchecked")
    public static <T> T getInstance(String name) {
        return (T) INSTANCES.get(name);
    }

    // 存储实例
    public static void putInstance(String name, Object instance) {
        INSTANCES.put(name, instance);
    }

    // 是否存在实例
    public static boolean contains(String name) {
        return INSTANCES.containsKey(name);
    }

    // 移除实例
    public static void removeInstance(String name) {
        INSTANCES.remove(name);
    }
}