package features.justsimple.generic5;

import org.junit.jupiter.api.Test;
import org.noear.eggg.ClassEggg;
import org.noear.eggg.MethodEggg;
import org.mutantcat.justsimple.core.util.EgggUtil;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

/**
 * @author noear 2025/5/3 created
 */
public class BridgeMethodTest {
    @Test
    public void case1() {
        ClassEggg classWrap = EgggUtil.getClassEggg(SysResourcePermissionController.class);

        assert classWrap.getDeclaredMethodEgggs().size() == 0;

        // Class.getMethods() 的方法顺序 JDK 不保证，同一次构建换次 JVM 也可能会变，故按名字查找而非下标
        MethodEggg saveAll = findByName(classWrap, "saveAll");
        assert saveAll.getGenericReturnType() instanceof ParameterizedType;

        MethodEggg saveOne = findByName(classWrap, "saveOne");
        assert saveOne.getGenericReturnType().equals(SysResourcePermission.class);

        System.out.println(Arrays.toString(SysResourcePermissionController.class.getDeclaredMethods()));
        //=> [public java.util.List com.example.demo.App$SysResourcePermissionController.saveAll(java.util.List)]
    }

    private MethodEggg findByName(ClassEggg classWrap, String name) {
        return classWrap.getPublicMethodEgggs()
                .stream()
                .filter(m -> name.equals(m.getName()))
                .findFirst()
                .orElse(null);
    }

    public class SysResourcePermissionController extends BaseController<SysResourcePermissionService, SysResourcePermission, SysResourcePermissionId> {

    }

    abstract class BaseController<S extends BaseService<T, ID>, T, ID> {
        protected S service;

        public T saveOne(T ts) {
            return service.saveOne(ts);
        }

        public List<T> saveAll(List<T> ts) {
            return service.saveAll(ts);
        }
    }

    public class SysResourcePermissionService extends BaseService<SysResourcePermission, SysResourcePermissionId> {
    }

    abstract class BaseService<T, ID> {
        public T saveOne(T ts) {
            return ts;
        }

        public List<T> saveAll(List<T> ts) {
            return ts;
        }
    }

    public class SysResourcePermissionId {
    }

    public class SysResourcePermission {
        public int id;
        public String name;
    }
}
