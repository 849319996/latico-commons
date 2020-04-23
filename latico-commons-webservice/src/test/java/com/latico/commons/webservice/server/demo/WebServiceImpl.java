package com.latico.commons.webservice.server.demo;


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
public class WebServiceImpl implements WebService {

    @Override
    public String sayHello(String param) {
        return "sayHello : ".concat(param);
    }
}
