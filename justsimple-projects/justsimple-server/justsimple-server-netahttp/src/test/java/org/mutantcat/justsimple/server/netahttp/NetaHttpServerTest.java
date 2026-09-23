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
package org.mutantcat.justsimple.server.netahttp;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Mapping;
import org.mutantcat.justsimple.test.HttpTester;
import org.mutantcat.justsimple.test.JustSimpleTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NetaHttp 服务器 JustSimple 集成测试
 *
 * @author noear
 * @since 3.10
 */
@JustSimpleTest(NetaHttpServerTest.App.class)
public class NetaHttpServerTest extends HttpTester {

    @Controller
    public static class App {
        public static void main(String[] args) {
            JustSimple.start(App.class, args);
        }

        @Mapping("/hello")
        public String hello() {
            return "hello";
        }

        @Mapping("/echo")
        public String echo(String name) {
            return "echo:" + name;
        }

        @Mapping("/status")
        public String status() {
            return "ok";
        }
    }

    @Test
    public void testHttpGetHello() throws Exception {
        String result = path("/hello").get();
        assertEquals("hello", result);
    }

    @Test
    public void testHttpGetWithParam() throws Exception {
        String result = path("/echo").data("name", "world").post();
        assertNotNull(result);
        assertTrue(result.contains("world"));
    }

    @Test
    public void testHttpGetStatus() throws Exception {
        String result = path("/status").get();
        assertEquals("ok", result);
    }
}
