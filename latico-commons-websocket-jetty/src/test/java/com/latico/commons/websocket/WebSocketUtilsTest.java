package com.latico.commons.websocket;

import com.latico.commons.websocket.client.ClientWebSocketService;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class WebSocketUtilsTest {

    @Test
    public void createWebSocketClient() throws Exception {
//        String destUri = "ws://localhost:8080/latico-commons-websocket-tomcat/websocket";

        ClientWebSocketService webSocketService = new ClientWebSocketService();
        WebSocketClient client = WebSocketUtils.createWebSocketClient("localhost", 8080, "/latico-commons-websocket-tomcat/websocket", webSocketService, null, new String[]{"test"});

        webSocketService.awaitClose(1, TimeUnit.SECONDS);

        while (true) {
            if (webSocketService.closeLatch.getCount() == 0) {
                break;
            }
            Thread.sleep(3000);
        }
    }
}