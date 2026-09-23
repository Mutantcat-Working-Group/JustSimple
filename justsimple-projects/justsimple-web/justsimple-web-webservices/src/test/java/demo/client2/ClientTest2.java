package demo.client2;


import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.annotation.Managed;
import org.mutantcat.justsimple.core.bean.LifecycleBean;
import org.mutantcat.justsimple.web.webservices.WebServiceReference;

import javax.jws.WebMethod;
import javax.jws.WebService;

public class ClientTest2 {
    public static void main(String[] args) {
        JustSimple.start(ClientTest2.class, args, app -> app.enableHttp(false));
    }

    @Managed
    public static class DemoCom implements LifecycleBean {
        @WebServiceReference("http://localhost:8080/ws/HelloService")
        private HelloService helloService;

        @Override
        public void start() throws Throwable {
            System.out.println("rst::" + helloService.hello("noear"));
        }
    }

    @WebService(serviceName = "HelloService", targetNamespace = "http://demo.justsimple.io")
    public interface HelloService {
        @WebMethod
        String hello(String name);
    }
}
