package features.justsimple.bean;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.core.AppContext;
import org.mutantcat.justsimple.core.BeanWrap;

/**
 *
 * @author noear 2025/11/8 created
 *
 */
public class BeanTest {
    @Test
    public void case1() {
        AppContext appContext = new AppContext();

        appContext.beanScan(BeanTest.class);
        appContext.start();

        BeanWrap bw = appContext.getWrap(InterfaceBean.class);

        assert bw == null;
    }

    @Test
    public void case2() {
        AppContext appContext = new AppContext();

        appContext.beanMake(InterfaceBean.class);
        appContext.start();

        BeanWrap bw = appContext.getWrap(InterfaceBean.class);

        assert bw == null;
    }
}
