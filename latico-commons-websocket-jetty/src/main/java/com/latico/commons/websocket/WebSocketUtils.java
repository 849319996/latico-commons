package com.latico.commons.websocket;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-06-04 13:40
 * @Version: 1.0
 */
public class WebSocketUtils {

    /**
     * 创建一个websocket的客户端
     *
     * @param hostname
     * @param port
     * @param contextPath 上下文
     * @param webSocketServiceHandler 相应的webSocket业务处理，需要使用@WebSocket注解的类
     * @param headers 可选，头部信息
     * @param subProtocols 可选，设置指定协议类型，服务端根据它可以判断使用什么处理类
     * @return
     * @throws Exception
     */
    public static WebSocketClient createWebSocketClient(String hostname, int port, String contextPath, Object webSocketServiceHandler, Map<String, List<String>> headers, String[] subProtocols) throws Exception {
        if (contextPath == null) {
            contextPath = "/";
        }
        if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }

//        连接地址，格式："ws://127.0.0.1:7778/test/"
        String uriStr = StringUtils.join("ws://", hostname, ":", port, contextPath);
        URI uri = new URI(uriStr);
        WebSocketClient client = new WebSocketClient();

        ClientUpgradeRequest request = new ClientUpgradeRequest();

        if (subProtocols != null) {
            request.setSubProtocols(subProtocols);
        }
        request.setHeader("index", "3");

//            开始连接
        client.start();
        client.connect(webSocketServiceHandler, uri, request);

        return client;
    }
}
