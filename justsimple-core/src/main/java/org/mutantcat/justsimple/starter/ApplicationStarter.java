package org.mutantcat.justsimple.starter;

import io.netty.handler.codec.http.cors.CorsConfig;
import org.mutantcat.justsimple.config.Config;
import org.mutantcat.justsimple.instance.InstanceHandler;
import org.mutantcat.justsimple.scanner.ControllerScanner;
import org.mutantcat.justsimple.scanner.InstanceScanner;
import org.mutantcat.justsimple.scanner.StarterApplicationScanner;
import org.mutantcat.justsimple.web.NettyWithController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ApplicationStarter {

    private static final String CONFIG_INSTANCE_NAME = "just_simple_config";

    public void start(Class<?> clazz, String[] args) {
        System.out.println("JustSimple 启动中...");
        // 扫描启动类上的 @JustSimple 注解获得包名
        String scan = StarterApplicationScanner.scan(clazz);
        System.out.println("扫描到基础包: " + scan);
        // 初始化配置
        InstanceHandler.putInstance(CONFIG_INSTANCE_NAME, new Config());
        // 扫描库中的所有 Instance 类进行注册
        InstanceScanner.scan("org.mutantcat.justsimple");
        InstanceScanner.scan("org.mutantcat.justsimple.dao.mybatis");
        // 扫描基础包下的所有 Instance 类之后进行注册
        InstanceScanner.scan(scan);

        Map<String, Method> handlerMap = new HashMap<>();
        Map<String, String> singletonMap = new HashMap<>();
        try {
            handlerMap = ControllerScanner.scanHandlers(scan + ".controller");
            singletonMap = ControllerScanner.scanSingletonHandlers(scan + ".controller");
        } catch (Exception e) {
            throw new RuntimeException("扫描 controller 包失败", e);
        }

        // 使用 args 提供的配置覆盖默认配置
        // 格式: corsConfig:CorsConfigInstanceName 或 port:portConfigName
        if (args != null) {
            for (String arg : args) {
                if (arg == null) {
                    continue;
                }
                String[] split = arg.trim().split(":", -1);
                if (split.length != 2) {
                    continue;
                }
                String key = split[0].trim();
                String instanceName = split[1].trim();
                if (instanceName.isEmpty()) {
                    continue;
                }
                Object referenced = InstanceHandler.getInstance(instanceName);
                if (referenced == null) {
                    System.err.println("args 引用了未注册的实例: " + instanceName);
                    continue;
                }
                Config config = InstanceHandler.getInstance(CONFIG_INSTANCE_NAME);
                if (config == null) {
                    System.err.println("Config 实例丢失,跳过参数: " + arg);
                    continue;
                }
                if ("corsConfig".equals(key)) {
                    if (referenced instanceof CorsConfig) {
                        config.setCorsConfig((CorsConfig) referenced);
                    } else {
                        System.err.println("corsConfig 实例类型错误,期望 CorsConfig,实际 " + referenced.getClass().getName());
                    }
                } else if ("port".equals(key)) {
                    config.setConfig("port", referenced);
                }
            }
        }

        // 启动 Netty 服务器
        try {
            Config config = InstanceHandler.getInstance(CONFIG_INSTANCE_NAME);
            Object portObj = config.getConfig().get("port");
            int port = portObj instanceof Integer ? (Integer) portObj : Integer.parseInt(String.valueOf(portObj));
            new NettyWithController(port, handlerMap, singletonMap).start();
        } catch (Exception e) {
            throw new RuntimeException("启动 Netty 服务器失败", e);
        }
    }
}