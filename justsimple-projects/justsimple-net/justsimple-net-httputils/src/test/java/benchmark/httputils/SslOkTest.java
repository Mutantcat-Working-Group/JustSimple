package benchmark.httputils;

import features.LocalHttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.net.http.HttpUtils;
import org.mutantcat.justsimple.net.http.impl.okhttp.OkHttpUtilsFactory;

/**
 * @author noear 2024/10/4 created
 */
public class SslOkTest {
    static HttpUtils http(String url) {
        return OkHttpUtilsFactory.getInstance().http(url);
    }

    private static LocalHttpServer server;

    @BeforeAll
    public static void setup() throws Exception {
        // 原用例要手工启动演示应用才能连通，这里本地起服务，保证单测可独立运行
        server = new LocalHttpServer();
    }

    @AfterAll
    public static void tearDown() {
        server.close();
    }

    //1000a=> 1549, 1494, 1481, 1235, 1213, 1242; 1143, 1123
    //1000b=> 1188, 1173, 1131, 1137, 1207
    @Test
    public void performance() {
        String url = server.url("/hello?name=solon");

        for (int i = 0; i < 10; i++) {
            http(url).get();
        }


        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            http(url).get();
        }
        long timespan = System.currentTimeMillis() - start;
        System.out.println("timespan: " + timespan);
    }
}
