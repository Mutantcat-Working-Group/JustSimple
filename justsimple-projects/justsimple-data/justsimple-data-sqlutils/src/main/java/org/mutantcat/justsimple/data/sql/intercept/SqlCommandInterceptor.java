package org.mutantcat.justsimple.data.sql.intercept;

import org.mutantcat.justsimple.lang.Preview;

import java.sql.SQLException;

/**
 * Sql 命令拦截器
 *
 * @author noear
 * @since 3.1
 */
@Preview("3.1")
public interface SqlCommandInterceptor {
    /**
     * 拦截
     *
     * @param inv 调用者
     */
    Object doIntercept(SqlCommandInvocation inv) throws SQLException;
}