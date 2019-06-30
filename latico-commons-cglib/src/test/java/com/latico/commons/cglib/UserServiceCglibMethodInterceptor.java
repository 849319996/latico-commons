package com.latico.commons.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class UserServiceCglibMethodInterceptor implements MethodInterceptor {

    /**
     * 实现MethodInterceptor接口中重写的方法
     * 
     * 回调方法
     */
    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("==================");
        System.out.println("拦截到事务开始。。。" + method.getName());
        Object result = proxy.invokeSuper(object, args);
        System.out.println("拦截到事务结束。。。" + method.getName());
        System.out.println("==================");
        return result;
    }

}