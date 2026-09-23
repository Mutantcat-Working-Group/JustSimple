package demo.justsimple;

import org.mutantcat.justsimple.annotation.Managed;
import org.mutantcat.justsimple.core.handle.MethodArgumentResolver;
import org.mutantcat.justsimple.core.handle.Context;
import org.mutantcat.justsimple.core.util.LazyReference;
import org.mutantcat.justsimple.core.wrap.MethodWrap;
import org.mutantcat.justsimple.core.wrap.ParamWrap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author noear 2025/7/15 created
 */
@Managed
public class MethodArgumentResolverImpl implements MethodArgumentResolver {
    @Override
    public boolean matched(Context ctx, ParamWrap pWrap) {
        return pWrap.getParameter().isAnnotationPresent(Argument.class);
    }

    @Override
    public Object resolveArgument(Context ctx, Object target, MethodWrap mWrap, ParamWrap pWrap, int pIndex, LazyReference bodyRef) throws Throwable {
        Argument anno = pWrap.getParameter().getAnnotation(Argument.class);

        return anno.value();
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Argument {
        String value() default "";
    }
}
