package features;

import org.junit.jupiter.api.Test;
import org.noear.snack4.ONode;
import org.noear.snack4.codec.BeanEncoder;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.test.JustSimpleTest;
import webapp.App;

/**
 *
 * @author noear 2025/10/15 created
 *
 */
@JustSimpleTest(App.class)
public class PropSerializeTest {
    @Test
    public void case1() {
        ONode oNode = BeanEncoder.encode(JustSimple.cfg());
        System.out.println(oNode.toJson());
    }
}
