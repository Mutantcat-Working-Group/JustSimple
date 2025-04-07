package org.mutantcat.justsimple.scanner;

import org.mutantcat.justsimple.annotation.Instance;
import org.mutantcat.justsimple.instance.InstanceHandler;

import java.util.List;

// 扫描指定包和子包下所有带有@Instance注解的类
public class InstanceScanner {
    public static void scan(String packageName) {
        // 获取所有子包列表并递归扫描
        try {
            for (Class<?> clazz : PackageScanner.scanPackage(packageName)) {
                if (clazz.isAnnotationPresent(Instance.class)) {
                    Instance instance = clazz.getAnnotation(Instance.class);
                    String instanceName = instance.name().isEmpty() ? clazz.getName() : instance.name();
                    System.out.println("注册了实例：" + instanceName + " -> " + clazz.getName());
                    InstanceHandler.putInstance(instanceName, clazz.newInstance());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

