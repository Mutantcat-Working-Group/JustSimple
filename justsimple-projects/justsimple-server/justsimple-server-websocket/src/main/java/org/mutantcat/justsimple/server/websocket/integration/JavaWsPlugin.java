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
package org.mutantcat.justsimple.server.websocket.integration;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.Utils;
import org.mutantcat.justsimple.server.ServerConstants;
import org.mutantcat.justsimple.server.ServerProps;
import org.mutantcat.justsimple.server.prop.impl.WebSocketServerProps;
import org.mutantcat.justsimple.server.websocket.JavaWsServer;
import org.mutantcat.justsimple.core.*;
import org.mutantcat.justsimple.core.bean.LifecycleBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;

public class JavaWsPlugin implements Plugin {
    static final Logger log = LoggerFactory.getLogger(JavaWsPlugin.class);

    private static Signal _signal;

    public static Signal signal() {
        return _signal;
    }

    private JavaWsServer _server = null;

    public static String justsimple_server_ver() {
        return "org.java_websocket 1.5/" + JustSimple.version();
    }

    @Override
    public void start(AppContext context) throws Throwable {
        if (context.app().enableWebSocket() == false) {
            return;
        }

        if (context.isStarted()) {
            start0(context);
        } else {
            context.lifecycle(ServerConstants.SIGNAL_LIFECYCLE_INDEX, new LifecycleBean() {
                @Override
                public void postStart() throws Throwable {
                    start0(context);
                }
            });
        }
    }

    private void start0(AppContext context) throws Throwable {
        //初始化属性
        ServerProps.init();

        WebSocketServerProps props = WebSocketServerProps.getNew();
        final String _host = props.getHost();
        final int _port = props.getPort();

        long time_start = System.currentTimeMillis();

        if (Utils.isEmpty(_host)) {
            _server = new JavaWsServer(props, _port);
        } else {
            _server = new JavaWsServer(props, Inet4Address.getByName(_host), _port);
        }

        _server.setReuseAddr(true); //重启时，端口可立即复用
        _server.start();

        if (Utils.isNotEmpty(props.getName())) {
            _signal = new SignalSim(props.getName(), props.getWrapHost(), props.getWrapPort(), "ws", SignalType.WEBSOCKET);

            context.app().signalAdd(_signal);
        }

        long time_end = System.currentTimeMillis();

        String wsServerUrl = props.buildWsServerUrl(false);
        log.info("Connector:main: websocket: Started ServerConnector@{HTTP/1.1,[WebSocket]}{" + wsServerUrl + "}");
        log.info("Server:main: websocket: Started (" + justsimple_server_ver() + ") @" + (time_end - time_start) + "ms");
    }

    @Override
    public void stop() throws Throwable {
        if (_server != null) {
            _server.stop();
            _server = null;

            log.info("Server:main: websocket: Has Stopped (" + justsimple_server_ver() + ")");
        }
    }
}
