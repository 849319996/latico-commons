package com.latico.commons.common.util.proxy.jdk;


import com.latico.commons.common.util.reflect.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * <PRE>
 * JDK动态代理
 * 1、其实就是动态创建一个指定interface接口类的实例（如果不是接口类，会报异常：at java.lang.reflect.Proxy$ProxyClassFactory.apply(Proxy.java:590) is not an interface），
 * 该实例执行方法的时候，真正执行的是InvocationHandler的invoke(Object proxy, Method method, Object[] args)方法，
 * 2、
 * </PRE>
 *
 * @Author: latico
 * @Date: 2018-12-31 17:04
 * @Version: 1.0
 */
public class JdkProxyUtils {

    /**
     * @param instance 被代理的实例
     * @param interfaceClass 接口类，代理后的对象所属的类，一个
     * @param invocationHandler 代理事件处理
     * @param <T> 被代理后的对象实例
     * @return
     */
    public static <T extends Object> T createProxyInstance(T instance, Class<T> interfaceClass, InvocationHandler invocationHandler) throws Exception {
        if(interfaceClass == null){
            throw new IllegalArgumentException("代理接口类为空");
        }
        if(invocationHandler == null){
            throw new IllegalArgumentException("代理调用处理器类为空");
        }

        if (instance != null) {
            return (T)Proxy.newProxyInstance(instance.getClass().getClassLoader(), new Class[]{interfaceClass}, invocationHandler);
        }else{
            return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, invocationHandler);
        }
    }

    /**
     * @param instance 被代理的实例
     * @param invocationHandler 代理事件处理
     * @param <T> 被代理后的对象实例
     * @return
     */
    public static <T extends Object> T createProxyInstance(T instance, InvocationHandler invocationHandler) throws Exception {
        if(invocationHandler == null){
            throw new IllegalArgumentException("代理调用处理器类为空");
        }
        if(instance == null){
            throw new IllegalArgumentException("实例对象不能为空");
        }

        //获取实例的所有接口
        List<Class<?>> allInterfaces = ClassUtils.getAllInterfaces(instance.getClass());
        return (T)Proxy.newProxyInstance(instance.getClass().getClassLoader(), (Class[])allInterfaces.toArray(), invocationHandler);
    }

    /**
     * 该创建代理的方式，没有被代理的实例对象，
     * 也就是mybatis的MapperProxy处理方式：
     * {@link org.apache.ibatis.binding.MapperProxyFactory#newInstance#(org.apache.ibatis.binding.MapperProxy))
     * @param interfaceClass 代理接口类
     * @param invocationHandler 代理事件处理
     * @param <T> 被代理后的对象实例
     * @return
     */
    public static <T extends Object> T createProxyInstance(Class<T> interfaceClass, InvocationHandler invocationHandler) throws Exception {
        if(invocationHandler == null){
            throw new IllegalArgumentException("代理调用处理器类为空");
        }
        if(interfaceClass == null){
            throw new IllegalArgumentException("代理接口类为空");
        }

        //获取实例的所有接口
        return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, invocationHandler);
    }

    /**
     * 最简单的创建代理对象的方式
     * 该创建代理的方式，没有被代理的实例对象，也就是mybatis的MapperProxy处理方式
     * @param interfaceClass 代理接口类
     * @param invocationHandler 代理事件处理类,需保证有空参数构造函数存在
     * @param <T> 被代理后的对象实例
     * @return
     */
    public static <T extends Object> T createProxyInstance(Class<T> interfaceClass, Class<? extends InvocationHandler> invocationHandler) throws Exception {
        if(invocationHandler == null){
            throw new IllegalArgumentException("代理调用处理器类为空");
        }
        if(interfaceClass == null){
            throw new IllegalArgumentException("代理接口类为空");
        }

        //获取实例的所有接口
        return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, invocationHandler.newInstance());
    }
}
