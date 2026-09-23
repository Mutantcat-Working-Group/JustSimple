package features;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.test.HttpTester;
import org.mutantcat.justsimple.test.JustSimpleTest;
import webapp.App;

/**
 * @author noear 2025/5/16 created
 */
@JustSimpleTest(App.class)
public class HttpRedirectTest extends HttpTester {
    @Test
    public void case301() throws Exception {
        assert path("/demo2/redirect/jump?code=301").get().equals("ok");
    }

    @Test
    public void case302() throws Exception {
        assert path("/demo2/redirect/jump?code=302").get().equals("ok");
    }

    @Test
    public void case307() throws Exception {
        assert path("/demo2/redirect/jump?code=307").get().equals("ok");
    }
}
