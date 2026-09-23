package webapp.demo2_mvc.util;

import org.mutantcat.justsimple.annotation.Managed;
import org.mutantcat.justsimple.core.handle.MethodArgumentResolver;
import org.mutantcat.justsimple.core.handle.Context;
import org.mutantcat.justsimple.core.util.LazyReference;
import org.mutantcat.justsimple.core.wrap.MethodWrap;
import org.mutantcat.justsimple.core.wrap.ParamWrap;

/**
 * @author noear 2025/7/15 created
 */
@Managed
public class UserArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean matched(Context ctx, ParamWrap pWrap) {
        return pWrap.getAnnotation(UserAnno.class) != null;
    }

    @Override
    public Object resolveArgument(Context ctx, Object target, MethodWrap mWrap, ParamWrap pWrap, int pIndex, LazyReference bodyRef) throws Throwable {
        UserAnno anno = pWrap.getAnnotation(UserAnno.class);

        return new User(anno.value());
    }
}
