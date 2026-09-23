package features.core;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.annotation.Inject;
import org.mutantcat.justsimple.core.AppContext;
import org.mutantcat.justsimple.core.BeanWrap;
import org.mutantcat.justsimple.core.handle.Handler;
import org.mutantcat.justsimple.test.JustSimpleTest;
import webapp.App;

/**
 * @author noear 2024/9/6 created
 */
@Configuration
@JustSimpleTest(App.class)
public class RawClzTest {
    @Inject
    AppContext appContext;

    @Test
    public void anonymousClassTest1() {
        BeanWrap beanWrap = appContext.getWrap("test_AnonymousClass_Handler");
        assert Handler.class.isAssignableFrom(beanWrap.rawClz());
    }

    @Test
    public void anonymousClassTest2() {
        BeanWrap beanWrap = appContext.getWrap("test_AnonymousClass_Handler2");
        assert Handler.class.isAssignableFrom(beanWrap.rawClz());
    }
}
