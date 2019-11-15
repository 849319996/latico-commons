package com.latico.commons.webservice.server.demo;


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
public class WebServiceImpl implements WebService {

    @Override
    public String sayHello(String param) {
        return "sayHello : ".concat(param);
    }
}
