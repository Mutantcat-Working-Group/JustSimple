package features.jdkhttp.https;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.core.util.MimeType;
import org.mutantcat.justsimple.core.util.ResourceUtil;
import org.mutantcat.justsimple.net.http.HttpSslSupplier;
import org.mutantcat.justsimple.net.http.HttpUtils;
import org.mutantcat.justsimple.net.http.impl.HttpSslSupplierAny;
import org.mutantcat.justsimple.net.http.ssl.SslAnyTrustManager;
import org.mutantcat.justsimple.net.http.ssl.SslContextBuilder;
import org.mutantcat.justsimple.test.HttpTester;
import org.mutantcat.justsimple.test.JustSimpleTest;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.net.URL;

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
    public void hello() throws Exception {
        assert "hello null".equals(path("/hello").get());
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
    public void ct0() {
        assert path("/ct0").exec("GET").contentType() == null;
    }

    @Test
    public void ct1() {
        assert path("/hello").exec("GET").contentType()
                .startsWith(MimeType.TEXT_PLAIN_VALUE);
    }
}
