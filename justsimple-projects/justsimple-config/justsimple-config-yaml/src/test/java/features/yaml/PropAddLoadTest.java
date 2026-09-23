package features.yaml;


import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.SimpleJustSimpleApp;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.core.AppContext;
import org.mutantcat.justsimple.core.Plugin;

import java.util.Properties;

/**
 * @author noear 2025/5/29 created
 */
public class PropAddLoadTest {
    @Test
    public void case1() throws Throwable {
        SimpleJustSimpleApp app = new SimpleJustSimpleApp(PropAddLoadTest.class,
                "-cfg=app-cfg-test.yml",
                "-testing=1");
        app.start(x -> {
            x.enableScanning(false);
            x.pluginAdd(990, new Plugin() {
                @Override
                public void start(AppContext context) throws Throwable {
                    Properties properties = new Properties();
                    properties.put("demo2.datasource.driverClassName", "456");
                    JustSimple.cfg().loadAdd(properties);
                }
            });
        });

        Properties properties = app.cfg().getProp("app2.db");
        System.out.println(properties);

        assert "{driverClassName=123, driverClassName2=456, driverClassName1=456}".equals(properties.toString());
    }
}
