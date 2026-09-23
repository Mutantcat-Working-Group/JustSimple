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
import org.mutantcat.justsimple.data.annotation.Transaction;
import org.mutantcat.justsimple.data.tran.TranNode;
import org.mutantcat.justsimple.data.tran.TranManager;

/**
 * 数据新事务实现。新建一个事务，同时将当前事务挂起（需要入栈）
 *
 * @author noear
 * @since 1.0
 * */
public class TranDbNewImpl extends DbTran implements TranNode {

    public TranDbNewImpl(Transaction meta) {
        super(meta);
    }

    @Override
    public void apply(RunnableEx runnable) throws Throwable {
        TranManager.with(null, () -> {
            super.execute(() -> {
                runnable.run();
            });
        });

        //尝试挂起事务
        //
//        DbTran tran = TranManager.trySuspend();
//
//        try {
//            super.execute(() -> {
//                runnable.run();
//            });
//        } finally {
//            //尝试恢复事务
//            TranManager.tryResume(tran);
//        }
    }
}