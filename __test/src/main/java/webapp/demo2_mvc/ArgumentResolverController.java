package webapp.demo2_mvc;

import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Mapping;
import org.mutantcat.justsimple.annotation.Singleton;
import webapp.demo2_mvc.util.User;
import webapp.demo2_mvc.util.UserAnno;

/**
 * @author noear 2025/7/15 created
 */
@Singleton(false)
@Mapping("/demo2/argumentResolver")
@Controller
public class ArgumentResolverController {
    @Mapping("getUser")
    public User getUser(@UserAnno("justsimple") User user) {
        return user;
    }
}