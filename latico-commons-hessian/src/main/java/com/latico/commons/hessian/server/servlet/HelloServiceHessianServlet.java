package com.latico.commons.hessian.server.servlet;

import com.latico.commons.hessian.common.api.HelloService;
import com.latico.commons.hessian.common.bean.BeanExample;
import com.caucho.hessian.server.HessianServlet;

import java.util.HashMap;
import java.util.Map;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-09 22:29
 * @Version: 1.0
 */
public class HelloServiceHessianServlet extends HessianServlet implements HelloService {
    @Override
    public String helloWorld(String message) {
        System.out.println("来自客户端的请求:" + message);
        return "hessian server:" + message;
    }

    @Override
    public BeanExample getMyInfo(BeanExample user) {
        System.out.println("来自客户端的请求:" + user);

        if (user == null) {
            user = new BeanExample();
        }
        Map<String, Object> detailData = new HashMap<>();
        detailData.put("hession server:", "你好");
        user.setDetailData(detailData);
        return user;
    }
}
