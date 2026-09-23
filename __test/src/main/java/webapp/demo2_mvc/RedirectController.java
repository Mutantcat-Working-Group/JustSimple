package webapp.demo2_mvc;

import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Mapping;
import org.mutantcat.justsimple.core.handle.Context;

/**
 * @author noear 2025/5/16 created
 */
@Mapping("/demo2/redirect/")
@Controller
public class RedirectController {
    @Mapping("/jump")
    public void jump(Context ctx, int code) throws Exception {
        ctx.redirect("target", code);
    }

    @Mapping("/target")
    public String target() throws Exception {
        return "ok";
    }
}
