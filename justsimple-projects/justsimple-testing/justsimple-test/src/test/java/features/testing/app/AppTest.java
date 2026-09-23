package features.testing.app;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.annotation.Inject;
import org.mutantcat.justsimple.test.JustSimpleTest;

/**
 * @author noear 2025/1/8 created
 */
@JustSimpleTest
public class AppTest {
    @Inject
    private DemoCom demoCom;

    @Test
    public void test(){
        System.out.println(demoCom.hello());
    }
}
