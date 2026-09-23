package features.jetty.https;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Mapping;
import org.mutantcat.justsimple.annotation.Param;
import org.mutantcat.justsimple.core.handle.Context;
import org.mutantcat.justsimple.core.util.MultiMap;

import javax.servlet.http.HttpSession;

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
    public String hello(HttpSession session) {
        assert session != null;
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

    @Mapping("session")
    public Object session(Context ctx, @Param(value = "name",required = false) String name) {
        if (name == null) {
            return ctx.session("name");
        } else {
            ctx.sessionSet("name", name);
            return name;
        }
    }
}
