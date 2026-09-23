package features.cache;

import org.mutantcat.justsimple.annotation.Component;
import org.mutantcat.justsimple.annotation.Param;
import org.mutantcat.justsimple.data.annotation.Cache;

/**
 *
 * @author noear 2025/12/22 created
 *
 */

@Component
public class DemoService {
    @Cache(key = "#{id}", seconds = 1)
    public String getName(@Param("id") String i) {
        return String.valueOf(System.currentTimeMillis());
    }
}
