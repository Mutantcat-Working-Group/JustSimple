package features.smarthttp.http;

import org.mutantcat.justsimple.annotation.Component;
import org.mutantcat.justsimple.annotation.Mapping;
import org.mutantcat.justsimple.core.handle.Context;
import org.mutantcat.justsimple.core.handle.Handler;

/**
 *
 * @author noear 2025/11/4 created
 *
 */
@Mapping("ct0")
@Component
public class ContentTypeHandler implements Handler {
    @Override
    public void handle(Context ctx) throws Throwable {
        ctx.output("hello");
    }
}
