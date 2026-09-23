package features.justsimple;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.core.handle.ContextEmpty;

import java.net.URI;

/**
 * @author noear 2025/5/15 created
 */
public class ContextTest {
    @Test
    public void case1() {
        ContextEmpty contextEmpty = new ContextEmpty() {
            private URI uri = URI.create("http://localhost:8080/demo/");

            @Override
            public URI uri() {
                return uri;
            }
        };

        assert contextEmpty.uri() != null;
        assert contextEmpty.uri().getScheme().equals("http");
        assert contextEmpty.path().equals("/demo/");
        assert contextEmpty.url().equals("http://localhost:8080/demo/");
    }
}
