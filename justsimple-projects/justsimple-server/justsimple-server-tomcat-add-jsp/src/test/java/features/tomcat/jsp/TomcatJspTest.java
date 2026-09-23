package features.tomcat.jsp;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.test.HttpTester;
import org.mutantcat.justsimple.test.JustSimpleTest;

/**
 *
 * @author noear 2025/12/1 created
 *
 */
@JustSimpleTest(App.class)
public class TomcatJspTest extends HttpTester {
    @Test
    public void case1() {
        assert path("/").get().contains("你好");
    }
}
