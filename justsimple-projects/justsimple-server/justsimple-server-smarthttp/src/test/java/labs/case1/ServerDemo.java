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
package labs.case1;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.annotation.Managed;
import org.mutantcat.justsimple.server.prop.impl.HttpServerProps;
import org.mutantcat.justsimple.server.smarthttp.SmHttpServer;
import org.mutantcat.justsimple.core.bean.LifecycleBean;
import org.mutantcat.justsimple.core.handle.Context;
import org.mutantcat.justsimple.core.handle.Handler;

/**
 * @author noear 2023/4/6 created
 */
@Managed
public class ServerDemo implements LifecycleBean , Handler {
    SmHttpServer _server;

    @Override
    public void start() throws Throwable {
        _server = new SmHttpServer(HttpServerProps.getInstance());
        _server.enableWebSocket(false);
        _server.setCoreThreads(Runtime.getRuntime().availableProcessors() * 2);
        _server.setHandler(this); //如果使用 JustSimple.app()::tryHandle，则转发给 JustSimple.app()
        _server.start(null, JustSimple.cfg().serverPort() + 1);
    }

    @Override
    public void stop() throws Throwable {
        if (_server != null) {
            _server.stop();
            _server = null;
        }
    }

    @Override
    public void handle(Context ctx) throws Throwable {
        ctx.output("Hello world!");
    }
}
