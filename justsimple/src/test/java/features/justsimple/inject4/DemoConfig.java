package features.justsimple.inject4;

import org.mutantcat.justsimple.annotation.Managed;
import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.annotation.Inject;

import java.util.List;

/**
 * @author noear 2025/4/10 created
 */
@Configuration
public class DemoConfig {
    private DemoCon con;
    private Demo demo;


    public DemoConfig(@Inject(required = false) DemoCon con) {
        this.con = con;
    }

    @Managed
    public void setDemo(Demo demo) {
        this.demo = demo;
    }
}