package org.mutantcat.justsimple.scanner;

import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Handler;
import org.mutantcat.justsimple.annotation.Instance;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private ControllerScanner() {
    }

    // 扫描带有 @Controller 和 @Handler 注解的类和方法
    public static Map<String, Method> scanHandlers(String packageName) throws Exception {
        Map<String, Method> handlerMap = new HashMap<>();
        for (Class<?> clazz : PackageScanner.scanPackage(packageName)) {
            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Handler.class)) {
                    Handler handler = method.getAnnotation(Handler.class);
                    handlerMap.put(handler.path(), method);
                }
            }
        }
        return handlerMap;
    }

    // 同时有 @Controller 和 @Instance 注解的类 为单例 将Handler中定义的路径 和 Instance中的实例名称进行映射
    public static Map<String, String> scanSingletonHandlers(String packageName) throws Exception {
        Map<String, String> handlerMap = new HashMap<>();
        for (Class<?> clazz : PackageScanner.scanPackage(packageName)) {
            if (!clazz.isAnnotationPresent(Controller.class) || !clazz.isAnnotationPresent(Instance.class)) {
                continue;
            }
            Instance instance = clazz.getAnnotation(Instance.class);
            String instanceName = "".equals(instance.name()) ? clazz.getName() : instance.name();
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Handler.class)) {
                    Handler handler = method.getAnnotation(Handler.class);
                    handlerMap.put(handler.path(), instanceName);
                }
            }
        }
        return handlerMap;
    }
}