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
package org.mutantcat.justsimple.server.reactornetty;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.JustSimpleApp;
import org.mutantcat.justsimple.core.AppContext;
import org.mutantcat.justsimple.core.Plugin;
import reactor.netty.DisposableServer;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.server.HttpServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * https://projectreactor.io/docs/netty/release/reference/index.html#http-server
 * */
public class XPluginImp implements Plugin {
    static final Logger log = LoggerFactory.getLogger(XPluginImp.class);

    DisposableServer _server = null;

    public static String justsimple_server_ver() {
        return "reactor-netty-http 1.0.20/" + JustSimple.version();
    }

    @Override
    public void start(AppContext context) {
        if (JustSimple.app().enableHttp() == false) {
            return;
        }

        new Thread(() -> {
            start0(JustSimple.app());
        });
    }

    private void start0(JustSimpleApp app) {
        long time_start = System.currentTimeMillis();

        try {
            RnHttpHandler handler = new RnHttpHandler();

            //
            // https://projectreactor.io/docs/netty/release/reference/index.html#_starting_and_stopping_2
            //
            _server = HttpServer.create()
                    .compress(true)
                    .protocol(HttpProtocol.HTTP11)
                    .port(app.cfg().serverPort())
                    .handle(handler)
                    .bindNow();


            _server.onDispose()
                    .block();

            long time_end = System.currentTimeMillis();

            log.info("Connector:main: reactor-netty-http: Started ServerConnector@{HTTP/1.1,[http/1.1]}{http://localhost:" + app.cfg().serverPort() + "}");
            log.info("Server:main: reactor-netty-http: Started (" + justsimple_server_ver() + ") @" + (time_end - time_start) + "ms");
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void stop() throws Throwable {
        if (_server != null) {
            _server.dispose();
            _server = null;

            log.info("Server:main: reactor-netty-http: Has Stopped (" + justsimple_server_ver() + ")");
        }
    }
}
