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
package org.mutantcat.justsimple.net.stomp.broker;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.Utils;
import org.mutantcat.justsimple.core.BeanWrap;
import org.mutantcat.justsimple.core.util.RankEntity;
import org.mutantcat.justsimple.lang.Preview;
import org.mutantcat.justsimple.net.annotation.ServerEndpoint;
import org.mutantcat.justsimple.net.stomp.StompEmitter;
import org.mutantcat.justsimple.net.stomp.broker.impl.BrokerMediaStompListener;
import org.mutantcat.justsimple.net.stomp.broker.impl.StompBrokerMedia;
import org.mutantcat.justsimple.net.stomp.handle.ForwardStompListener;
import org.mutantcat.justsimple.net.stomp.listener.StompListener;
import org.mutantcat.justsimple.net.websocket.WebSocketListener;
import org.mutantcat.justsimple.net.websocket.WebSocketListenerSupplier;

import java.util.Arrays;
import java.util.Collections;

/**
 * Stomp 经理人
 *
 * @author noear
 * @since 3.0
 */
@Preview("3.0")
public class StompBroker implements WebSocketListenerSupplier {
    //WebSocket 监听器
    protected final ToStompWebSocketListener webSocketListener;
    //Broker 媒介（用于内存东西）
    protected final StompBrokerMedia brokerMedia;

    public StompBroker() {
        ServerEndpoint serverEndpoint = getClass().getAnnotation(ServerEndpoint.class);
        if (serverEndpoint == null || Utils.isEmpty(serverEndpoint.value())) {
            throw new IllegalArgumentException("Endpoint is not empty");
        }

        String path = JustSimple.cfg().getByTmpl(serverEndpoint.value());

        //初始化
        brokerMedia = new StompBrokerMedia();
        webSocketListener = new ToStompWebSocketListener(path, brokerMedia);

        //注册到容器
        BeanWrap bw = JustSimple.context().wrap(path, brokerMedia.emitter);
        JustSimple.context().putWrap(path, bw);
        JustSimple.context().putWrap(StompEmitter.class, bw);

        //添加处理监听
        this.addListener(999, new BrokerMediaStompListener(brokerMedia));
        this.addListener(998, new ForwardStompListener(brokerMedia));
    }

    /**
     * 设置经理前缀
     */
    protected void setBrokerDestinationPrefixes(String... destinationPrefixes) {
        brokerMedia.brokerDestinationPrefixes.addAll(Arrays.asList(destinationPrefixes));
    }

    /**
     * 添加服务端监听器
     */
    protected void addListener(StompListener listener) {
        addListener(0, listener);
    }

    /**
     * 添加服务端监听器
     */
    protected void addListener(int index, StompListener listener) {
        brokerMedia.listeners.add(new RankEntity<>(listener, index));
        Collections.sort(brokerMedia.listeners);
    }

    /**
     * 获取 WebSocket 监听器（对接 WebSocket 体系）
     */
    @Override
    public WebSocketListener getWebSocketListener() {
        return webSocketListener;
    }

    /**
     * 获取服务端发送器
     */
    public StompEmitter getEmitter() {
        return brokerMedia.emitter;
    }
}