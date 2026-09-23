package features.jetty.https;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.core.util.MimeType;
import org.mutantcat.justsimple.core.util.MultiMap;
import org.mutantcat.justsimple.core.util.RunUtil;
import org.mutantcat.justsimple.net.http.HttpResponse;
import org.mutantcat.justsimple.net.http.HttpUtils;
import org.mutantcat.justsimple.net.http.impl.HttpSslSupplierAny;
import org.mutantcat.justsimple.test.HttpTester;
import org.mutantcat.justsimple.test.JustSimpleTest;

@JustSimpleTest(App.class)
public class ServerTest extends HttpTester {
    @AfterAll
    public static void aftAll() {
        if (JustSimple.app() != null) {
            JustSimple.stopBlock();
        }
    }

    @Override
    public HttpUtils path(String path) {
        return super.path("https", path).ssl(HttpSslSupplierAny.getInstance());
    }

    @Test
    public void test() throws Exception {
        assert "hello".equals(path("/test/hello").get());
    }

    @Test
    public void test_old404() throws Exception {
        assert path("/hello").execAsCode("GET") == 404;
    }

    @Test
    public void async() throws Exception {
        assert "async".equals(path("/test/async").get());
    }

    @Test
    public void async_old404() throws Exception {
        assert path("/async").execAsCode("GET") == 404;
    }

    @Test
    public void async_timeout() throws Exception {
        assert 500 == path("/test/async_timeout").head();
    }

    @Test
    public void session() throws Exception {
        MultiMap<String> cookies = new MultiMap<>();
        try (HttpResponse resp = path("/test/session?name=n1").exec("GET")) {
            assert "n1".equals(resp.bodyAsString());

            for (String cookie : resp.cookies()) {
                String[] nameAndValues = cookie.split(";")[0].split("=");
                cookies.add(nameAndValues[0], nameAndValues[1]);
            }
        }

        assert "n1".equals(path("/test/session").cookies(cookies).get());
    }

    @Test
    public void ct0() {
        assert path("/ct0").exec("GET").contentType() == null;
    }

    @Test
    public void ct1() {
        assert path("/test/hello").exec("GET").contentType()
                .startsWith(MimeType.TEXT_PLAIN_VALUE);
    }
}