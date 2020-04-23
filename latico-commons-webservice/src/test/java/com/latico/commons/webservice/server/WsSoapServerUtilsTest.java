package com.latico.commons.webservice.server;

import com.latico.commons.webservice.server.demo.WebServiceImpl;
import org.junit.Test;

/**
 * <PRE>
 *
 * </PRE>
 * @author: latico
 * @date: 2019-06-30 00:51:08
 * @version: 1.0
 */
public class WsSoapServerUtilsTest {

    /**
     * 此类并不是一个发布Websevices的API.
     * 此类的作用是【演示】如何通过 JAX-WS 发布一个超轻量级的Websevices服务.
     */
    @Test
    public void publish() {
        WsServerUtils.publish("http://127.0.0.1:8080/wsdemo?wsdl", new WebServiceImpl());
        System.out.println("启动完成");


        try {
            Thread.sleep(10000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}