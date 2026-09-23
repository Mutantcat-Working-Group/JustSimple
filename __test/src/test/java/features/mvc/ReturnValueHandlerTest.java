package features.mvc;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.core.Constants;
import org.mutantcat.justsimple.core.handle.*;
import org.mutantcat.justsimple.test.JustSimpleTest;
import webapp.App;
import webapp.models.UserModel;

/**
 * @author noear 2024/10/5 created
 */
@JustSimpleTest(App.class)
public class ReturnValueHandlerTest {
    @Test
    public void case1() throws Throwable {
        UserModel user = new UserModel();
        user.setId(111);

        ContextEmpty ctx = new ContextEmpty(){
            @Override
            public String path() {
                return "/demo2/json/bean";
            }

            @Override
            public Object pull(Class<?> clz) {
                if(UserModel.class.isAssignableFrom(clz)) {
                    return user;
                }

                return super.pull(clz);
            }
        };

        ctx.attrSet(Constants.ATTR_RETURN_HANDLER, new ReturnValueHandlerImpl());

        JustSimple.app().handler().handle(ctx);

        Object ddd = ctx.attr("ddd");
        System.out.println(ddd);
        assert ddd == user;
    }

    @Test
    public void case2() throws Throwable {
        UserModel user = new UserModel();
        user.setId(111);

        ContextEmpty ctx = new ContextEmpty(){
            @Override
            public String path() {
                return "/demo2/json/bean";
            }

            @Override
            public Object pull(Class<?> clz) {
                if(UserModel.class.isAssignableFrom(clz)) {
                    return user;
                }

                return super.pull(clz);
            }
        };

        ctx.attrSet(Constants.ATTR_RETURN_HANDLER, new ReturnValueHandlerImpl());

        //比 JustSimple.app().handler()，省去2层（HandlerPipeline, RouterInterceptorChain）
        Handler handler = JustSimple.app().router().matchMain(ctx);

        assert handler != null;

        handler.handle(ctx);

        Object ddd = ctx.attr("ddd");
        System.out.println(ddd);
        assert ddd == user;
    }

    public static class ReturnValueHandlerImpl implements ReturnValueHandler {
        @Override
        public boolean matched(Context ctx, Class<?> returnType) {
            return false;
        }

        @Override
        public void returnHandle(Context ctx, Object returnValue) throws Throwable {
            ctx.attrSet("ddd", returnValue);
        }
    }
}
