package demo;

import org.mutantcat.justsimple.annotation.Managed;
import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.view.velocity.VelocityRender;

/**
 * @author noear 2024/9/15 created
 */
@Configuration
public class Config {
    @Managed
    public void configure(VelocityRender render){
        render.getProvider();
        render.getProviderOfDebug();
    }
}
