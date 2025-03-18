package org.mutantcat.justsimple.scanner;

import org.mutantcat.justsimple.annotation.JustSimple;

public class StarterApplicationScanner {
    public static String scan(Class<?> clazz) {
        // 扫描启动类上的 @JustSimple 注解获得包名
        JustSimple justSimple = clazz.getAnnotation(JustSimple.class);
        if (justSimple == null) {
            throw new RuntimeException("启动类上缺少 @JustSimple 注解");
        }
        // 获得注解中指定的包名
        String packageName = justSimple.packageName();
        // 如果为空串 则使用所在包为基础包
        if ("".equals(packageName)) {
            packageName = clazz.getPackage().getName();
        }
        return packageName;
    }
}
