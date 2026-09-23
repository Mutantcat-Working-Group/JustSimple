package demo;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.core.BeanWrap;
import org.mutantcat.justsimple.proxy.BeanProxy;
import org.mutantcat.justsimple.proxy.ProxyUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *
 * @author noear 2025/10/27 created
 *
 */
public class ProxyUtilDemo {
    //代理
    public static Object doProxy(Object bean) {
        Object tmp = ProxyUtil.newProxyInstance(bean.getClass(), (proxy, method, args) -> {
            //执行之前
            try {
                return method.invoke(bean, args);
            } finally {
                //执行之后
            }
        });
        return tmp;
    }

    public static void main(String[] args) {
        JustSimple.start(ProxyUtilDemo.class, args, app->{
            app.context().getWrapAsync(ProxyUtilDemo.class, bw->{
                bw.proxySet((bw1, bean) -> doProxy(bean));
            });
        });
    }
}
