package features.undertow.jsp;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Mapping;
import org.mutantcat.justsimple.core.handle.ModelAndView;

/**
 *
 * @author noear 2025/12/1 created
 *
 */
@Controller
public class App {
    public static void main(String[] args) {
        JustSimple.start(App.class, args);
    }

    @Mapping("/")
    public ModelAndView home() {
        ModelAndView model = new ModelAndView("jsp.jsp");
        model.put("title","dock");
        model.put("msg","你好 world! in XController");

        return model;
    }
}
