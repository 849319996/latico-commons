package com.latico.commons.javaagent.runtime;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.IOException;
import java.util.List;

/**
 * 获取另外一个虚拟机，并对另外一个虚拟机执行加载agent操作
 */
class JVMTIMain{

  public static void main(String[] args) throws Exception {
    List<VirtualMachineDescriptor> list = VirtualMachine.list();

    String jarFile = "./latico-commons-javaagent/lib/jvm-agent.jar";
    System.out.println(jarFile);

    for (VirtualMachineDescriptor vmd : list) {

      //      找到AccountMain启动类启动的虚拟机
      if (vmd.displayName().endsWith("AccountMain")) {
        VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());

        virtualMachine.loadAgent(jarFile, "cxs");
        System.out.println("call finish");
        virtualMachine.detach();
      }
    }
  }

}