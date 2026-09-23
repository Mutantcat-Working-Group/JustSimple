package features;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.test.HttpTester;
import org.mutantcat.justsimple.test.JustSimpleTest;
import webapp.App;

/**
 * @author noear 2024/11/2 created
 */
@JustSimpleTest(App.class)
public class HttpIndexTest extends HttpTester {
    @Test
    public void case1() throws Exception {
        assert "a1-1".equals(path("/demo1/a1/a").get());
    }

    @Test
    public void case2() throws Exception {
        assert "b1-2-1".equals(path("/demo1/b1/a").get());
    }
}
