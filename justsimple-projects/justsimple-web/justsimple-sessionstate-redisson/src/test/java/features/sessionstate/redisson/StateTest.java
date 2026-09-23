package features.sessionstate.redisson;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.test.HttpTester;
import org.mutantcat.justsimple.test.JustSimpleTest;

/**
 * @author noear 2024/11/29 created
 */

@JustSimpleTest(App.class)
public class StateTest extends HttpTester {
    @Test
    public void test() throws Exception {
        String sid = "aaaaaaaaaabbbbbbbbbbccccccccccdddddddddd";

        StateDo rst = path("/").cookie("JUSTSIMPLEID", sid).getAs(StateDo.class);
        System.out.println(rst);

        Thread.sleep(1000);

        StateDo rst2 = path("/").cookie("JUSTSIMPLEID", sid).getAs(StateDo.class);
        System.out.println(rst2);

        assert rst2.t2 > rst2.t1;
        assert rst.t1 == rst2.t1;
    }
}
