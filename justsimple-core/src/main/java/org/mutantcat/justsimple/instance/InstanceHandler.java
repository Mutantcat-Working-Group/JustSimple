package org.mutantcat.justsimple.instance;

import java.util.concurrent.ConcurrentHashMap;

// 单例实例存取
public class InstanceHandler {
    // 用于存储实例的哈希表
    private static final ConcurrentHashMap<String, Object> INSTANCES = new ConcurrentHashMap<>();

    // 获取实例
    public static Object getInstance(String name) {
        return INSTANCES.get(name);
    }

    // 存储实例
    public static void putInstance(String name, Object instance) {
        INSTANCES.put(name, instance);
    }

}
