package features.yaml;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.SimpleJustSimpleApp;

/**
 * @author noear 2025/6/5 created
 */
public class PropDefTest {
    @Test
    public void testPropDef() throws Throwable {
        SimpleJustSimpleApp app = new SimpleJustSimpleApp(PropDefTest.class, new String[]{"-cfg=app-def-test.yml"});
        app.start(x->{
            x.enableScanning(false);
        });


       String server = app.cfg().get("justsimple.config.nacos.server");
       System.out.println(server);
    }
}
