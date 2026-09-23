package features.feathttp.https;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.annotation.Body;
import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Mapping;
import org.mutantcat.justsimple.annotation.Post;
import org.mutantcat.justsimple.core.handle.Context;
import org.mutantcat.justsimple.core.util.MultiMap;

/**
 * @author noear 2024/10/1 created
 */
@Controller
public class App {
    public static void main(String[] args) {
        JustSimple.start(
                App.class,
                MultiMap.from(args).then(x -> x.add("cfg", "app-https.yml"))
        );
    }

    @Mapping("hello")
    public String hello(String name) {
        return "hello " + name;
    }

    @Mapping("body")
    public String body(String tag, @Body String body) {
        return tag + ":" + body;
    }

    @Post
    @Mapping("post")
    public String post() {
        return "ok";
    }

    @Mapping("async")
    public void async(Context ctx) {
        try {
            ctx.asyncStart();
            ctx.output("async");
        } finally {
            ctx.asyncComplete();
        }
    }

    @Mapping("async_timeout")
    public void async_timeout(Context ctx) {
        ctx.asyncStart(100L, null);
    }

    @Mapping("/redirect/h5")
    public void h5(Context ctx, int code) throws Exception {
        ctx.redirect("https://h5.noear.org/", code);
    }

    @Mapping("/redirect/jump")
    public void jump(Context ctx, int code) throws Exception {
        ctx.redirect("target", code);
    }

    @Mapping("/redirect/target")
    public String target() throws Exception {
        return "ok";
    }
}
