package com.latico.commons.webservice.server;

import javax.xml.ws.Endpoint;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-08 0:43
 * @version: 1.0
 */
public class WsServerUtils {

    /**
     * 发布WSDL
     * @param wsdlUrl
     * @param implementor 实现了@javax.jws.WebService注解的类
     */
    public static void publish(String wsdlUrl, Object implementor){
        Endpoint.publish(wsdlUrl, implementor);
    }
}
