package features.enjoy;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.core.handle.ContextEmpty;
import org.mutantcat.justsimple.core.handle.ModelAndView;
import org.mutantcat.justsimple.test.JustSimpleTest;
import org.mutantcat.justsimple.view.enjoy.EnjoyRender;

/**
 * @author noear 2024/10/26 created
 */
@JustSimpleTest(RenderTest.class)
public class RenderTest {
    @Test
    public void case1() throws Throwable {
        ModelAndView modelAndView = new ModelAndView("enjoy.shtm");
        modelAndView.put("msg", "1");
        modelAndView.put("title", "2");

        EnjoyRender render = JustSimple.context().getBean(EnjoyRender.class);
        String html = render.renderAndReturn(modelAndView, new ContextEmpty());

        assert html != null;
        assert html.contains("<html>");
    }
}
