package com.latico.commons.webservice.soap.server.demo;


import javax.jws.WebMethod;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-04-09 13:00
 * @Version: 1.0
 */
@javax.jws.WebService
public interface WebService {

    @WebMethod
    public String sayHello(String param);
}
