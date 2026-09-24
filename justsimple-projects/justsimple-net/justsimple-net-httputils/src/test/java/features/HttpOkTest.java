package features;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.net.http.HttpUtils;
import org.mutantcat.justsimple.net.http.impl.okhttp.OkHttpUtilsFactory;

/**
 * @author noear 2024/10/6 created
 */
public class HttpOkTest {
    static final String url = "http://solon.noear.org";
    static final String url404 = "http://solon.noear.org/_test/_demo";

    private static LocalHttpServer server;

    @BeforeAll
    public static void setup() throws Exception {
        server = new LocalHttpServer();
        //远端故意拖 3 秒再响应，配合下面的 1 秒超时必定触发读超时
        server.setDelayMs(3_000);
    }

    @AfterAll
    public static void tearDown() {
        server.close();
    }

    public static HttpUtils http(String url){
        return OkHttpUtilsFactory.getInstance().http(url);
    }

    @Test
    public void get() throws Exception {
        assert http(url)
                .enablePrintln(true)
                .execAsCode("GET") == 200;
    }

    @Test
    public void get_404() throws Exception {
        assert http(url404)
                .enablePrintln(true)
                .execAsCode("GET") == 404;
    }

    @Test
    public void post() throws Exception {
        assert http(url).data("user", "noear")
                .enablePrintln(true)
                .execAsCode("POST") == 200;

        assert http(url).bodyOfJson("{\"user\":\"noear\"}")
                .enablePrintln(true)
                .execAsCode("POST") == 200;

        assert http(url).body("{\"user\":\"noear\"}".getBytes())
                .enablePrintln(true)
                .execAsCode("POST") == 200;
    }

    @Test
    public void post_404() throws Exception {
        assert http(url404).data("user", "noear")
                .enablePrintln(true)
                .execAsCode("POST") == 404;

        assert http(url404).bodyOfJson("{\"user\":\"noear\"}")
                .enablePrintln(true)
                .execAsCode("POST") == 404;

        assert http(url404).body("{\"user\":\"noear\"}".getBytes())
                .enablePrintln(true)
                .execAsCode("POST") == 404;
    }

    @Test
    public void http_timeout() throws Exception {
        long startTimeMs = System.currentTimeMillis();

        // 原用例请求 https://www.google.com/ 并期望 1 秒超时，而 timeout(int) 的单位是秒。
        // 境外 CI 网络上该站点 1 秒内就能返回，"一定抛异常"的断言必然失败；只有在访问
        // Google 本身就很慢的环境才碰巧通过。改为打本机 /slow 端点，离线且结果确定。
        Assertions.assertThrows(Exception.class, () -> {
            http(server.url("/slow"))
                    .timeout(1)
                    .get();
        });

        long spanTimeMs = System.currentTimeMillis() - startTimeMs;
        System.out.println(spanTimeMs);

        assert spanTimeMs < 3_000;
    }
}
