package org.mutantcat.justsimple.web;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;
import org.mutantcat.justsimple.config.Config;
import org.mutantcat.justsimple.instance.InstanceHandler;
import org.mutantcat.justsimple.request.Context;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NettyWithController {
    private static final int DEFAULT_MAX_CONTENT_LENGTH = 65536;
    private static final String TEXT_PLAIN_UTF8 = "text/plain; charset=UTF-8";

    private final int port;
    private final Map<String, Method> handlerMap;
    private final Map<String, String> singletonMap;

    public NettyWithController(int port, Map<String, Method> handlerMap, Map<String, String> singletonMap) {
        this.port = port;
        this.handlerMap = handlerMap != null ? handlerMap : Collections.<String, Method>emptyMap();
        this.singletonMap = singletonMap != null ? singletonMap : Collections.<String, String>emptyMap();
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
                            pipeline.addLast(new HttpObjectAggregator(DEFAULT_MAX_CONTENT_LENGTH));
                            // 配置跨域规则
                            Config configInstance = InstanceHandler.getInstance("just_simple_config");
                            if (configInstance != null) {
                                CorsConfig corsConfig = configInstance.getCorsConfig();
                                if (corsConfig != null) {
                                    // 添加 CORS 处理器
                                    pipeline.addLast(new CorsHandler(corsConfig));
                                }
                            }
                            pipeline.addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
                                    handleRequest(ctx, request);
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                    System.err.println("Netty 处理器异常: " + cause.getMessage());
                                    if (ctx.channel().isActive()) {
                                        writeTextResponse(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR, "500");
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

    private void handleRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        try {
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
            Map<String, List<String>> parameters = queryStringDecoder.parameters();
            String uri = queryStringDecoder.path();
            Method method = handlerMap.get(uri);
            String json = "";
            Map<String, Object> formData = new HashMap<>();

            if (request.method() == HttpMethod.POST) {
                json = parsePostBody(request, formData);
            }

            // 看看单例中是否有注册的
            String singletonName = singletonMap.get(uri);
            Object singletonObject = singletonName == null ? null : InstanceHandler.getInstance(singletonName);

            if (method == null) {
                writeTextResponse(ctx, HttpResponseStatus.NOT_FOUND, "404");
                return;
            }

            Object controllerInstance;
            if (singletonObject != null) {
                controllerInstance = singletonObject;
            } else {
                controllerInstance = newControllerInstance(method);
            }

            Object resultObj;
            Parameter[] parameterList = method.getParameters();
            if (parameterList.length > 0) {
                resultObj = method.invoke(controllerInstance, new Context(request, parameters, json, formData));
            } else {
                resultObj = method.invoke(controllerInstance);
            }

            String responseContent = resultObj == null ? "null" : resultObj.toString();
            writeTextResponse(ctx, HttpResponseStatus.OK, responseContent);
        } catch (Exception e) {
            System.err.println("处理请求失败: " + e.getMessage());
            writeTextResponse(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR, "500");
        }
    }

    private Object newControllerInstance(Method method) throws Exception {
        return method.getDeclaringClass().getDeclaredConstructor().newInstance();
    }

    private String parsePostBody(FullHttpRequest request, Map<String, Object> formData) {
        String contentType = request.headers().get(HttpHeaderNames.CONTENT_TYPE);
        if (contentType == null) {
            return "";
        }
        if (contentType.toLowerCase().contains("application/json")) {
            return request.content().toString(CharsetUtil.UTF_8);
        }
        if (contentType.startsWith("application/x-www-form-urlencoded")) {
            QueryStringDecoder postDecoder = new QueryStringDecoder(request.content().toString(CharsetUtil.UTF_8), false);
            postDecoder.parameters().forEach((key, value) -> formData.put(key, value.get(0)));
            return "";
        }
        if (contentType.startsWith("multipart/form-data")) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
            try {
                decoder.offer(request);
                List<InterfaceHttpData> bodyHttpDatas = decoder.getBodyHttpDatas();
                for (InterfaceHttpData data : bodyHttpDatas) {
                    try {
                        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                            Attribute attribute = (Attribute) data;
                            formData.put(attribute.getName(), attribute.getValue());
                        } else if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
                            FileUpload fileUpload = (FileUpload) data;
                            if (fileUpload.isCompleted()) {
                                try {
                                    FileUploadHandler.TempFile tempFile = FileUploadHandler.saveToTemporaryFile(fileUpload, fileUpload.content());
                                    formData.put(fileUpload.getName(), tempFile);
                                } catch (Exception e) {
                                    System.err.println("Failed to save file " + fileUpload.getFilename() + ": " + e.getMessage());
                                }
                            }
                        }
                    } finally {
                        data.release();
                    }
                }
            } catch (Exception e) {
                System.err.println("解析 multipart 失败: " + e.getMessage());
            } finally {
                decoder.destroy();
            }
            return "";
        }
        return "";
    }

    private void writeTextResponse(ChannelHandlerContext ctx, HttpResponseStatus status, String content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        response.content().writeBytes(bytes);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, status.code() == 404 || status.code() == 500 ? "text/plain" : TEXT_PLAIN_UTF8);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}