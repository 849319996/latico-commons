package com.latico.commons.net.rmi.example;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloImpl extends UnicastRemoteObject implements Hello {
    // 这个实现必须有一个显式的构造函数，并且要抛出一个RemoteException异常  
    protected HelloImpl() throws RemoteException {
        super();
    }

    /**
     * 说明清楚此属性的业务含义
     */
    private static final long serialVersionUID = 4077329331699640331L;

    public String sayHello(String name) throws RemoteException {
        return "Hello " + name + " ^_^ ";
    }


}