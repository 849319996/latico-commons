package com.latico.commons.docker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <PRE>
 *  默认主页
 * </PRE>
 * @author: latico
 * @date: 2019-03-15 10:34:32
 * @version: 1.0
 */
@RestController
@Configuration
public class HomeController {

    /**
     * 拿到端口配置
     */
    @Value("${server.port}")
    private String serverPort;

    /**
     * 拿到项目上下文路径
     */
    @Value("${server.servlet.context-path}")
    private String serverContextPath;

    @RequestMapping("")
    public String home() {
        String serverContextPath = this.serverContextPath;
        if (!serverContextPath.startsWith("/")) {
            serverContextPath = "/" + serverContextPath;
        }
        if (!serverContextPath.endsWith("/")) {
            serverContextPath = serverContextPath + "/";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Spring Boot Home!<hr/>");
        sb.append("1、Rest API调测界面: http://localhost:" + serverPort + serverContextPath + "swagger-ui.html<hr/>");
        sb.append("2、Druid监控界面: http://localhost:" + serverPort + serverContextPath + "druid<hr/>");
        return sb.toString();
    }

}
