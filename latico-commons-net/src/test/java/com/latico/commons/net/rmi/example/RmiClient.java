package com.latico.commons.net.rmi.example;

import com.latico.commons.net.rmi.RmiUtils;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-11-15 9:50
 * @Version: 1.0
 */
public class RmiClient {
    public static void main(String[] args) throws Exception {
        String name = "hello";
        Hello hello = RmiUtils.getRmiClient("localhost", 1099, name);
        System.out.println(hello.sayHello("你好"));
        System.out.println("发送完成");
    }
}
