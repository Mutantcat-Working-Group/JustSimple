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
package org.mutantcat.justsimple.data.tran.impl;

import org.mutantcat.justsimple.core.util.RunnableEx;
import org.mutantcat.justsimple.data.tran.TranNode;
import org.mutantcat.justsimple.data.tran.TranManager;

/**
 * 以无事务的方式执行，如果当前有事务则报错（不需要入栈）
 *
 * @author noear
 * @since 1.0
 * */
public class TranNeverImpl implements TranNode {
    public TranNeverImpl() {

    }

    @Override
    public void apply(RunnableEx runnable) throws Throwable {
        //获取当前事务
        //
        if (TranManager.current() != null) {
            //绝不能有事务
            throw new RuntimeException("Never support transactions");
        } else {
            runnable.run();
        }
    }
}

