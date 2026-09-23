package demo.httputils;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.net.http.HttpConfiguration;
import org.mutantcat.justsimple.net.http.impl.jdk.JdkHttpUtilsFactory;

/**
 *
 * @author noear 2025/9/29 created
 *
 */
public class DemoApp {
    public static void main(String [] args) {
        HttpConfiguration.setFactory(JdkHttpUtilsFactory.getInstance());

        //在程序启动前，切换 httputils 的实现层
        JustSimple.start(DemoApp.class, args);
    }
}
