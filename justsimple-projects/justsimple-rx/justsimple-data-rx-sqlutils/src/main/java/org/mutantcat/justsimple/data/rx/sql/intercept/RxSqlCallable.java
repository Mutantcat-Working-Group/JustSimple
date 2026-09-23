package org.mutantcat.justsimple.data.rx.sql.intercept;

import org.mutantcat.justsimple.lang.Preview;
import org.reactivestreams.Publisher;

/**
 * Sql 可调用的
 *
 * @author noear
 * @since 3.1
 */
@Preview("3.1")
public interface RxSqlCallable {
    /**
     * 调用
     */
    Publisher call();
}