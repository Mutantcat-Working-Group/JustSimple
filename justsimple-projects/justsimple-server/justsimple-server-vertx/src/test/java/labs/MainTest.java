package labs;

import org.mutantcat.justsimple.JustSimple;

/**
 * @author noear 2024/8/23 created
 */
public class MainTest {
    public static void main(String[] args) {
        JustSimple.start(MainTest.class, args, app -> {
            app.router().get("/", c -> c.output("Hello!"));
        });
    }
}
