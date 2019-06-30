package com.latico.commons.webservice.soap.server.demo;


/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-04-09 13:00
 * @Version: 1.0
 */
// 注解中必须指定接口类位置, 否则会报错 "Could not load Webservice SEI"
//@javax.jws.WebService(endpointInterface= "com.latico.commons.webservice.soap.server.IWsdlService")
@javax.jws.WebService
public class WebServiceImpl implements WebService {

    @Override
    public String sayHello(String param) {
        return "webservice-demo : foo-".concat(param);
    }
}
