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
package org.mutantcat.justsimple.server.nettyhttp;

import java.net.InetSocketAddress;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.JustSimpleApp;
import org.mutantcat.justsimple.server.ServerConstants;
import org.mutantcat.justsimple.server.ServerProps;
import org.mutantcat.justsimple.server.prop.impl.HttpServerProps;
import org.mutantcat.justsimple.core.AppContext;
import org.mutantcat.justsimple.core.Plugin;
import org.mutantcat.justsimple.core.Signal;
import org.mutantcat.justsimple.core.SignalSim;
import org.mutantcat.justsimple.core.SignalType;
import org.mutantcat.justsimple.core.event.EventBus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class XPluginImp implements Plugin {
    static final Logger log = LoggerFactory.getLogger(XPluginImp.class);

    private static Signal _signal;

    public static Signal signal() {
        return _signal;
    }

    public static String justsimple_server_ver() {
        return "netty http/" + JustSimple.version();
    }

    NettyHttpServer _server;

    @Override
    public void start(AppContext context) {
        if (JustSimple.app().enableHttp() == false) {
            return;
        }

        context.lifecycle(ServerConstants.SIGNAL_LIFECYCLE_INDEX, () -> {
            start0(JustSimple.app());
        });
    }

    private void start0(JustSimpleApp app) throws Throwable {
        //初始化属性
        ServerProps.init();

        HttpServerProps props = HttpServerProps.getInstance();
        final String _host = props.getHost();
        final int _port = props.getPort();
        final String _name = props.getName();

        long time_start = System.currentTimeMillis();

        _server = new NettyHttpServer(
                new InetSocketAddress(_host, _port),
                props,
                JustSimple.app()::tryHandle);

        //尝试事件扩展
        EventBus.publish(_server);
        _server.start(_host, _port);

        final String _wrapHost = props.getWrapHost();
        final int _wrapPort = props.getWrapPort();
        _signal = new SignalSim(_name, _wrapHost, _wrapPort, "http", SignalType.HTTP);
        app.signalAdd(_signal);

        long time_end = System.currentTimeMillis();

        String httpServerUrl = props.buildHttpServerUrl(_server.isSecure());

        log
                .info("Connector:main: nettyhttp: Started ServerConnector@{HTTP/1.1,[http/1.1]}{"
                        + httpServerUrl + "}");
        log
                .info("Server:main: nettyhttp: Started (" + justsimple_server_ver() + ") @" + (time_end
                        - time_start) + "ms");
    }


    @Override
    public void stop() throws Throwable {
        if (_server == null) {
            return;
        }

        _server.stop();
        _server = null;
        log.info("Server:main: nettyhttp: Has Stopped (" + justsimple_server_ver() + ")");
    }
}
