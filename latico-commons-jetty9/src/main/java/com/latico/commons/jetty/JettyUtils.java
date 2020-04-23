package com.latico.commons.jetty;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.Servlet;
import java.util.Map;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-05-22 11:01
 * @version: 1.0
 */
public class JettyUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JettyUtils.class);

    /**
     * 创建一个服务器
     * @param serverPort
     * @param ServletWithMappings url和Servlet的映射关系
     * @return
     */
    public static Server createServerByServletMapping(int serverPort, Map<String, Class<? extends Servlet>> ServletWithMappings) {
        Server server = new Server(serverPort);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        for (Map.Entry<String, Class<? extends Servlet>> servletStringEntry : ServletWithMappings.entrySet()) {
            handler.addServletWithMapping(servletStringEntry.getValue(), servletStringEntry.getKey());
        }
        return server;
    }

    /**
     * 比如websocket可以用到
     * @param serverPort
     * @param contextPath
     * @param serviceHandler 业务handler
     * @return
     */
    public static Server createServerByServiceHandler(int serverPort, String contextPath, Handler serviceHandler) {
        Server server = new Server(serverPort);
        ContextHandler contextHandler = new ContextHandler();
        contextHandler.setContextPath(contextPath);
        contextHandler.setHandler(serviceHandler);
        server.setHandler(contextHandler);

        return server;
    }

    /**
     * 启动服务器
     * @param server
     * @return
     */
    public static boolean startServer(Server server) {
        try {
            server.start();
            return true;
        } catch (Exception e) {
            LOG.error(e);
        }
        return false;
    }


    /**
     * 等待服务器停止
     * @param server
     * @return
     */
    public static boolean waitServerStop(Server server) {
        try {
            server.join();
            return true;
        } catch (Exception e) {
            LOG.error(e);
        }
        return false;
    }
}
