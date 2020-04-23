package com.latico.commons.spring.extend;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <PRE>
 * ApplicationContextAware的实现类，也是工具类，用于获取ApplicationContext，并提供静态方法获取bean
 * spring要使用该类，必须要把该bean注册进spring容器中
 * </PRE>
 *
 * @author: latico
 * @date: 2019-02-20 18:56
 * @version: 1.0
 */
@Component
public class ApplicationContextAwareImpl implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 获取applicationContext
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     * bean的默认name是首字母小写的类名称，也可以复写BeanNameGenerator
     * @param name
     * @return
     */
    public static <T> T getBean(String name) {
        return (T)getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 获取ApplicationContext
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (ApplicationContextAwareImpl.applicationContext == null) {
            ApplicationContextAwareImpl.applicationContext = applicationContext;
        }
    }
}
