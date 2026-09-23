package labs;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.net.annotation.ServerEndpoint;
import org.mutantcat.justsimple.net.websocket.WebSocket;
import org.mutantcat.justsimple.net.websocket.WebSocketListener;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author noear 2024/11/27 created
 */
@ServerEndpoint("/")
public class WsApp implements WebSocketListener {
    public static void main(String[] args) {
        JustSimple.start(WsApp.class, args, app -> {
            app.enableWebSocket(true);
        });
    }

    @Override
    public void onOpen(WebSocket socket) {

    }

    @Override
    public void onMessage(WebSocket socket, String text) throws IOException {

    }

    @Override
    public void onMessage(WebSocket socket, ByteBuffer binary) throws IOException {

    }

    @Override
    public void onClose(WebSocket socket) {

    }

    @Override
    public void onError(WebSocket socket, Throwable error) {

    }
}
