package org.mutantcat.justsimple.web;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;
import org.mutantcat.justsimple.config.Config;
import org.mutantcat.justsimple.instance.InstanceHandler;
import org.mutantcat.justsimple.request.Context;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
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
                            // 配置跨域规则
                            CorsConfig corsConfig = ((Config) InstanceHandler.getInstance("just_simple_config")).getCorsConfig();
                            if (corsConfig != null) {
                                // 添加 CORS 处理器
                                pipeline.addLast(new CorsHandler(corsConfig));
                            }
                            pipeline.addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
                                    QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
                                    Map<String, List<String>> parameters = queryStringDecoder.parameters();
                                    String uri = queryStringDecoder.path();
                                    Method method = handlerMap.get(uri);
                                    Object singletonObject = null;
                                    String json = "";
                                    Map<String, Object> formData = new HashMap<>();

                                    if (request.method() == HttpMethod.POST) {
                                        String contentType = request.headers().get(HttpHeaderNames.CONTENT_TYPE);
                                        // 检查 Content-Type 是否为 application/json
                                        if (HttpHeaderValues.APPLICATION_JSON.contentEqualsIgnoreCase(contentType)) {
                                            // 获取请求体
                                            json = request.content().toString(CharsetUtil.UTF_8);
                                        } else if (contentType.startsWith(HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())) {
                                            // 处理 application/x-www-form-urlencoded
                                            QueryStringDecoder postDecoder = new QueryStringDecoder(request.content().toString(CharsetUtil.UTF_8), false);
                                            postDecoder.parameters().forEach((key, value) -> formData.put(key, value.get(0))); // 只取第一个值
                                        } else if (contentType.startsWith(HttpHeaderValues.MULTIPART_FORM_DATA.toString())) {
                                            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
                                            try {
                                                decoder.offer(request); // 提供数据进行解码
                                                List<InterfaceHttpData> bodyHttpDatas = decoder.getBodyHttpDatas(); // 获取所有数据片段
                                                for (InterfaceHttpData data : bodyHttpDatas) {
                                                    try {
                                                        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                                                            // 普通字段
                                                            Attribute attribute = (Attribute) data;
                                                            formData.put(attribute.getName(), attribute.getValue());
                                                        } else if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
                                                            // 文件字段
                                                            FileUpload fileUpload = (FileUpload) data;
                                                            if (fileUpload.isCompleted()) {
                                                                try {
                                                                    // 使用 FileUploadHandler 将文件保存到临时文件
                                                                    FileUploadHandler.TempFile tempFile = FileUploadHandler.saveToTemporaryFile(fileUpload, fileUpload.content());

                                                                    // 将临时文件对象存入 Map
                                                                    formData.put(fileUpload.getName(), tempFile);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                    System.err.println("Failed to save file: " + fileUpload.getFilename());
                                                                }
                                                            }
                                                        }
                                                    } finally {
                                                        data.release(); // 释放当前数据片段
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace(); // 捕获并打印解码异常
                                            } finally {
                                                decoder.destroy(); // 释放解码器资源
                                            }
                                        }

                                    }


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
                                        Parameter[] parameterList = method.getParameters();
                                        String responseContent = null;
                                        if (parameterList.length > 0) {
                                            responseContent = (String) method.invoke(controllerInstance, new Context(request, parameters, json, formData)).toString();
                                        } else {
                                            responseContent = (String) method.invoke(controllerInstance).toString();
                                        }
                                        if (responseContent == null) {
                                            responseContent = "null";
                                        }

                                        // 构建 HTTP 响应
                                        FullHttpResponse response = new DefaultFullHttpResponse(
                                                HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                                        response.content().writeBytes(responseContent.getBytes(StandardCharsets.UTF_8));
                                        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
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
