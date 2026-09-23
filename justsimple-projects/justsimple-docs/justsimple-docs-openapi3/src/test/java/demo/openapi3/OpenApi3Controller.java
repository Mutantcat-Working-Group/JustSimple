package demo.openapi3;

import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Mapping;
import org.mutantcat.justsimple.annotation.Produces;
import org.mutantcat.justsimple.core.handle.Context;
import org.mutantcat.justsimple.docs.openapi3.OpenApi3Utils;

import java.io.IOException;

@Controller
public class OpenApi3Controller {
    /**
     * swagger 获取分组信息
     */
    @Produces("application/json; charset=utf-8")
    @Mapping("swagger-resources")
    public String resources() throws IOException {
        return OpenApi3Utils.getApiGroupResourceJson();
    }

    /**
     * swagger 获取分组接口数据
     */
    @Produces("application/json; charset=utf-8")
    @Mapping("swagger/v3")
    public String api(Context ctx, String group) throws IOException {
        return OpenApi3Utils.getApiJson(ctx, group);
    }
}