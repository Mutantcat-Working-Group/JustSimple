package demo.openapi3.base;

import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.annotation.Managed;
import org.mutantcat.justsimple.docs.DocDocket;

@Configuration
public class Config {
    /**
     * 简单点的
     */
    @Managed("appApi")
    public DocDocket appApi() {
        return new DocDocket()
                .groupName("app端接口")
                .apis("demo.openapi3.base");

    }
}
