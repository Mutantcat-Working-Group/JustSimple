/*
 * Copyright 2017-2025 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package features;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.mutantcat.justsimple.annotation.Inject;
import org.mutantcat.justsimple.data.cache.CacheService;
import org.mutantcat.justsimple.test.JustSimpleTest;

/**
 * @author noear 2023/2/16 created
 * <p>
 * 这些用例要连真实的 Redis（app.yml 里配的是 localhost:6379）。
 * CI 环境没有这个服务，端口不通时整个类跳过；本机起了服务就会真正执行。
 */
@EnabledIf("isServiceAvailable")
@JustSimpleTest
public class CacheTest {
    @Inject
    CacheService cacheService;

    @Test
    public void test() throws Exception {
        cacheService.store("test", "1", 1);
        assert "1".equals(cacheService.get("test", String.class));
        Thread.sleep(2000);
        assert cacheService.get("test", String.class) == null;


        UserM userM = new UserM();
        userM.id = 12;
        userM.name = "test";

        cacheService.store("test", userM, 1);
        Thread.sleep(200);
        assert userM.id == cacheService.get("test", UserM.class).id;
        Thread.sleep(2000);
        assert cacheService.get("test", UserM.class) == null;
    }

    @Test
    public void test2() throws Exception {
        cacheService.store("test", "1", 0);
        assert "1".equals(cacheService.get("test", String.class));
        cacheService.remove("test");
        assert cacheService.get("test", String.class) == null;


        UserM userM = new UserM();
        userM.id = 12;
        userM.name = "test";

        cacheService.store("test", userM, 0);
        Thread.sleep(200);
        assert userM.id == cacheService.get("test", UserM.class).id;
        cacheService.remove("test");
        assert cacheService.get("test", UserM.class) == null;
    }

    /**
     * 仅当本机 Redis 端口可连接时才启用，避免 CI 上因缺少服务而误报失败。
     */
    static boolean isServiceAvailable() {
        try (java.net.Socket socket = new java.net.Socket("127.0.0.1", 6379)) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}