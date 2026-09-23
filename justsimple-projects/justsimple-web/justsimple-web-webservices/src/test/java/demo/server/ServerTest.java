package demo.server;


import org.mutantcat.justsimple.JustSimple;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

public class ServerTest {
    public static void main(String[] args) {
        JustSimple.start(ServerTest.class, args);
    }

    @BindingType(SOAPBinding.SOAP12HTTP_BINDING)
    @WebService(serviceName = "HelloService", targetNamespace = "http://demo.justsimple.io")
    public static class HelloServiceImpl {
        public String hello(String name) {
            return "hello " + name;
        }
    }
}
