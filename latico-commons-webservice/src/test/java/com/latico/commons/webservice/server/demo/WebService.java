package com.latico.commons.webservice.server.demo;


import javax.jws.WebMethod;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-04-09 13:00
 * @version: 1.0
 */
@javax.jws.WebService
public interface WebService {

    @WebMethod
    public String sayHello(String param);
}
