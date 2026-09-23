package features.enjoy;

import cn.aifei.enjoy.Directive;
import cn.aifei.enjoy.Env;
import cn.aifei.enjoy.io.Writer;
import cn.aifei.enjoy.stat.Scope;
import org.mutantcat.justsimple.annotation.Component;
import org.mutantcat.justsimple.annotation.Inject;

@Component("view:hello")
public class HellDirective extends Directive {
    @Inject
    HelloService helloService;

    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        try {
            Object[] values = exprList.evalExprList(scope);
            if (values.length > 0) {
                writer.write(helloService.greet((String) values[0]));
            }

            if (stat != null) {
                stat.exec(env, scope, writer);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasEnd() {
        return false;
    }
}
