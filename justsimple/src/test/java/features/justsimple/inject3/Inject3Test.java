package features.justsimple.inject3;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.Utils;
import org.mutantcat.justsimple.core.AppContext;

/**
 * @author noear 2025/4/10 created
 */
public class Inject3Test {
    @Test
    public void test() {
        AppContext appContext = new AppContext();

        appContext.beanScan(DemoConfig.class);
        appContext.start();

        DemoConfig demoConfig = appContext.getBean(DemoConfig.class);

        assert Utils.isNotEmpty(demoConfig.getDemos());
        assert demoConfig.getDemos().size() == 1;
    }
}
