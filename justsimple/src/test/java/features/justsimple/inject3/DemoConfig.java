package features.justsimple.inject3;

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
    private List<Demo> demos;

    public List<Demo> getDemos() {
        return demos;
    }

    public DemoCon getCon() {
        return con;
    }

    public DemoConfig(@Inject(required = false) DemoCon con){
        this.con = con;
    }

    @Managed(autoInject = true)
    public void setDemo(List<Demo> demos) {
        this.demos = demos;
    }
}