package features.webrx;

import demo.webrx.App;
import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.test.HttpTester;
import org.mutantcat.justsimple.test.JustSimpleTest;

/**
 * @author noear 2024/8/30 created
 */
@JustSimpleTest(value = App.class, args = "--server.port=7071")
public class Case3AppTest extends HttpTester {
    @Test
    public void test11() throws Exception {
        assert path("/case3/m1?name=d").get().equals("Hello d");
    }

    @Test
    public void test12() throws Exception {
        assert path("/case3/m2?name=d").get().equals("Hello d");
    }
}