package org.mutantcat.justsimple.scanner;

import org.mutantcat.justsimple.annotation.Instance;
import org.mutantcat.justsimple.instance.InstanceHandler;

import java.util.List;

// 扫描指定包和子包下所有带有@Instance注解的类
public class InstanceScanner {
    public static void scan(String packageName) {
        // 获取子包列表
        List<String> subPackageList = PackageScanner.getSubPackage(packageName);
        if (subPackageList.isEmpty()) {
            return;
        }

        // 扫所有子包中的类是否有@Instance注解
        for (String subPackage : subPackageList) {
            try {
                // 扫描子包下所有类
                for (Class<?> clazz : PackageScanner.scanPackage(subPackage)) {
                    // 判断是否有@Instance注解
                    if (clazz.isAnnotationPresent(Instance.class)) {
                        // 如果Instance中name属性为空 则使用类名作为name
                        Instance instance = clazz.getAnnotation(Instance.class);
                        if ("".equals(instance.name())) {
                            System.out.println("注册了实例：" + clazz.getName());
                            InstanceHandler.putInstance(clazz.getName(), clazz.newInstance());
                        } else {
                            System.out.println("注册了实例：" + instance.name());
                            InstanceHandler.putInstance(instance.name(), clazz.newInstance());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 获得所有子包的子包
        for (String subPackage : subPackageList) {
            scan(subPackage);
        }
    }
}
