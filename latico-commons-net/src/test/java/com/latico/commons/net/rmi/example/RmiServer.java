package com.latico.commons.net.rmi.example;

import com.latico.commons.net.rmi.RmiUtils;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-11-15 9:50
 * @Version: 1.0
 */
public class RmiServer {
    public static void main(String[] args) throws Exception {
        try {
            Hello hello = new HelloImpl();
            Registry registry = RmiUtils.registryRmiServer(1099, "hello", hello);
            System.out.print(registry + " Ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
