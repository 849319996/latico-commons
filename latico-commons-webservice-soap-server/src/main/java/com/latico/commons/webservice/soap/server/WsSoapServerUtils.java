package com.latico.commons.webservice.soap.server;

import javax.xml.ws.Endpoint;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-08 0:43
 * @Version: 1.0
 */
public class WsSoapServerUtils {

    /**
     * 发布WSDL
     * @param wsdlUrl
     * @param implementor 实现了@javax.jws.WebService注解的类
     */
    public static void publish(String wsdlUrl, Object implementor){
        Endpoint.publish(wsdlUrl, implementor);
    }
}
