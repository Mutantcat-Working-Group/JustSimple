package features.serialization.jackson.action;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.annotation.*;
import org.mutantcat.justsimple.core.AppContext;
import org.mutantcat.justsimple.core.handle.ContextEmpty;
import org.mutantcat.justsimple.test.JustSimpleTest;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author noear 2024/9/17 created
 */
@JustSimpleTest
public class TestExecutor {
    @Inject
    AppContext context;

    @Test
    public void test() throws Throwable {
        System.out.println(DateTimeFormatter.ISO_OFFSET_DATE_TIME.toString());

        ContextEmpty ctx = new ContextEmpty();
        ctx.headerMap().add("Content-Type", "text/json");
        ctx.pathNew("/a1");
        ctx.bodyNew("{\"name\":\"noear\",\"label\":\"A\"}");

        context.app().routerHandler().handle(ctx);
        ctx.result = ctx.attr("output");
        System.out.println(ctx.result);
        assert "Hello noear A".equals(ctx.result);
    }

    @Test
    public void test1() throws Throwable {
        ContextEmpty ctx = new ContextEmpty();
        ctx.headerMap().add("Content-Type", "text/json");
        ctx.pathNew("/a2");
        ctx.bodyNew("{\"name\":\"noear\",\"label\":\"A\"}");

        context.app().routerHandler().handle(ctx);

        ctx.result = ctx.attr("output");
        System.out.println(ctx.result);
        assert "\"A\"".equals(ctx.result);
    }

    @Test
    public void test2() throws Throwable {
        String time1 = "2024-10-22T08:23:17.315";

        ContextEmpty ctx = new ContextEmpty();
        ctx.headerMap().add("Content-Type", "text/json");
        ctx.pathNew("/a3");
        ctx.bodyNew("{\"name\":\"noear\",\"time\":\"" + time1 + "\"}");

        context.app().routerHandler().handle(ctx);

        ctx.result = ctx.attr("output");
        System.out.println(ctx.result);
        assert time1.equals(ctx.result);
    }

    @Test
    public void test2_a() throws Throwable {
        String time1 = "2024-10-22T08:48:21";
        String time2 = "2024-10-22 08:48:21";
        String time3 = "2024-10-22";

        {
            ContextEmpty ctx = new ContextEmpty();
            ctx.headerMap().add("Content-Type", "text/json");
            ctx.pathNew("/a3");
            ctx.bodyNew("{\"name\":\"noear\",\"time\":\"" + time1 + "\"}");

            context.app().routerHandler().handle(ctx);

            ctx.result = ctx.attr("output");
            System.out.println(ctx.result);
            assert time1.equals(ctx.result);
        }

        {
            ContextEmpty ctx = new ContextEmpty();
            ctx.headerMap().add("Content-Type", "text/json");
            ctx.pathNew("/a3");
            ctx.bodyNew("{\"name\":\"noear\",\"time\":\"" + time2 + "\"}");

            context.app().routerHandler().handle(ctx);

            ctx.result = ctx.attr("output");
            System.out.println(ctx.result);
            assert time1.equals(ctx.result);
        }

        {
            ContextEmpty ctx = new ContextEmpty();
            ctx.headerMap().add("Content-Type", "text/json");
            ctx.pathNew("/a3");
            ctx.bodyNew("{\"name\":\"noear\",\"time\":\"" + time3 + "\"}");

            context.app().routerHandler().handle(ctx);

            ctx.result = ctx.attr("output");
            System.out.println(ctx.result);
            assert "2024-10-22T00:00".equals(ctx.result);
        }
    }

    @Test
    public void test2_b() throws Throwable {
        {
            ContextEmpty ctx = new ContextEmpty();
            ctx.headerMap().add("Content-Type", "text/json");
            ctx.pathNew("/a3");
            ctx.bodyNew("{\"name\":\"noear\",\"time\":\"" + LocalDate.now() + "\"}");

            context.app().routerHandler().handle(ctx);
        }

        {
            ContextEmpty ctx = new ContextEmpty();
            ctx.headerMap().add("Content-Type", "text/json");
            ctx.pathNew("/a3");
            //LocalTime.now() 的小数位取决于 JVM 时钟精度：Linux 上是纳秒（9 位），
            //macOS 上是微秒（6 位），而 DateUtil 只支持到 6 位小数（见 DateUtilTest
            //的无效格式用例，9 位必须抛异常）。这里截到毫秒，保证任何平台发出的都是受支持格式
            ctx.bodyNew("{\"name\":\"noear\",\"time\":\"" + LocalTime.now().truncatedTo(ChronoUnit.MILLIS) + "\"}");

            context.app().routerHandler().handle(ctx);
        }

        {
            ContextEmpty ctx = new ContextEmpty();
            ctx.headerMap().add("Content-Type", "text/json");
            ctx.pathNew("/a3");
            ctx.bodyNew("{\"name\":\"noear\",\"time\":\"" + OffsetDateTime.now() + "\"}");

            context.app().routerHandler().handle(ctx);
        }

        {
            ContextEmpty ctx = new ContextEmpty();
            ctx.headerMap().add("Content-Type", "text/json");
            ctx.pathNew("/a3");
            //与上面的 LocalTime 同理：OffsetTime.now() 在纳秒精度的 JVM 上会带 9 位小数，
            //超出 DateUtil 支持的 6 位上限，必须先截断再发送
            ctx.bodyNew("{\"name\":\"noear\",\"time\":\"" + OffsetTime.now().truncatedTo(ChronoUnit.MILLIS) + "\"}");

            context.app().routerHandler().handle(ctx);
        }
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

        @Mapping("/a3")
        public String a3(String name, LocalDateTime time) {
            return time.toString();
        }
    }

    public enum Label {
        A,
        B
    }
}