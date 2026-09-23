package org.mutantcat.justsimple.data.rx.sql.intercept;

import org.mutantcat.justsimple.lang.Preview;
import org.reactivestreams.Publisher;

/**
 * Sql 命令拦截器
 *
 * @author noear
 * @since 3.1
 */
@Preview("3.1")
public interface RxSqlCommandInterceptor {
    /**
     * 拦截
     *
     * @param inv 调用者
     */
    Publisher doIntercept(RxSqlCommandInvocation inv);
}