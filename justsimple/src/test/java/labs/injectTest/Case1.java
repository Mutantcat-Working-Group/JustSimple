package labs.injectTest;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.annotation.Inject;
import org.mutantcat.justsimple.annotation.Managed;

/**
 * @author noear 2024/11/27 created
 */
@Managed
public class Case1 {
    @Inject("${demo.p1}")
    static int p1;

    public static void main(String[] args) {
        System.setProperty("demo.p1", "1");

        JustSimple.start(Case1.class, args);

        System.out.println(Case1.p1);
    }
}
