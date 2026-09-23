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
package org.mutantcat.justsimple.docs.integration;

import org.mutantcat.justsimple.Utils;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.cloud.model.Discovery;
import org.mutantcat.justsimple.core.AppContext;
import org.mutantcat.justsimple.core.BeanWrap;
import org.mutantcat.justsimple.core.LoadBalance;
import org.mutantcat.justsimple.core.bean.LifecycleBean;
import org.mutantcat.justsimple.core.event.EventListener;
import org.mutantcat.justsimple.docs.DocDocket;
import org.mutantcat.justsimple.docs.integration.properties.DiscoverProperties;

import java.util.Collection;

/**
 * 服务发现事件监听器
 *
 * @author noear
 * @since 2.9
 */
public class DiscoverLocator implements LifecycleBean, EventListener<Discovery> {
    private final AppContext appContext;
    private final DiscoverProperties discover;

    public DiscoverLocator(AppContext appContext, DiscoverProperties discover) {
        this.appContext = appContext;
        this.discover = discover;
    }

    /**
     * 开始
     */
    @Override
    public void start() {
        if (Utils.isNotEmpty(discover.getIncludedServices())) {
            for (String name : discover.getIncludedServices()) {
                register(name);
            }
        }

        Collection<String> serviceNames = CloudClient.discovery().findServices("");
        if (Utils.isNotEmpty(serviceNames)) {
            for (String name : serviceNames) {
                register(name);
            }
        }
    }

    @Override
    public void onEvent(Discovery discovery) throws Throwable {
        syncStatus(discovery);
    }

    /**
     * 注册
     */
    protected void register(String serviceName) {
        if (discover.getExcludedServices().contains(serviceName)) {
            //排除
            return;
        }

        Object tmp = appContext.getBean(serviceName);

        if (tmp == null) {
            //自动创建（如果还没有）
            String uri = discover.getUriPattern().replace("{service}", serviceName);
            String contextPath = discover.getContextPathPattern().replace("{service}", serviceName);

            DocDocket docDocket = new DocDocket();
            docDocket.groupName(serviceName);
            docDocket.upstream(serviceName, contextPath, uri);

            if (Utils.isNotEmpty(discover.getBasicAuth())) {
                docDocket.basicAuth().putAll(discover.getBasicAuth());
            }


            BeanWrap beanWrap = appContext.wrap(serviceName, docDocket);
            appContext.putWrap(serviceName, beanWrap);

            //预热获取
            LoadBalance.get(serviceName);
        }
    }

    /**
     * 同步状态
     */
    protected void syncStatus(Discovery discovery) throws Throwable {
        if (discover.isSyncStatus() == false) {
            return;
        }

        Object tmp = appContext.getBean(discovery.service());

        //如果要同步状态
        if (tmp instanceof DocDocket) {
            DocDocket docDocket = (DocDocket) tmp;
            docDocket.enable(discovery.clusterSize() > 0);
        }
    }
}