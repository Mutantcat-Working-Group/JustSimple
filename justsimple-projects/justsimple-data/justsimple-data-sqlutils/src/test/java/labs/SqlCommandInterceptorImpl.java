package labs;

import org.mutantcat.justsimple.annotation.Managed;
import org.mutantcat.justsimple.data.sql.intercept.SqlCommandInterceptor;
import org.mutantcat.justsimple.data.sql.intercept.SqlCommandInvocation;

import java.sql.SQLException;

@Managed
public class SqlCommandInterceptorImpl implements SqlCommandInterceptor {


    @Override
    public Object doIntercept(SqlCommandInvocation inv) throws SQLException {
        System.out.println(inv.getCommand());
        long start = System.currentTimeMillis();

        try {
            return inv.invoke();
        } finally {
            System.out.println(System.currentTimeMillis() - start);
        }
    }
}