package com.latico.commons.javaagent.runtime;

import com.latico.commons.javaagent.onload.BeanExample;

class AccountMain {

  public static void main(String[] args) throws InterruptedException {
//    启动代理线程,代理跟业务程序在同一个jvm中
//    new JVMTIThread().start();

    while(true) {
//     这里示例是为了让该虚拟机不关闭
      new Account().operation();
      Thread.sleep(5000);
    }

  }
}