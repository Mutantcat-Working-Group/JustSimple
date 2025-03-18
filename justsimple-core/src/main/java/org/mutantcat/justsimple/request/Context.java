package org.mutantcat.justsimple.request;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class Context {
    FullHttpRequest fullHttpRequest;
    ChannelHandlerContext channelHandlerContext;

    public Context(FullHttpRequest fullHttpRequest) {
        this.fullHttpRequest = fullHttpRequest;
    }

    public FullHttpRequest getFullHttpRequest(){
        return fullHttpRequest;
    }

    public ChannelHandlerContext getChannelHandlerContext(){
        return channelHandlerContext;
    }

}
