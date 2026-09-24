package features.sessionstate.redisson;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.mutantcat.justsimple.test.HttpTester;
import org.mutantcat.justsimple.test.JustSimpleTest;

/**
 * @author noear 2024/11/29 created
 */

/**
 * 这些用例要连真实的 Redis（app.yml 里配的是 localhost:6379）。
 * CI 环境没有这个服务，端口不通时整个类跳过；本机起了服务就会真正执行。
 */
@EnabledIf("isServiceAvailable")
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

    /**
     * 仅当本机 Redis 端口可连接时才启用，避免 CI 上因缺少服务而误报失败。
     */
    static boolean isServiceAvailable() {
        try (java.net.Socket socket = new java.net.Socket("127.0.0.1", 6379)) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}