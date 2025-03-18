package org.mutantcat.justsimple.web;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import org.mutantcat.justsimple.instance.InstanceHandler;
import org.mutantcat.justsimple.request.Context;

import java.lang.reflect.Method;
import java.util.Map;

public class NettyWithController {
    private final int port; // 监听端口
    private final Map<String, Method> handlerMap; // 路径-方法映射
    private final Map<String, String> singletonMap; // 单例实例映射

    public NettyWithController(int port, Map<String, Method> handlerMap, Map<String, String> singletonMap) {
        this.port = port;
        this.handlerMap = handlerMap;
        this.singletonMap = singletonMap;
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            pipeline.addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
                                    String uri = request.uri();
                                    Method method = handlerMap.get(uri);
                                    Object singletonObject = null;


                                    // 看看单例中是否有注册的
                                    if (singletonMap.containsKey(uri)) {
                                        String singletonName = singletonMap.get(uri);
                                        singletonObject = InstanceHandler.getInstance(singletonName);
                                    }

                                    Object controllerInstance = null;
                                    if (method != null) {
                                        if (singletonObject != null) {
                                            // 若有单例对象走单例
                                            controllerInstance = singletonObject;
                                        } else {
                                            // 若没有注册的单例 或者注册的单例中没有@Handler修饰的方法 通过反射调用 Handler 方法
                                            controllerInstance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
                                        }
                                        String responseContent = (String) method.invoke(controllerInstance, new Context(request));

                                        // 构建 HTTP 响应
                                        FullHttpResponse response = new DefaultFullHttpResponse(
                                                HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                                        response.content().writeBytes(responseContent.getBytes());
                                        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                                        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                                        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                                    } else {
                                        // 返回 404
                                        FullHttpResponse response = new DefaultFullHttpResponse(
                                                HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
                                        response.content().writeBytes("404".getBytes());
                                        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                                        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                                        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                                    }
                                }
                            });
                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("JustSimple started at port " + port);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
