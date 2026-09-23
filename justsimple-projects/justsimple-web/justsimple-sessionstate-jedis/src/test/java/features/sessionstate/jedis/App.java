package features.sessionstate.jedis;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Mapping;
import org.mutantcat.justsimple.core.handle.Context;
import org.mutantcat.justsimple.core.handle.SessionState;

import java.util.HashMap;
import java.util.Map;

/**
 * @author noear 2024/11/29 created
 */
@Controller
public class App {
    public static void main(String[] args) {
        JustSimple.start(App.class, args);
    }

    @Mapping
    public Object home(Context ctx) {
        SessionState state = ctx.sessionState();

        Map<String, Object> map = new HashMap<>();

        map.put("id", state.sessionId());
        map.put("t1", state.creationTime());
        map.put("t2", state.lastAccessTime());

        return map;
    }
}
