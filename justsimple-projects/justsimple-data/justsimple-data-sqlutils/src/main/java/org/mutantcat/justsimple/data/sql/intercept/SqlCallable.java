package org.mutantcat.justsimple.data.sql.intercept;

import org.mutantcat.justsimple.lang.Preview;

import java.sql.SQLException;

/**
 * Sql 可调用的
 *
 * @author noear
 * @since 3.1
 */
@Preview("3.1")
public interface SqlCallable {
    /**
     * 调用
     */
    Object call() throws SQLException;
}
