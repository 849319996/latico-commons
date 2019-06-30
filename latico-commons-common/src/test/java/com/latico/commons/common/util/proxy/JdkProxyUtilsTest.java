package com.latico.commons.common.util.proxy;

import com.latico.commons.common.util.logging.LogUtils;
import com.latico.commons.common.util.proxy.jdk.JdkProxyUtils;
import com.latico.commons.common.util.proxy.jdk.expample.InterfaceExample;
import com.latico.commons.common.util.proxy.jdk.expample.InterfaceExampleImpl;
import com.latico.commons.common.util.proxy.jdk.expample.InvocationHandlerExample;
import org.junit.Test;

public class JdkProxyUtilsTest {

    @Test
    public void createProxyInstance() {
        LogUtils.loadLogBackConfigDefault();
        try {
            InterfaceExampleImpl impl = new InterfaceExampleImpl();

             InterfaceExample proxyInstance = JdkProxyUtils.createProxyInstance(InterfaceExample.class, new InvocationHandlerExample(impl));
            System.out.println(proxyInstance.get("abc"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void createProxyInstance2() {
        LogUtils.loadLogBackConfigDefault();
        try {
            InterfaceExampleImpl impl = new InterfaceExampleImpl();

             InterfaceExample proxyInstance = JdkProxyUtils.createProxyInstance(InterfaceExample.class, InvocationHandlerExample.class);
            System.out.println(proxyInstance.get("abc"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}