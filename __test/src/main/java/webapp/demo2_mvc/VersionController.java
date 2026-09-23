package webapp.demo2_mvc;

import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Mapping;
import org.mutantcat.justsimple.annotation.Singleton;

/**
 * @author noear 2025/6/25 created
 */
@Mapping("/demo2/version/api")
@Controller
public class VersionController {
    @Mapping(version = "1.0")
    public String v1() {
        return "v1.0";
    }

    @Mapping(version = "2.0")
    public String v2() {
        return "v2.0";
    }
}
