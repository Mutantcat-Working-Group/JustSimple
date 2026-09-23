package features.serialization.gson.action;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Inject;
import org.mutantcat.justsimple.annotation.Mapping;
import org.mutantcat.justsimple.core.AppContext;
import org.mutantcat.justsimple.core.handle.ContextEmpty;
import org.mutantcat.justsimple.test.JustSimpleTest;

/**
 * @author noear 2024/9/17 created
 */
@JustSimpleTest
public class TestExecutor {
    @Inject
    AppContext context;

    @Test
    public void test() throws Throwable {
        ContextEmpty ctx = new ContextEmpty();
        ctx.headerMap().add("Content-Type", "text/json");
        ctx.pathNew("/a1");
        ctx.bodyNew("{\"name\":\"noear\",\"label\":\"A\"}");

        context.app().tryHandle(ctx);
        ctx.result = ctx.attr("output");
        System.out.println(ctx.result);
        assert "Hello noear A".equals(ctx.result);

        ctx = new ContextEmpty();
        ctx.headerMap().add("Content-Type", "text/json");
        ctx.pathNew("/a2");
        ctx.bodyNew("{\"name\":\"noear\",\"label\":\"A\"}");

        context.app().tryHandle(ctx);
        ctx.result = ctx.attr("output");
        System.out.println(ctx.result);
        assert "\"A\"".equals(ctx.result);
    }

    @Controller
    public static class Demo {
        @Mapping("/a1")
        public String a1(String name, Label label) {
            return "Hello " + name + " " + label;
        }

        @Mapping("/a2")
        public Label a2(String name, Label label) {
            return label;
        }
    }

    public enum Label {
        A,
        B
    }
}