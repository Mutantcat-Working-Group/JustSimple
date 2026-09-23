package demo.nami;

import org.mutantcat.justsimple.nami.Invocation;
import org.mutantcat.justsimple.nami.NamiFilter;
import org.mutantcat.justsimple.nami.Result;
import org.mutantcat.justsimple.annotation.Component;

/**
 *
 * @author noear 2025/9/5 created
 *
 */
@Component
public class NamiFilterImpl implements NamiFilter {
    @Override
    public Result doFilter(Invocation inv) throws Throwable {
        inv.headers.put("xxx","xxx");

        return inv.invoke();
    }
}
