package demo.boot.jdkhttp;

import org.mutantcat.justsimple.JustSimple;

/**
 * @author noear 2025/4/16 created
 */
public class ServerApp {
    public static void main(String[] args) {
        JustSimple.start(ServerDemo.class, args, app -> {
            app.router().get("/", ctx -> {
                ctx.output("Hello World");
            });
        });
    }
}
