package features.core;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.annotation.Inject;
import org.mutantcat.justsimple.test.JustSimpleTest;
import webapp.App;
import webapp.dso.GenericsTestConfig;

/**
 * @author noear 2024/9/30 created
 */
@Configuration
@JustSimpleTest(App.class)
public class GenericsTest {
    @Inject
    GenericsTestConfig genericsTestConfig;

    @Test
    public void test1() {
        assert genericsTestConfig.wxCallbackContext != null;

        genericsTestConfig.wxCallbackContext.check();
        genericsTestConfig.fsCallbackContext.check();
    }

    @Test
    public void test1_static() {
        assert GenericsTestConfig.fsCallbackContext != null;

        GenericsTestConfig.fsCallbackContext.check();
        GenericsTestConfig.fsCallbackContext.check();
    }

    @Test
    public void test2() {
        GenericsTestConfig.TestConfig.check();
    }
}
