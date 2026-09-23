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
package org.mutantcat.justsimple.server.util;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.Utils;

/**
 * Web 跳转工具
 *
 * @author noear
 * @since 1.11
 * @since 3.5
 */
public class RedirectUtils {
    /**
     * 获取跳转地址
     */
    public static String getRedirectPath(String location) {
        if (Utils.isEmpty(JustSimple.cfg().serverContextPath())) {
            return location;
        }

        if (location.startsWith("/")) {
            if (location.startsWith(JustSimple.cfg().serverContextPath())) {
                return location;
            } else {
                return JustSimple.cfg().serverContextPath() + location.substring(1);
            }
        } else {
            return location;
        }
    }
}
