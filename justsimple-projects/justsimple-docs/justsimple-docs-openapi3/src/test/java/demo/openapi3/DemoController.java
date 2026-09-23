package demo.openapi3;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.mutantcat.justsimple.annotation.Body;
import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Mapping;
import org.mutantcat.justsimple.annotation.Post;
import org.mutantcat.justsimple.core.handle.Result;

@Tag(name ="控制器")
@Mapping("/demo")
@Controller
public class DemoController {
    @Operation(summary = "接口")
    @Mapping("hello")
    public Result hello(User user) { //以普通参数提交
        return null;
    }

    @Operation(summary = "接口")
    @Post
    @Mapping("hello2")
    public Result hello2(@Body User user) { //以 json body 提交
        return null;
    }
}