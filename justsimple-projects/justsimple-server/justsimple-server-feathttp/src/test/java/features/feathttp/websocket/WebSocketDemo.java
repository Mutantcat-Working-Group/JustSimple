package features.feathttp.websocket;

import org.mutantcat.justsimple.net.annotation.ServerEndpoint;
import org.mutantcat.justsimple.net.websocket.WebSocket;
import org.mutantcat.justsimple.net.websocket.listener.SimpleWebSocketListener;

@ServerEndpoint("/")
public class WebSocketDemo extends SimpleWebSocketListener {
    @Override
    public void onOpen(WebSocket socket) {

    }

    @Override
    public void onMessage(WebSocket socket, String text) {
        System.out.println("当前线程是否是虚拟线程: " + ThreadUtil.isVirtualThread());
        socket.send("我收到了：" + text);
    }
}