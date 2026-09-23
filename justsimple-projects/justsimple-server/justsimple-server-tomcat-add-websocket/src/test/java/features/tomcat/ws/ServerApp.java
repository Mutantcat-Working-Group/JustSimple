package features.tomcat.ws;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.net.annotation.ServerEndpoint;
import org.mutantcat.justsimple.net.websocket.WebSocket;
import org.mutantcat.justsimple.net.websocket.listener.SimpleWebSocketListener;

import java.io.IOException;

@ServerEndpoint("/demo")
public class ServerApp extends SimpleWebSocketListener {
    public static void main(String[] args) throws Exception {
        JustSimple.start(ServerApp.class, args, app -> {
            app.enableWebSocket(true);
        });
    }

    @Override
    public void onMessage(WebSocket socket, String text) throws IOException {
        socket.send("收到" + text);
    }
}
