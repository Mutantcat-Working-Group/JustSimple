package demo.test;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.annotation.Component;
import org.mutantcat.justsimple.core.handle.Context;
import org.mutantcat.justsimple.core.handle.Render;

@Component("@json")
public class RenderImpl implements Render {
    @Override
    public void render(Object data, Context ctx) throws Throwable {
        //用 json 序列化器生成数据
        String json = JustSimple.app().serializers().jsonOf().serialize(data);
        String jsonEncoded = "";//加密
        String jsonEigned = ""; //鉴名

        ctx.headerSet("E", jsonEigned);
        ctx.output(jsonEncoded);
    }
}