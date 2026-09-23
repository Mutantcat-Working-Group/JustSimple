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
package org.mutantcat.justsimple.data.rx.sql.integration;

import io.r2dbc.spi.ConnectionFactory;
import org.mutantcat.justsimple.Utils;
import org.mutantcat.justsimple.annotation.Inject;
import org.mutantcat.justsimple.core.AppContext;
import org.mutantcat.justsimple.core.BeanWrap;
import org.mutantcat.justsimple.core.Plugin;
import org.mutantcat.justsimple.core.VarHolder;
import org.mutantcat.justsimple.data.rx.sql.RxSqlConfiguration;
import org.mutantcat.justsimple.data.rx.sql.RxSqlUtils;
import org.mutantcat.justsimple.data.rx.sql.intercept.RxSqlCommandInterceptor;

import java.util.function.Consumer;

/**
 * @author noear
 * @since 3.0
 */
public class RxSqlUtilsPlugin implements Plugin {
    @Override
    public void start(AppContext context) throws Throwable {
        context.beanInjectorAdd(Inject.class, RxSqlUtils.class, this::doInject);

        context.getWrapAsync(RxSqlCommandInterceptor.class, bw -> {
            RxSqlConfiguration.addInterceptor(bw.raw(), bw.index());
        });
    }

    private void doInject(VarHolder vh, Inject anno) {
        vh.required(anno.required());

        this.observeDs(vh.context(), anno.value(), bw -> {
            vh.setValue(RxSqlUtils.of(bw.raw()));
        });
    }

    private void observeDs(AppContext context, String dsName, Consumer<BeanWrap> consumer) {
        if (Utils.isEmpty(dsName)) {
            context.getWrapAsync(ConnectionFactory.class, (dsBw) -> {
                consumer.accept(dsBw);
            });
        } else {
            context.getWrapAsync(dsName, (dsBw) -> {
                if (dsBw.raw() instanceof ConnectionFactory) {
                    consumer.accept(dsBw);
                }
            });
        }
    }
}
