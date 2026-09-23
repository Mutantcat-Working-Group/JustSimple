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
package org.mutantcat.justsimple.luffy.impl;

import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * luffy 的上游执行器在构造时会通过 org.noear.solon.Solon.app() 访问应用容器的 shared 表。
 * 本项目的应用容器是 JustSimpleApp，这里为它们准备一个只带 shared 表的最小 SolonApp，
 * 不启动上游 Solon，避免影响 JustSimple 的启动流程。
 */
final class SolonAppRef {
    private static final Logger log = LoggerFactory.getLogger(SolonAppRef.class);

    private SolonAppRef() {
    }

    /**
     * 已安装则复用，未安装则安装一个最小的上游 SolonApp
     */
    static void install() throws Exception {
        Field appField = Solon.class.getDeclaredField("app");
        appField.setAccessible(true);

        if (appField.get(null) != null) {
            return;
        }

        SolonApp app = newInstance();

        if (app == null) {
            log.warn("Can't create a minimal SolonApp for luffy, the javascript executor may not work");
            return;
        }

        appField.set(null, app);
    }

    /**
     * 绕过 SolonApp 的重量级构造（会重新加载配置、扫描插件），只保留 shared 表相关的字段
     */
    private static SolonApp newInstance() throws Exception {
        Object instance = allocateInstance().invoke(unsafe(), SolonApp.class);
        SolonApp app = (SolonApp) instance;

        set(app, "_shared", new LinkedHashMap<String, Object>());
        set(app, "_onSharedAdd_event", Collections.synchronizedSet(new HashSet<Object>()));

        return app;
    }

    private static Object unsafe() throws Exception {
        Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
        Field theUnsafe = unsafeClass.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);

        return theUnsafe.get(null);
    }

    private static Method allocateInstance() throws Exception {
        return Class.forName("sun.misc.Unsafe").getMethod("allocateInstance", Class.class);
    }

    private static void set(Object target, String fieldName, Object val) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, val);
    }
}
