package webapp.dso;

import org.mutantcat.justsimple.annotation.Managed;
import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.core.handle.Filter;
import org.mutantcat.justsimple.web.version.VersionFilter;

/**
 * @author noear 2025/6/25 created
 */
@Configuration
public class VersonConfig {
    @Managed
    public Filter filter() {
        return new VersionFilter().useHeader("Api-Version");
    }
}
