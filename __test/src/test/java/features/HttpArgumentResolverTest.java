package features;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.test.HttpTester;
import org.mutantcat.justsimple.test.JustSimpleTest;
import webapp.App;

import java.io.IOException;

/**
 * @author noear 2025/7/15 created
 */
@JustSimpleTest(App.class)
public class HttpArgumentResolverTest  extends HttpTester {
    @Test
    public void case1() throws IOException {
        assert path("/demo2/argumentResolver/getUser").get().equals("{\"name\":\"justsimple\"}");
    }
}
