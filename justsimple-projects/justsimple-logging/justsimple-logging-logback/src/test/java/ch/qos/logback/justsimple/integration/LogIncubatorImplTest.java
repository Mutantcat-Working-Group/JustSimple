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
package ch.qos.logback.justsimple.integration;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.rolling.RollingFileAppender;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.core.util.ResourceUtil;
import org.mutantcat.justsimple.logging.LogOptions;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LogIncubatorImpl 场景测试：
 * doLoadUrl 默认装配 / xml 装配、getUrlOfConfig 命中与未命中、doInit 同步 logger 级别
 *
 * @since 3.9.2
 */
public class LogIncubatorImplTest {

    static String savedConfig;
    static File tempDir;

    @BeforeAll
    public static void bootJustSimple() throws Exception {
        if (JustSimple.app() == null) {
            JustSimple.start(LogIncubatorImplTest.class, new String[]{});
        }
        savedConfig = JustSimple.cfg().get("justsimple.logging.config");
        tempDir = Files.createTempDirectory("logback-incubator-test").toFile();
        // 默认装配会把日志写到 logs/ 下，重定向到临时目录
        JustSimple.cfg().put("justsimple.logging.appender.file.name",
                new File(tempDir, "app").getPath().replace('\\', '/'));
        JustSimple.cfg().put("justsimple.logging.appender.file.rolling",
                new File(tempDir, "app_%d{yyyy-MM-dd}_%i.log").getPath().replace('\\', '/'));
    }

    @AfterAll
    public static void cleanUp() throws IOException {
        // Props 无卸载能力：指向不存在的文件，行为上等同未配置（走默认装配）
        JustSimple.cfg().put("justsimple.logging.config", "./no/such/file.xml");
        if (tempDir != null && tempDir.exists()) {
            File[] files = tempDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    Files.deleteIfExists(f.toPath());
                }
            }
            Files.deleteIfExists(tempDir.toPath());
        }
    }

    @AfterEach
    public void resetConfig() {
        // 置为不存在的路径：getUrlOfConfig 未命中 → 后续用例走默认装配
        JustSimple.cfg().put("justsimple.logging.config", "./no/such/file.xml");
    }

    private LoggerContext globalContext() {
        return (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    private void clearRoot() {
        globalContext().getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)
                .detachAndStopAllAppenders();
    }

    @Test
    public void test_doLoadUrl_null_applies_default_configuration() throws Exception {
        clearRoot();
        new LogIncubatorImpl().doLoadUrl(null);

        ch.qos.logback.classic.Logger root =
                globalContext().getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        // app.yml: file.enable=false, console 启用 → 默认装配后 root 至少有 CONSOLE 与 JUSTSIMPLE
        assertNotNull(root.getAppender("CONSOLE"));
        assertNotNull(root.getAppender("JUSTSIMPLE"));
    }

    @Test
    public void test_doLoadUrl_with_url_configures_from_xml() throws Exception {
        URL url = ResourceUtil.getResource("custom-logback-justsimple.xml");
        assertNotNull(url);

        clearRoot();
        new LogIncubatorImpl().doLoadUrl(url);

        ch.qos.logback.classic.Logger root =
                globalContext().getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        assertNotNull(root.getAppender("CUSTOM_XML"));
        assertNull(root.getAppender("CONSOLE"));
    }

    @Test
    public void test_incubate_with_missing_config_uses_default() throws Throwable {
        JustSimple.cfg().put("justsimple.logging.config", "./no/such/file.xml");

        new LogIncubatorImpl().incubate();

        ch.qos.logback.classic.Logger root =
                globalContext().getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        // justsimple.logging.config 未命中 → 走默认装配
        assertNotNull(root.getAppender("CONSOLE"));
    }

    @Test
    public void test_incubate_with_config_resource_uses_it() throws Throwable {
        JustSimple.cfg().put("justsimple.logging.config", "custom-logback-justsimple.xml");

        new LogIncubatorImpl().incubate();

        ch.qos.logback.classic.Logger root =
                globalContext().getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        assertNotNull(root.getAppender("CUSTOM_XML"));
        assertNull(root.getAppender("CONSOLE"));
    }

    @Test
    public void test_do_init_syncs_logger_levels() {
        // app.yml 配了 features=TRACE、root=INFO（JustSimple 启动时载入 LogOptions）
        new LogIncubatorImpl().doInit();

        assertEquals(Level.TRACE, globalContext().getLogger("features").getLevel());
        assertEquals(Level.INFO,
                globalContext().getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).getLevel());
    }

    @Test
    public void test_plugin_start_incubates() throws Throwable {
        // LogbackPlugin.start 只是再次孵化；验证不抛异常且 root 有 appender
        new LogbackPlugin().start(null);

        ch.qos.logback.classic.Logger root =
                globalContext().getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        assertNotNull(root.getAppender("JUSTSIMPLE"));
        RollingFileAppender.class.getName(); // 引用校验（file.enable=false 时无 FILE）
    }
}
