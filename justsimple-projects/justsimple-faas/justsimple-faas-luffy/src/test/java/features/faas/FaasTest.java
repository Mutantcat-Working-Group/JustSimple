package features.faas;

import demo.faas.LuffyApp;
import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.test.HttpTester;
import org.mutantcat.justsimple.test.JustSimpleTest;

/**
 * @author noear 2025/7/1 created
 */
@JustSimpleTest(LuffyApp.class)
public class FaasTest extends HttpTester {
    @Test
    public void case1() {
        assert path("/hello.js?name=justsimple").get().equals("justsimple");
    }

    @Test
    public void case2() {
        assert path("/hello.js").data("name", "justsimple").execAsCode("POST") == 405;
    }
}
