package demo.openapi2.base;

import io.swagger.models.Scheme;
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
                .schemes(Scheme.HTTP.toValue())
                .apis("demo.openapi2.base");

    }
}
