package labs.undertow.http;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.core.util.MimeType;
import org.mutantcat.justsimple.core.util.MultiMap;
import org.mutantcat.justsimple.net.http.HttpResponse;
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

    @Test
    public void test() throws Exception {
        assert "hello".equals(path("/hello").get());
    }

    @Test
    public void async() throws Exception {
        assert "async".equals(path("/async").get());
    }

    @Test
    public void async_timeout() throws Exception {
        assert 500 == path("/async_timeout").head();
    }

    @Test
    public void session() throws Exception {
        MultiMap<String> cookies = new MultiMap<>();
        try (HttpResponse resp = path("/session?name=n1").exec("GET")) {
            assert "n1".equals(resp.bodyAsString());

            for (String cookie : resp.cookies()) {
                String[] nameAndValues = cookie.split(";")[0].split("=");
                cookies.add(nameAndValues[0], nameAndValues[1]);
            }
        }

        assert "n1".equals(path("/session").cookies(cookies).get());
    }

    @Test
    public void ct0() {
        assert path("/ct0").exec("GET").contentType() == null;
    }

    @Test
    public void ct1() {
        assert path("/hello").exec("GET").contentType()
                .startsWith(MimeType.TEXT_PLAIN_VALUE);
    }

    @AfterAll
    public static void allAft() {
        //避免与同项目的，边上的 app 冲突
        if (JustSimple.app() != null) {
            JustSimple.stopBlock();
        }
    }
}
