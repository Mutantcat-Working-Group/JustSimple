package features;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.nami.annotation.NamiClient;
import org.mutantcat.justsimple.core.handle.UploadedFile;
import org.mutantcat.justsimple.core.util.MimeType;
import org.mutantcat.justsimple.test.JustSimpleTest;
import webapp.App;
import webapp.demo5_rpc.HelloService;
import webapp.demo5_rpc.Namiform;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author noear 2025/5/7 created
 */
@JustSimpleTest(App.class)
public class NamiAndHttpTest {
    //直接指定服务端地址
    @NamiClient(url = "http://localhost:8080/demo5/hello/")
    HelloService helloService;

    @Test
    public void hello() {
        assert helloService.hello("world", "1", "a").contains("world:1:a");
        assert helloService.hello("justsimple", "2", "b").contains("justsimple:2:b");
    }

    @Test
    public void test01() {
        List<String> ids = new ArrayList<>();
        ids.add("a");
        ids.add("b");
        assert helloService.test01(ids).equals("a,b");
    }

    @Test
    public void test02() throws IOException {
        UploadedFile file = new UploadedFile(MimeType.TEXT_PLAIN_VALUE,
                new ByteArrayInputStream("hello".getBytes()),
                "demo1.txt");

        assert helloService.test02(file).equals("demo1.txt");
    }

    @Test
    public void test03() throws IOException {
        assert helloService.test03().equals("test03");
    }

    @Test
    public void test04() throws IOException {
        assert helloService.test04("test04").equals("test04");
    }

    @Test
    public void test05() throws IOException {
        assert helloService.test05(1, "test05").equals("1:\"test05\"");
    }

    @Test
    public void test06() throws IOException {
        assert helloService.test06("a").equals("test06:a:application/x-www-form-urlencoded");
    }

    @Test
    public void test07() throws IOException {
        Namiform namiform = new Namiform();
        namiform.setName("noear");
        namiform.setAge(18);
        namiform.setSex("male");

        String s = helloService.test07(namiform);
        System.out.println(s);
    }
}
