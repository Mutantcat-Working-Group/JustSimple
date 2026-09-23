package features;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.net.http.HttpResponse;
import org.mutantcat.justsimple.net.http.HttpUtilsFactory;
import org.mutantcat.justsimple.net.http.impl.okhttp.OkHttpUtilsFactory;

import java.util.concurrent.CompletableFuture;

/**
 * @author noear 2024/10/6 created
 */
public class AsyncOkFactoryTest {
    static HttpUtilsFactory httpUtils = new OkHttpUtilsFactory();

    @Test
    public void case11() throws Exception {
        CompletableFuture<HttpResponse> htmlFuture = httpUtils.http("https://solon.noear.org/").execAsync("GET");

        String text = htmlFuture.get().bodyAsString();
        System.out.println(text);

        assert text != null;
        assert text.contains("Solon");
    }

    @Test
    public void case12() throws Exception {
        CompletableFuture<HttpResponse> htmlFuture = httpUtils.http("https://www.bilibili.com/").execAsync("GET");

        String text = htmlFuture.get().bodyAsString();
        System.out.println(text);

        assert text != null;
        assert text.contains("bilibili");
    }
}
