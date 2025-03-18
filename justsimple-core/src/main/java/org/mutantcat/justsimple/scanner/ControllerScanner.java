package org.mutantcat.justsimple.scanner;

import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Handler;
import org.mutantcat.justsimple.annotation.Instance;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    // 扫描带有 @Controller 和 @Handler 注解的类和方法
    public static Map<String, Method> scanHandlers(String packageName) throws Exception {
        Map<String, Method> handlerMap = new HashMap<>();
        Set<Class<?>> classes = PackageScanner.scanPackage(packageName);

        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Handler.class)) {
                        Handler handler = method.getAnnotation(Handler.class); // 获得方法上的 @Handler 注解
                        handlerMap.put(handler.path(), method); // 将路径和方法自身存入映射
                    }
                }
            }
        }
        return handlerMap;
    }

    // 同时有 @Controller 和 @Instance 注解的类 为单例 将Handler中定义的路径 和 Instance中的实例名称进行映射
    public static Map<String, String> scanSingletonHandlers(String packageName) throws Exception {
        Map<String, String> handlerMap = new HashMap<>();
        Set<Class<?>> classes = PackageScanner.scanPackage(packageName);

        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                if (clazz.isAnnotationPresent(Instance.class)) {
                    Instance instance = clazz.getAnnotation(Instance.class);
                    for (Method method : clazz.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(Handler.class)) {
                            Handler handler = method.getAnnotation(Handler.class); // 获得方法上的 @Handler 注解
                            // 如果Instance中name属性为空 则使用类名作为name
                            if ("".equals(instance.name())) {
                                handlerMap.put(handler.path(), clazz.getName()); // 将路径和类名存入映射
                            } else {
                                handlerMap.put(handler.path(), instance.name()); // 将路径和方法自身存入映射
                            }
                        }
                    }
                }
            }
        }

        return handlerMap;
    }
}