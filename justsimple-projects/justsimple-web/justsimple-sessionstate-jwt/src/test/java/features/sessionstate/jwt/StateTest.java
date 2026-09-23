package features.sessionstate.jwt;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.net.http.HttpResponse;
import org.mutantcat.justsimple.test.HttpTester;
import org.mutantcat.justsimple.test.JustSimpleTest;

/**
 * @author noear 2024/11/29 created
 */

@JustSimpleTest(value = App.class, properties = {"user.home=target/test-home"})
public class StateTest extends HttpTester {
    @Test
    public void test() throws Exception {
        String sid = "aaaaaaaaaabbbbbbbbbbccccccccccdddddddddd";

        HttpResponse resp = path("/").cookie("JUSTSIMPLEID", sid).exec("GET");
        StateDo rst = resp.bodyAsBean(StateDo.class);
        String token = resp.cookie("TOKEN");
        resp.close();
        System.out.println(rst);

        Thread.sleep(1000);

        StateDo rst2 = path("/")
                .cookie("TOKEN", token)
                .cookie("JUSTSIMPLEID", sid)
                .getAs(StateDo.class);

        System.out.println(rst2);

        assert rst2.t2 > rst2.t1;
        assert rst.t1 == rst2.t1;
    }
}
