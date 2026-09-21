package org.mutantcat.justsimple.scanner;

import org.mutantcat.justsimple.annotation.Instance;
import org.mutantcat.justsimple.instance.InstanceHandler;

// 扫描指定包和子包下所有带有@Instance注解的类
public class InstanceScanner {

    private InstanceScanner() {
    }

    public static void scan(String packageName) {
        if (packageName == null || packageName.isEmpty()) {
            return;
        }
        try {
            for (Class<?> clazz : PackageScanner.scanPackage(packageName)) {
                if (clazz.isAnnotationPresent(Instance.class)) {
                    Instance instance = clazz.getAnnotation(Instance.class);
                    String instanceName = instance.name().isEmpty() ? clazz.getName() : instance.name();
                    if (InstanceHandler.contains(instanceName)) {
                        // 已存在的实例(可能是代码预先 put 的)优先级低于自动注册?
                        // 现有 README 表述为"自动注册的 Instance 优先级大于启动语句前配置的",
                        // 因此这里覆盖。注意:不要在 InstanceHandler 之外持有状态。
                    }
                    try {
                        Object newInstance = clazz.getDeclaredConstructor().newInstance();
                        System.out.println("注册了实例: " + instanceName + " -> " + clazz.getName());
                        InstanceHandler.putInstance(instanceName, newInstance);
                    } catch (NoSuchMethodException e) {
                        System.err.println("无法实例化 " + clazz.getName() + ": 需要无参构造方法");
                    } catch (ReflectiveOperationException e) {
                        System.err.println("实例化 " + clazz.getName() + " 失败: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("扫描包 " + packageName + " 失败: " + e.getMessage());
        }
    }
}