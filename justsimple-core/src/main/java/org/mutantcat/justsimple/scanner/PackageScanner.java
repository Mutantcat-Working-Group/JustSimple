package org.mutantcat.justsimple.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PackageScanner {
    public static Set<Class<?>> scanPackage(String packageName) throws Exception {
        Set<Class<?>> classes = new HashSet<>();
        String path = packageName.replace('.', '/');
        String classPath = Thread.currentThread().getContextClassLoader().getResource(path).getPath();

        File directory = new File(classPath);
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    classes.addAll(scanPackage(packageName + "." + file.getName())); // 递归子包
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + "." + file.getName().replace(".class", "");
                    classes.add(Class.forName(className));
                }
            }
        }
        return classes;
    }

    public static List<String> getSubPackage(String packageName) {
        // 获取子包列表
        List<String> subPackageList = new ArrayList<>();
        String path = packageName.replace('.', '/');
        String classPath = Thread.currentThread().getContextClassLoader().getResource(path).getPath();
        File directory = new File(classPath);
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    subPackageList.add(packageName + "." + file.getName());
                }
            }
        }
        return subPackageList;
    }
}
