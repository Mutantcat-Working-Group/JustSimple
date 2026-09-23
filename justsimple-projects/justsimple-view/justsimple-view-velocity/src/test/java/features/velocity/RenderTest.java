package features.velocity;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.core.handle.ContextEmpty;
import org.mutantcat.justsimple.core.handle.ModelAndView;
import org.mutantcat.justsimple.view.velocity.VelocityRender;

/**
 * @author noear 2024/10/26 created
 */
public class RenderTest {
    @Test
    public void case1() throws Throwable {
        JustSimple.start(RenderTest.class, new String[0], app -> {

        });


        ModelAndView modelAndView = new ModelAndView("velocity.vm");
        modelAndView.put("msg", "1");
        modelAndView.put("title", "2");

        VelocityRender render = JustSimple.context().getBean(VelocityRender.class);
        String html = render.renderAndReturn(modelAndView, new ContextEmpty());

        assert html != null;
        assert html.contains("<html>");
    }
}
