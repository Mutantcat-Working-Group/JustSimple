package features.feathttp.http;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Mapping;
import org.mutantcat.justsimple.core.handle.Context;

/**
 * @author noear 2024/10/1 created
 */
@Controller
public class App {
    public static void main(String[] args) {
        JustSimple.start(ServerTest.class, args);
    }

    @Mapping("hello")
    public String hello() {
        return "hello";
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

    @Mapping("multipart/text")
    public String multipartText(Context ctx) {
        return ctx.param("text");
    }

    @Mapping("multipart/count")
    public int multipartCount(Context ctx) {
        return ctx.paramMap().size();
    }
}
