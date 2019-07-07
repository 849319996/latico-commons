package com.latico.commons.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <PRE>
 * 群聊的websocket，客户端连接websocket：ws://localhost:8080/latico-commons-websocket-tomcat/websocket
 * 页面访问：http://localhost:8080/latico-commons-websocket-tomcat/websocket.jsp
 *
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 *
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-04 11:01:33
 * @Version: 1.0
 */
@ServerEndpoint("/websocket")
public class GroupChatWebSocketDemo {
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static AtomicLong onlineCount = new AtomicLong();
    /**
     * 所有当前连接，concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
     */
    private static CopyOnWriteArraySet<GroupChatWebSocketDemo> webSocketConnections = new CopyOnWriteArraySet<GroupChatWebSocketDemo>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        session.getAsyncRemote().sendText("欢迎加入");

//        通知所有其他用户，有新用户加入
        try {
            for (GroupChatWebSocketDemo groupChatWebSocketDemo : webSocketConnections) {
                groupChatWebSocketDemo.sendMessage("有新用户加入群聊");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //把当前用户加入set中
        webSocketConnections.add(this);

        //在线数加1
        addOnlineCount();

        System.out.println("有新连接加入！当前在线人数为：" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketConnections.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        //群发消息,体现出websocket的核心功能，主动推送
        try {
            for (GroupChatWebSocketDemo item : webSocketConnections) {
                item.sendMessage("服务器推送消息:" + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
        try {
            session.getBasicRemote().sendText("发生错误");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized long getOnlineCount() {
        return onlineCount.get();
    }

    public static synchronized void addOnlineCount() {
        GroupChatWebSocketDemo.onlineCount.incrementAndGet();
    }

    public static synchronized void subOnlineCount() {
        GroupChatWebSocketDemo.onlineCount.decrementAndGet();
    }
}