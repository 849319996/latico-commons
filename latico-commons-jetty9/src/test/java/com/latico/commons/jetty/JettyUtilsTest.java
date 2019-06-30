package com.latico.commons.jetty;

import org.eclipse.jetty.server.Server;
import org.junit.Test;

import javax.servlet.Servlet;
import java.util.HashMap;
import java.util.Map;

public class JettyUtilsTest {

    @Test
    public void createServerWithServletMapping() {
        Map<String, Class<? extends Servlet>> ServletWithMappings = new HashMap<>();
        ServletWithMappings.put("/hello", HelloServlet.class);
        Server server = JettyUtils.createServerByServletMapping(8080, ServletWithMappings);

        System.out.println(JettyUtils.startServer(server));

        System.out.println(JettyUtils.waitServerStop(server));

    }

    @Test
    public void createServerByServiceHandler() {
//
    }
}