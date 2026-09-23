package features.websse;

import demo.websse.App;
import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.test.HttpTester;
import org.mutantcat.justsimple.test.JustSimpleTest;

/**
 * @author noear 2024/8/30 created
 */
@JustSimpleTest(value = App.class)
public class Case3AppTest extends HttpTester {
    @Test
    public void test11() throws Exception {
        assert path("/case3/m1?name=d").get().equals("Hello d");
    }

    @Test
    public void test12() throws Exception {
        assert path("/case3/m2?name=d").get().equals("Hello d");
    }

    @Test
    public void test21() throws Exception {
        assert path("/case3/f1?name=d").get().equals("data:test\n");
    }

    @Test
    public void test22() throws Exception {
        assert path("/case3/f2?name=d").get().equals("data:test1\n" +
                "data:test2\n");
    }
}