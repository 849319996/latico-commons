package com.latico.commons.net.rmi.example;

import java.rmi.registry.LocateRegistry;

public class Hello_RMI_Server {
    public static void main(String[] args) {
        try {
            Hello hello = new HelloImpl();
            LocateRegistry.createRegistry(1099);
            java.rmi.Naming.rebind("rmi://localhost:1099/hello", hello);
            System.out.print("Ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}