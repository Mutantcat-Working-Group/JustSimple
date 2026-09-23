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
package org.mutantcat.justsimple;

import org.mutantcat.justsimple.core.util.ConsumerEx;
import org.mutantcat.justsimple.core.util.MultiMap;

/**
 * 简单应用（临时应用对象）
 *
 * @author noear
 * @since 3.0
 */
public class SimpleJustSimpleApp extends JustSimpleApp {

    public SimpleJustSimpleApp(Class<?> source, String... args) throws Exception {
        this(source, MultiMap.from(args));
    }

    public SimpleJustSimpleApp(Class<?> source, MultiMap<String> argx) throws Exception {
        super(source, argx);
    }

    @Override
    protected boolean isMain() {
        return false;
    }

    private JustSimpleApp bakApp;
    private boolean globalize = true;

    /**
     * 是否全局化（默认为 true）
     */
    public SimpleJustSimpleApp globalize(boolean globalize) {
        this.globalize = globalize;
        return this;
    }

    /**
     * 简单开始
     */
    public SimpleJustSimpleApp start(ConsumerEx<JustSimpleApp> initialize) throws Throwable {
        if (globalize) {
            //切换全局 app
            bakApp = JustSimple.app();
            JustSimple.appSet(this);
        }

        super.startDo(initialize);
        return this;
    }

    /**
     * 简单停止（阻塞模式）
     */
    public void stop() {
        try {
            super.preStopDo();
            super.stoppingDo();
            super.stopDo();
        } finally {
            if (globalize) {
                //恢复全局 app
                JustSimple.appSet(bakApp);
            }
        }
    }
}