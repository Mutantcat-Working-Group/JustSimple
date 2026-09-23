package demo;

import org.mutantcat.justsimple.annotation.Managed;
import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.view.beetl.BeetlRender;

/**
 * @author noear 2024/9/15 created
 */
@Configuration
public class Config {
    @Managed
    public void configure(BeetlRender render){
        render.getProvider();
        render.getProviderOfDebug();
    }
}
