package com.latico.commons.websocket.server;

import com.latico.commons.jetty.JettyUtils;
import org.eclipse.jetty.server.Server;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-06-04 9:14
 * @Version: 1.0
 */
public class ServerMain {
    public static void main(String args[]) {

        /* webSocket的handler */
        WebSocketHandlerServiceImpl test = new WebSocketHandlerServiceImpl();

        Server server = JettyUtils.createServerByServiceHandler(7778, "/test", test);

        System.out.println(JettyUtils.startServer(server));
        System.out.println(JettyUtils.waitServerStop(server));

    }
}
