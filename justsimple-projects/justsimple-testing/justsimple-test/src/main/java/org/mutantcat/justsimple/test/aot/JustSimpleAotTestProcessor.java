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
package org.mutantcat.justsimple.test.aot;

import org.mutantcat.justsimple.aot.RuntimeNativeMetadata;
import org.mutantcat.justsimple.aot.JustSimpleAotProcessor;
import org.mutantcat.justsimple.core.AppContext;
import org.mutantcat.justsimple.core.BeanWrap;

/**
 * @author songyinyin
 * @since 2023/10/23 16:20
 */
public class JustSimpleAotTestProcessor extends JustSimpleAotProcessor {
    public JustSimpleAotTestProcessor(Class<?> applicationClass) {
        super(null, null, applicationClass);
    }

    public void process(AppContext appContext) throws Throwable {
        RuntimeNativeMetadata metadata = genRuntimeNativeMetadata(appContext);

        // 注册到 bean 容器，方便后续断言使用
        BeanWrap beanWrap = new BeanWrap(appContext, metadata.getClass(), metadata);
        appContext.beanRegister(beanWrap, RuntimeNativeMetadata.class.getName(), true);
    }
}
