package com.latico.commons.websocket.server;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;

/**
 * <PRE>
 *  在调用websocket的业务处理类前，在这里创建具体的业务处理类
 *
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-04 11:45:50
 * @Version: 1.0
 */
public class WebSocketCreatorImpl implements org.eclipse.jetty.websocket.servlet.WebSocketCreator {

    public WebSocketCreatorImpl() {

    }

    /**
     * 针对连接请求，在这里创建业务处理类
     * @param req
     * @param resp
     * @return
     */
    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {

        ServerWebSocketService serverWebSocketService = null;
        /**
         *   官方的Demo，这里可以根据相应的参数做判断，使用什么样的websocket处理
         */
        for (String sub : req.getSubProtocols()) {
            System.out.println("打印客户端使用协议提示，服务端使用专门的服务端websocket处理:" + sub + " ，服务端使用处理类："+ ServerWebSocketService.class);
            serverWebSocketService = new ServerWebSocketService();
            break;
        }

//使用默认方式
        if (serverWebSocketService == null) {
            serverWebSocketService = new ServerWebSocketService();
        }

        return serverWebSocketService;

    }
}