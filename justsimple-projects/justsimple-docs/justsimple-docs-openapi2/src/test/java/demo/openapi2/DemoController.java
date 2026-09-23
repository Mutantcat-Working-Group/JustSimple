package demo.openapi2;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mutantcat.justsimple.annotation.Body;
import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Mapping;
import org.mutantcat.justsimple.annotation.Post;
import org.mutantcat.justsimple.core.handle.Result;

@Api("控制器")  //v2.3.8 之前要用：@Api(tags = "控制器")
@Mapping("/demo")
@Controller
public class DemoController {
    @ApiOperation("接口")
    @Mapping("hello")
    public Result hello(User user) { //以普通参数提交
        return null;
    }

    @ApiOperation("接口")
    @Post
    @Mapping("hello2")
    public Result hello2(@Body User user) { //以 json body 提交
        return null;
    }
}