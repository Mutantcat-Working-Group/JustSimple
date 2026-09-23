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
package org.mutantcat.justsimple.logging.integration;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.core.AppContext;
import org.mutantcat.justsimple.core.Plugin;
import org.mutantcat.justsimple.core.util.ClassUtil;
import org.mutantcat.justsimple.logging.AppenderManager;
import org.mutantcat.justsimple.logging.LogOptions;
import org.mutantcat.justsimple.logging.event.Appender;

import java.util.Properties;


/**
 * @author noear
 * @since 1.3
 */
public class LoggingPlugin implements Plugin {
    public LoggingPlugin() {
        AppenderManager.init();
    }

    @Override
    public void start(AppContext context) {
        Properties props = JustSimple.cfg().getProp("justsimple.logging.appender");

        //初始化
        AppenderManager.init();

        //注册添加器
        if (props.size() > 0) {
            props.forEach((k, v) -> {
                String key = (String) k;
                String val = (String) v;

                if (key.endsWith(".class")) {
                    Appender appender = ClassUtil.tryInstance(val);
                    if (appender != null) {
                        String name = key.substring(0, key.length() - 6);
                        AppenderManager.register(name, appender);
                    }
                }
            });
        }

        //init
        LogOptions.getLoggerLevelInit();

        JustSimple.app().router().filter(Integer.MIN_VALUE,new MdcClearFilter());
    }

    @Override
    public void stop() throws Throwable {
        AppenderManager.stop();
    }
}