package features;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.net.http.HttpResponse;
import org.mutantcat.justsimple.net.http.HttpUtils;
import org.mutantcat.justsimple.net.http.impl.okhttp.OkHttpUtilsFactory;

import java.util.concurrent.CompletableFuture;

/**
 * @author noear 2024/10/6 created
 */
public class AsyncOkTest {
    static HttpUtils http(String url) {
        return OkHttpUtilsFactory.getInstance().http(url);
    }

    private static LocalHttpServer server;

    @BeforeAll
    public static void setup() throws Exception {
        server = new LocalHttpServer();
    }

    @AfterAll
    public static void tearDown() {
        server.close();
    }

    @Test
    public void case11() throws Exception {
        CompletableFuture<HttpResponse> htmlFuture = http(server.url("/site/Solon")).execAsync("GET");

        String text = htmlFuture.get().bodyAsString();
        System.out.println(text);

        assert text != null;
        assert text.contains("Solon");
    }

    @Test
    public void case12() throws Exception {
        CompletableFuture<HttpResponse> htmlFuture = http(server.url("/site/bilibili")).execAsync("GET");

        String text = htmlFuture.get().bodyAsString();
        System.out.println(text);

        assert text != null;
        assert text.contains("bilibili");
    }
}
