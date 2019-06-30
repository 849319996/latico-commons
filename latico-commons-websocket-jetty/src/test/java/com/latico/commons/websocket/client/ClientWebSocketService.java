package com.latico.commons.websocket.client;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.*;

import javax.websocket.OnError;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class ClientWebSocketService {

    /**
     * 统计监听，用于判断结束
     */
    public final CountDownLatch closeLatch;
    private Session session;

    public ClientWebSocketService() {
        this.closeLatch = new CountDownLatch(1);
    }

    /**
     * 一直监听归零
     * @param duration
     * @param unit
     * @return
     * @throws InterruptedException
     */
    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.printf("客户端关闭: %d - %s%n", statusCode, reason);
        this.session = null;

        // 触发监听减一位置
        this.closeLatch.countDown();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.printf("客户端连接: %s%n", session);
        this.session = session;
        try {
            String sendStr = "你好";
            System.out.println("客户端准备发送：" + sendStr + 1);
            Future<Void> fut = session.getRemote().sendStringByFuture(sendStr + 1);

            // 因为使用了future方式，等待发送完成
            fut.get(2, TimeUnit.SECONDS);
            System.out.println("客户端第一条已经发送完成：" + sendStr + 1);

            session.getRemote().sendString("客户端发送第二条消息:" + sendStr + 2);
            System.out.println("客户端第二条已经发送完成：" + sendStr + 2);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    @OnWebSocketMessage
    public void onMessage(String msg) {
        System.out.printf("接收到服务器消息: %s%n", msg);
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnWebSocketError
    public void onError(org.eclipse.jetty.websocket.api.Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
        try {
            session.getRemote().sendString("发生错误");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
