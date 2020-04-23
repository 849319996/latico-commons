package com.latico.commons.hessian.client;

import com.latico.commons.hessian.common.HessianUtils;
import com.latico.commons.hessian.common.api.HelloService;
import com.latico.commons.hessian.common.bean.BeanExample;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-09 23:00
 * @version: 1.0
 */
public class HessianClinetExample {
    public static void main(String[] args) {

        //hessian服务端web程序主路径
        String url = "http://localhost:8080/hessian";
        HelloService helloService = null;
        try {
            helloService = HessianUtils.createHessianProxy(HelloService.class, url);
            System.out.println(helloService.helloWorld("kitty!"));

            BeanExample user = new BeanExample();
            user.setUserName("name");
            user.setAge(12);
            System.out.println(helloService.getMyInfo(user));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
