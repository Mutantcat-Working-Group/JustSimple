package features.openapi2.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.openapi2.base.App;
import io.swagger.models.Scheme;
import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.docs.DocDocket;
import org.mutantcat.justsimple.docs.openapi2.OpenApi2Utils;
import org.mutantcat.justsimple.test.JustSimpleTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 校验 openapi2 生成的 swagger 文档。
 *
 * @author noear 2025/12/17 created
 *
 */
@JustSimpleTest(App.class)
public class DocTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void test() throws Exception {
        DocDocket docDocket = new DocDocket()
                .groupName("app端接口")
                .schemes(Scheme.HTTP.toValue())
                .apis("demo.openapi2.base");

        String json = OpenApi2Utils.getSwaggerJson(docDocket, "app端接口");

        System.out.println(json);

        JsonNode root = MAPPER.readTree(json);

        // 旧代码用一整份硬编码 JSON 快照比对长度，实在太脆：JSON 字段顺序会随
        // Jackson 版本变化，而且 update(@Body UserBo) 落地后 PUT /test/user 已从
        // 表单参数变成 application/json 的 body 参数，快照永远是旧的。
        // 改为按结构校验关键内容，既保留回归能力又能跟随文档演进。
        assertEquals("2.0", root.path("swagger").asText());
        assertEquals("app端接口", root.path("info").path("description").asText());
        assertEquals("localhost:8081", root.path("host").asText());
        assertEquals(1, root.path("schemes").size());
        assertEquals("http", root.path("schemes").get(0).asText());

        JsonNode tags = root.path("tags");
        assertEquals(1, tags.size());
        assertEquals("用户信息表 控制器", tags.get(0).path("name").asText());
        assertEquals("test/user (UserController)", tags.get(0).path("description").asText());

        JsonNode paths = root.path("paths");
        assertEquals(6, paths.size());
        assertTrue(paths.has("/test/user"));
        assertTrue(paths.has("/test/user/list"));
        assertTrue(paths.has("/test/user/page"));
        assertTrue(paths.has("/test/user/{id}"));
        assertTrue(paths.has("/test/user/{ids}"));
        assertTrue(paths.has("/test/user/info/{id}"));

        assertEquals("post__test_user", operation(paths, "/test/user", "post").path("operationId").asText());
        assertEquals("put__test_user", operation(paths, "/test/user", "put").path("operationId").asText());
        // save(UserBo) 走表单，update(@Body UserBo) 走 body
        assertEquals("application/x-www-form-urlencoded", operation(paths, "/test/user", "post").path("consumes").get(0).asText());
        assertEquals("application/json", operation(paths, "/test/user", "put").path("consumes").get(0).asText());
        assertEquals("get__test_user_list", operation(paths, "/test/user/list", "get").path("operationId").asText());
        assertEquals("get__test_user_page", operation(paths, "/test/user/page", "get").path("operationId").asText());
        assertEquals("get__test_user_{id}", operation(paths, "/test/user/{id}", "get").path("operationId").asText());
        assertEquals("delete__test_user_{ids}", operation(paths, "/test/user/{ids}", "delete").path("operationId").asText());
        assertEquals("get__test_user_info_{id}", operation(paths, "/test/user/info/{id}", "get").path("operationId").asText());

        assertTrue(root.path("definitions").has("UserBo"));
    }
    /**
     * paths 的键是带斜杠的完整路径，不能用 JsonNode.at() 的指针语法取，
     * 这里按 key 逐层取。
     */
    private static JsonNode operation(JsonNode paths, String path, String method) {
        return paths.get(path).path(method);
    }
}
