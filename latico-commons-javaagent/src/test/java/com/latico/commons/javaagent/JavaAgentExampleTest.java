package com.latico.commons.javaagent;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class JavaAgentExampleTest {

    /**
     *
     */
    @Test
    public void test1(){
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        System.out.println(list);
    }

}