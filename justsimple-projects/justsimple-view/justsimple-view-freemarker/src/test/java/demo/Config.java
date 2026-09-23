package demo;

import org.mutantcat.justsimple.annotation.Managed;
import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.view.freemarker.FreemarkerRender;

/**
 * @author noear 2024/9/15 created
 */
@Configuration
public class Config {
    @Managed
    public void configure(FreemarkerRender render){
        render.getProvider();
        render.getProviderOfDebug();
    }
}
