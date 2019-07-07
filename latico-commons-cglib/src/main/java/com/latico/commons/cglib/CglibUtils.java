package com.latico.commons.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * <PRE>
 * Cglib动态代理
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-05-21 17:53
 * @Version: 1.0
 */
public class CglibUtils {

    /**
     * 创建一个代理实例
     * @param instanceClass 被代理的类
     * @param interceptors 拦截器，可以多个
     * @return 被代理后的对象实例
     * @throws Exception
     */
    public static <T extends Object> T createProxyInstance(Class<T> instanceClass, MethodInterceptor... interceptors) throws Exception {
        if(instanceClass == null){
            throw new IllegalArgumentException("实例对象不能为空");
        }
        if(interceptors == null){
            throw new IllegalArgumentException("拦截器不能为空");
        }

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(instanceClass);
        // 设置回调方法
        enhancer.setCallbacks(interceptors);
        // 创建代理对象
        return (T)enhancer.create();
    }

    /**
     * 创建一个代理实例
     * @param instanceClass 被代理的类
     * @param interceptorClasses 拦截器类
     * @return 被代理后的对象实例
     * @throws Exception
     */
    public static <T extends Object> T createProxyInstance(Class<T> instanceClass, Class<? extends MethodInterceptor>... interceptorClasses) throws Exception {
        if(instanceClass == null){
            throw new IllegalArgumentException("被代理类不能为空");
        }
        if(interceptorClasses == null){
            throw new IllegalArgumentException("拦截器不能为空");
        }

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(instanceClass);
        MethodInterceptor[] interceptors = new MethodInterceptor[interceptorClasses.length];

        for (int i = 0; i < interceptorClasses.length; i++) {
            interceptors[i] = interceptorClasses[i].newInstance();
        }


        // 设置回调方法
        enhancer.setCallbacks(interceptors);
        // 创建代理对象
        return (T)enhancer.create();
    }
}
