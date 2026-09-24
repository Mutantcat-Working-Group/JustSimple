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
    /**
     * Properties.toString() 的输出顺序由 Hashtable 的哈希桶决定，
     * 与插入顺序无关，所以这里只比较键值对本身，不比较输出顺序。
     */
    private static boolean propEquals(Properties properties, String... kv) {
        if (properties.size() != kv.length / 2) {
            return false;
        }

        for (int i = 0; i < kv.length; i += 2) {
            if (kv[i + 1].equals(properties.getProperty(kv[i])) == false) {
                return false;
            }
        }

        return true;
    }

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

        assert propEquals(properties,
                "driverClassName", "123",
                "driverClassName2", "456",
                "driverClassName1", "456");
    }
}
