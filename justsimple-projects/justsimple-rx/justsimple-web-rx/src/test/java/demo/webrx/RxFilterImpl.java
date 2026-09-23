package demo.webrx;

import org.mutantcat.justsimple.annotation.Managed;
import org.mutantcat.justsimple.core.handle.Context;
import org.mutantcat.justsimple.core.handle.Filter;
import org.mutantcat.justsimple.core.handle.FilterChain;
import org.mutantcat.justsimple.rx.Completable;
import org.mutantcat.justsimple.rx.handle.RxContext;
import org.mutantcat.justsimple.rx.handle.RxFilter;
import org.mutantcat.justsimple.rx.handle.RxFilterChain;

/**
 * @author noear 2025/2/16 created
 */
@Managed
public class RxFilterImpl implements Filter, RxFilter {
    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        try {
            chain.doFilter(ctx);
        } catch (Throwable ex) {
            System.out.println("RxFilterImpl.doFilter error");
        }
    }

    @Override
    public Completable doFilter(RxContext ctx, RxFilterChain chain) {
        return chain.doFilter(ctx)
                .doOnComplete(() -> {
                    System.out.println("RxFilterImpl.doFilter called");
                }).doOnError(err -> {
                    System.out.println("RxFilterImpl.doFilter error");
                });
    }
}
