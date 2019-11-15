package com.latico.commons.net.rmi.example;

import java.rmi.Remote;

public interface Hello extends Remote {

    public String sayHello(String name) throws java.rmi.RemoteException;

}