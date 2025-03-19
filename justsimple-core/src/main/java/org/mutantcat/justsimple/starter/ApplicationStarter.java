package org.mutantcat.justsimple.starter;

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

    public void start(Class<?> clazz, String[] args) {
        System.out.println("JustSimple启动中...");
        // 扫描启动类上的 @JustSimple 注解获得包名
        String scan = StarterApplicationScanner.scan(clazz);
        System.out.println("扫描到基础包：" + scan);
        // 初始化配置
        InstanceHandler.putInstance("just_simple_config", new Config());
        // 扫描基础包下的所有Instance类之后进行注册
        InstanceScanner.scan(scan);
        Map<String, Method> handlerMap = new HashMap<>();
        Map<String, String> singletonMap = new HashMap<>();
        try {
            handlerMap = ControllerScanner.scanHandlers(scan+".controller");
            singletonMap = ControllerScanner.scanSingletonHandlers(scan+".controller");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动 Netty 服务器
        try {
            Config config = (Config) InstanceHandler.getInstance("just_simple_config");
            new NettyWithController((Integer) config.getConfig().get("port"), handlerMap,singletonMap).start();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
