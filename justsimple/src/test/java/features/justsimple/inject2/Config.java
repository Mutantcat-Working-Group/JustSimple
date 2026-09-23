package features.justsimple.inject2;

import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.annotation.Inject;

import java.util.List;
import java.util.Map;

/**
 * @author noear 2025/3/19 created
 */
@Configuration
public class Config {
    @Inject
    Map<String, DnBean> dnBeanMap;

    @Inject
    List<DsBean> dsBeanList;
}
