package com.latico.commons.websocket.server;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * <PRE>
 *  可以在{@link WebSocketCreatorImpl}类中的创建控制成单例
 *  
 * </PRE>
 * @author: latico
 * @date: 2019-06-04 11:43:57
 * @version: 1.0
 */
@WebSocket(maxTextMessageSize = 128 * 1024, maxBinaryMessageSize = 128 * 1024)
public class ServerWebSocketService {

    @OnWebSocketConnect
    public void onText(Session session) throws Exception {
        if (session.isOpen()) {
            //System.out.printf("返回消息 [%s]%n","ss");
            session.getRemote().sendString("服务器发送数据： 测试001");
            System.out.println("============================================");
            Future<Void> fut;
            fut = session.getRemote().sendStringByFuture("Hello");
            try {
                fut.get(2, TimeUnit.SECONDS);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                fut = session.getRemote().sendStringByFuture(df.format(new Date()));
                fut.get(2, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @OnWebSocketClose
    public void onWebSocketBinary(int i, String string) {
        System.out.println("关闭");


    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        System.out.println("服务器已经收到消息 " + msg);

    }

    public void onWebSocketBinary(org.eclipse.jetty.websocket.api.Session session, int a, java.lang.String s) {


    }


}
