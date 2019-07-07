package com.latico.commons.websocket.client;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-06-04 9:13
 * @Version: 1.0
 */
public class ClientMain {
    public static void main(String args[]) {
        String destUri = "ws://127.0.0.1:7778/test/";

        WebSocketClient client = new WebSocketClient();

        try {
            client.start();

//            连接地址，格式："ws://127.0.0.1:7778/test/"
            URI echoUri = new URI(destUri);

            ClientUpgradeRequest request = new ClientUpgradeRequest();
//            设置指定协议类型，服务端根据它可以判断使用什么处理类
            request.setSubProtocols("subProtocol");
            request.setHeader("index", "3");

//            相应的webSocket业务处理
            ClientWebSocketService serviceHandler = new ClientWebSocketService();

//            开始连接
            client.connect(serviceHandler, echoUri, request);

            System.out.printf("Connecting to : %s%n", echoUri);

//            等待关闭
            serviceHandler.awaitClose(1000, TimeUnit.SECONDS);

        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
