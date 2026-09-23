package features.enjoy;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.annotation.Inject;
import org.mutantcat.justsimple.core.handle.ContextEmpty;
import org.mutantcat.justsimple.core.handle.ModelAndView;
import org.mutantcat.justsimple.test.JustSimpleTest;
import org.mutantcat.justsimple.view.enjoy.EnjoyRender;

@JustSimpleTest(InjectTest.class)
public class InjectTest {
    public static void main(String[] args) {
        JustSimple.start(InjectTest.class, args);
    }

    @Test
    public void testViewDirectiveInject() throws Throwable {
        EnjoyRender render = JustSimple.context().getBean(EnjoyRender.class);

        ModelAndView mv = new ModelAndView("inject_test.shtm");
        mv.put("name", "JustSimple");
        String html = render.renderAndReturn(mv, new ContextEmpty());

        System.out.println(html);

        // HelloTag 通过 @Component("view:hello") 注册，由 ViewEnjoyPlugin 扫描并注入
        assert html.contains("share: Hi, JustSimple!; directive: Hi, JustSimple!") : "View directive injection failed: " + html;
    }
}
