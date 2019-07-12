package com.latico.commons.javaagent.runtime;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.util.List;

/**
 * 获取另外一个虚拟机，并对另外一个虚拟机执行加载agent操作，内部线程的方式
 */
class JVMTIThread extends Thread {

  @Override
  public void run() {
    //
    List<VirtualMachineDescriptor> list = VirtualMachine.list();

    String jarFile = "./latico-commons-javaagent/lib/jvm-agent.jar";
    System.out.println(jarFile);

    while (true) {
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      try {


        for (VirtualMachineDescriptor vmd : list) {

          //      找到AccountMain启动类启动的虚拟机
          if (vmd.displayName().endsWith("AccountMain")) {
            VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());

            virtualMachine.loadAgent(jarFile, "cxs");
            System.out.println("call finish");
            virtualMachine.detach();
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

    }

  }

}