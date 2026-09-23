package features.freemarker;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.core.handle.ContextEmpty;
import org.mutantcat.justsimple.core.handle.ModelAndView;
import org.mutantcat.justsimple.view.freemarker.FreemarkerRender;

/**
 * @author noear 2024/10/26 created
 */
public class RenderTest {
    @Test
    public void case1() throws Throwable {
        JustSimple.start(RenderTest.class, new String[0], app -> {

        });

        ModelAndView modelAndView = new ModelAndView("freemarker.ftl");
        modelAndView.put("msg", "1");
        modelAndView.put("title", "2");

        FreemarkerRender render = JustSimple.context().getBean(FreemarkerRender.class);
        String html = render.renderAndReturn(modelAndView, new ContextEmpty());

        assert html != null;
        assert html.contains("<html>");
    }
}
