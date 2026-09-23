package features.justsimple.inject;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.annotation.Inject;
import org.mutantcat.justsimple.core.AppContext;
import org.mutantcat.justsimple.core.Plugin;

/**
 * @author noear 2024/12/17 created
 */
public class AppTest {
    @Test
    public void case1() throws Exception {
        JustSimple.start(AppTest.class, new String[0], app -> {
            app.pluginAdd(0, context -> {
                context.beanInjectorAdd(Inject.class, IA.class, (vh, anno) -> {
                    vh.setValueDefault(() -> new IAExtDef());
                    vh.context().getBeanAsync(vh.getType(), bean -> {
                        vh.setValue(bean);
                    });
                });
            });
        });

        ComA comA = JustSimple.context().getBean(ComA.class);
        ComB comB = JustSimple.context().getBean(ComB.class);

        assert comA != null;
        assert comB != null;

        System.out.println(comA.ia);
        System.out.println(comB.ia);

        assert comA.ia instanceof IAExtDef;
        assert comB.ia instanceof IAExtImpl;
    }
}