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
package org.mutantcat.justsimple.serialization.javabin.integration;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.Utils;
import org.mutantcat.justsimple.core.AppContext;
import org.mutantcat.justsimple.core.Plugin;
import org.mutantcat.justsimple.serialization.javabin.JavabinClassFilter;
import org.mutantcat.justsimple.serialization.javabin.JavabinSerializer;

/**
 * Javabin 序列化插件。
 */
public class SerializationJavabinPlugin implements Plugin {

    static final String CFG_ALLOW = "justsimple.serialization.javabin.allow";
    static final String CFG_DENY = "justsimple.serialization.javabin.deny";
    static final String CFG_UNRESTRICTED = "justsimple.serialization.javabin.unrestricted";

    @Override
    public void start(AppContext context) {
        applyConfig(JavabinSerializer.getInstance().classFilter());

        JavabinSerializer serializer = JavabinSerializer.getInstance();
        context.wrapAndPut(JavabinSerializer.class, serializer);
    }

    static void applyConfig(JavabinClassFilter filter) {
        String allow = JustSimple.cfg().get(CFG_ALLOW);
        if (Utils.isNotEmpty(allow)) {
            for (String s : allow.split(",")) {
                filter.allow(s.trim());
            }
        }
        String deny = JustSimple.cfg().get(CFG_DENY);
        if (Utils.isNotEmpty(deny)) {
            for (String s : deny.split(",")) {
                filter.deny(s.trim());
            }
        }
        if ("true".equalsIgnoreCase(JustSimple.cfg().get(CFG_UNRESTRICTED))) {
            filter.allowAll(true);
        }
    }
}
