package org.mutantcat.justsimple.scanner;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;

public class PackageScanner {
    public static Set<Class<?>> scanPackage(String packageName) throws Exception {
        Set<Class<?>> classes = new HashSet<>();
        String path = packageName.replace('.', '/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);

        if (resource == null) {
            return classes; // 如果路径为空，直接返回
        }

        String protocol = resource.getProtocol();
        if ("file".equals(protocol)) {
            // 扫描本地文件系统中的类
            File directory = new File(resource.getFile());
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
        } else if ("jar".equals(protocol)) {
            // 扫描 JAR 文件中的类
            String jarPath = resource.getPath().split("!")[0].substring("file:".length());
            try (JarFile jarFile = new JarFile(new File(jarPath))) {
                jarFile.stream()
                        .filter(entry -> entry.getName().endsWith(".class") && entry.getName().startsWith(path))
                        .forEach(entry -> {
                            String className = entry.getName().replace('/', '.').replace(".class", "");
                            try {
                                classes.add(Class.forName(className));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        });
            }
        }

        return classes;
    }

    public static List<String> getSubPackage(String packageName) throws Exception {
        List<String> subPackageList = new ArrayList<>();
        Set<String> uniquePackages = new HashSet<>(); // 防止重复

        String path = packageName.replace('.', '/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);

        if (resource != null) {
            String protocol = resource.getProtocol();
            if ("file".equals(protocol)) {
                File directory = new File(resource.getPath());
                if (directory.exists()) {
                    for (File file : directory.listFiles()) {
                        if (file.isDirectory()) {
                            uniquePackages.add(packageName + "." + file.getName());
                        }
                    }
                }
            } else if ("jar".equals(protocol)) {
                String jarPath = resource.getPath().split("!")[0].substring("file:".length());
                try (JarFile jarFile = new JarFile(new File(jarPath))) {
                    jarFile.stream()
                            .filter(entry -> entry.getName().startsWith(path) && entry.getName().endsWith("/"))
                            .forEach(entry -> {
                                String subPackage = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 1);
                                uniquePackages.add(subPackage);
                            });
                }
            }
        }

        subPackageList.addAll(uniquePackages);
        return subPackageList;
    }
}
