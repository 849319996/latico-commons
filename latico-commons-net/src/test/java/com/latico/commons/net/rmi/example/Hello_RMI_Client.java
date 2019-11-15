package com.latico.commons.net.rmi.example;

import java.rmi.Naming;

public class Hello_RMI_Client {
    public static void main(String[] args) {
        try {
            Hello hello = (Hello) Naming.lookup("rmi://localhost:1099/hello");
            System.out.println(hello.sayHello("xiaoming"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}