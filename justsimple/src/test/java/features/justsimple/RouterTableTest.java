package features.justsimple;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.core.handle.MethodType;
import org.mutantcat.justsimple.core.route.RoutingTableDefault;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author noear 2025/12/3 created
 *
 */
public class RouterTableTest {
    @Test
    public void case1() {
        RoutingTableDefault<String> router = new RoutingTableDefault<>();

        router.add("/**/$*", MethodType.GET, 0, null, "a");
        router.add("/**/@*", MethodType.GET, 0, null, "b");

        assert "a".equals(router.matchOne("/log/list/$你好", null, MethodType.GET));
        assert "b".equals(router.matchOne("/log/list/@你好", null, MethodType.GET));
    }

    /**
     * 静态段优于变量段（与注册顺序无关）
     */
    @Test
    public void case2() {
        RoutingTableDefault<String> router1 = new RoutingTableDefault<>();
        router1.add("/workspace/{workspaceId}/knowledge/{id}", MethodType.GET, 0, null, "dynamic");
        router1.add("/workspace/{workspaceId}/knowledge/model", MethodType.GET, 0, null, "static");

        assertEquals("static", router1.matchOne("/workspace/11/knowledge/model", null, MethodType.GET));
        assertEquals("dynamic", router1.matchOne("/workspace/11/knowledge/123", null, MethodType.GET));

        RoutingTableDefault<String> router2 = new RoutingTableDefault<>();
        router2.add("/workspace/{workspaceId}/knowledge/model", MethodType.GET, 0, null, "static");
        router2.add("/workspace/{workspaceId}/knowledge/{id}", MethodType.GET, 0, null, "dynamic");

        assertEquals("static", router2.matchOne("/workspace/11/knowledge/model", null, MethodType.GET));
        assertEquals("dynamic", router2.matchOne("/workspace/11/knowledge/123", null, MethodType.GET));
    }

    /**
     * 同层级：按段从左到右比较精确度
     */
    @Test
    public void case3() {
        RoutingTableDefault<String> router = new RoutingTableDefault<>();
        router.add("/a/{x}/c", MethodType.GET, 0, null, "left-var");
        router.add("/a/b/{x}", MethodType.GET, 0, null, "left-const");

        assertEquals("left-const", router.matchOne("/a/b/c", null, MethodType.GET));
        assertEquals("left-var", router.matchOne("/a/1/c", null, MethodType.GET));
    }

    /**
     * 大层级不变：变量 优于 单段通配
     */
    @Test
    public void case4() {
        RoutingTableDefault<String> router = new RoutingTableDefault<>();
        router.add("/b/*", MethodType.GET, 0, null, "star");
        router.add("/{x}/a", MethodType.GET, 0, null, "var");

        assertEquals("var", router.matchOne("/b/a", null, MethodType.GET));
    }

    /**
     * 跨段通配与全匹配，始终排在最后
     */
    @Test
    public void case5() {
        RoutingTableDefault<String> router = new RoutingTableDefault<>();
        router.add("/**", MethodType.GET, 0, null, "matchall");
        router.add("/api/**", MethodType.GET, 0, null, "api-any");
        router.add("/api/{x}/list", MethodType.GET, 0, null, "api-list");

        assertEquals("api-list", router.matchOne("/api/1/list", null, MethodType.GET));
        assertEquals("api-any", router.matchOne("/api/1/other", null, MethodType.GET));
        assertEquals("matchall", router.matchOne("/other/1", null, MethodType.GET));
    }

    /**
     * 精确度相同时，仍按 index 排序
     */
    @Test
    public void case6() {
        RoutingTableDefault<String> router = new RoutingTableDefault<>();
        router.add("/a/{x}", MethodType.GET, 5, null, "index5");
        router.add("/a/{y}", MethodType.GET, 0, null, "index0");

        assertEquals("index0", router.matchOne("/a/1", null, MethodType.GET));
    }
}
