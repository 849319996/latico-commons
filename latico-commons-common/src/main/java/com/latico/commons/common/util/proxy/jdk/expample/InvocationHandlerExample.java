package com.latico.commons.common.util.proxy.jdk.expample;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <PRE>
 * 动态代理调用处理器
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2018-12-31 19:42
 * @Version: 1.0
 */
public class InvocationHandlerExample implements InvocationHandler {
    private static final Logger LOG = LoggerFactory.getLogger(InvocationHandlerExample.class);

    /**
     * 被代理对象
     */
    private InterfaceExample instance;

    /**
     * 不传入被代理对象
     */
    public InvocationHandlerExample() {
    }

    /**
     * 传入被代理对象
     * @param instance
     */
    public InvocationHandlerExample(InterfaceExample instance) {
        this.instance = instance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        LOG.info("{}-{}方法执行开始", method.getDeclaringClass(), method.getName());
        Object result = null;
        if(instance != null){
            try {
                //利用被代理对象去执行方法
                result = method.invoke(instance, args);
            } catch (Throwable e) {
                LOG.error("动态代理执行异常:{}-{}", e, method.getDeclaringClass(), method.getName());
            }
        }
        LOG.info("{}-{}方法执行完毕:{}", method.getDeclaringClass(), method.getName(), result);
        return result;
    }
}
