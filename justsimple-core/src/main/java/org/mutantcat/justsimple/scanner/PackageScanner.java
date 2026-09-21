package org.mutantcat.justsimple.scanner;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PackageScanner {

    private static final String CLASS_SUFFIX = ".class";

    private PackageScanner() {
    }

    public static Set<Class<?>> scanPackage(String packageName) throws Exception {
        Set<Class<?>> classes = new HashSet<>();
        if (packageName == null || packageName.isEmpty()) {
            return classes;
        }
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);
        Set<String> seen = new HashSet<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();
            if ("file".equals(protocol)) {
                File directory = new File(URLDecoder.decode(resource.getFile(), StandardCharsets.UTF_8.name()));
                scanDirectory(packageName, directory, classes);
            } else if ("jar".equals(protocol)) {
                scanJar(packageName, path, resource, classes, seen);
            }
        }
        return classes;
    }

    private static void scanDirectory(String packageName, File directory, Set<Class<?>> classes) {
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(packageName + "." + file.getName(), file, classes);
            } else if (file.getName().endsWith(CLASS_SUFFIX)) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - CLASS_SUFFIX.length());
                loadClass(className, classes);
            }
        }
    }

    private static void scanJar(String packageName, String path, URL resource,
                                Set<Class<?>> classes, Set<String> seen) throws IOException {
        String fullPath = resource.getPath();
        int separator = fullPath.indexOf("!/");
        if (separator < 0) {
            return;
        }
        String jarSpec = fullPath.substring(0, separator);
        try {
            URI uri = new URI(jarSpec);
            File jarFile = new File(uri.getSchemeSpecificPart());
            if (!jarFile.isFile()) {
                return;
            }
            try (JarFile jar = new JarFile(jarFile)) {
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (!name.endsWith(CLASS_SUFFIX) || !name.startsWith(path)) {
                        continue;
                    }
                    String className = name.substring(0, name.length() - CLASS_SUFFIX.length()).replace('/', '.');
                    if (seen.add(className)) {
                        loadClass(className, classes);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("扫描 jar 包失败: " + packageName + " (" + e.getMessage() + ")");
        }
    }

    private static void loadClass(String className, Set<Class<?>> classes) {
        try {
            Class<?> clazz = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
            classes.add(clazz);
        } catch (Throwable e) {
            // 跳过无法加载的类(如依赖缺失、模块未导出等)
        }
    }

    public static List<String> getSubPackage(String packageName) throws Exception {
        List<String> subPackageList = new ArrayList<>();
        if (packageName == null || packageName.isEmpty()) {
            return subPackageList;
        }
        Set<String> uniquePackages = new HashSet<>();
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();
            if ("file".equals(protocol)) {
                File directory = new File(URLDecoder.decode(resource.getFile(), StandardCharsets.UTF_8.name()));
                if (directory.exists() && directory.isDirectory()) {
                    File[] files = directory.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            if (file.isDirectory()) {
                                uniquePackages.add(packageName + "." + file.getName());
                            }
                        }
                    }
                }
            } else if ("jar".equals(protocol)) {
                String fullPath = resource.getPath();
                int separator = fullPath.indexOf("!/");
                if (separator < 0) {
                    continue;
                }
                String jarSpec = fullPath.substring(0, separator);
                try {
                    URI uri = new URI(jarSpec);
                    File jarFile = new File(uri.getSchemeSpecificPart());
                    if (!jarFile.isFile()) {
                        continue;
                    }
                    try (JarFile jar = new JarFile(jarFile)) {
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.startsWith(path) && name.endsWith("/")) {
                                String subPackage = name.replace('/', '.').substring(0, name.length() - 1);
                                uniquePackages.add(subPackage);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("扫描子包失败: " + packageName + " (" + e.getMessage() + ")");
                }
            }
        }
        subPackageList.addAll(uniquePackages);
        return subPackageList;
    }
}