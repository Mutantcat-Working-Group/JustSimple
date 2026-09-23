package webapp.demo2_mvc;

import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Mapping;

/**
 *
 * @author noear 2025/11/3 created
 *
 */
@Mapping("/demo2/pathprefix")
@Controller
public class PathPrefixController {
    @Mapping("hello")
    public String hello() {
        return "hello";
    }
}
