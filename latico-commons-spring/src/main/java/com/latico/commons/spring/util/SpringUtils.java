package com.latico.commons.spring.util;

import com.latico.commons.spring.extend.ApplicationContextAwareImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.util.Map;


/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-01 17:01
 * @version: 1.0
 */
public class SpringUtils {

    /**
     * 获取applicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return ApplicationContextAwareImpl.getApplicationContext();
    }

    /**
     * 传入配置文件的key，获取application的配置的value，比如想获取springboot配置文件的端口，可以传入：server.port
     *
     * @param confKey 比如：server.port
     * @return
     */
    public static String getApplicationConfigByKey(String confKey) {
        return ApplicationContextAwareImpl.getApplicationContext().getEnvironment().getProperty(confKey);
    }

    /**
     * 通过name获取 Bean.
     * bean的默认name是首字母小写的类名称，也可以复写BeanNameGenerator
     *
     * @param name
     * @return
     */
    public static <T> T getBean(String name) {
        return (T) getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }


    /**
     * 通过接口class获取所有实现Bean.
     * 等同于如下写法（因为艾特符号不能在这里用，所以下面的Autowired注解去掉了前面的艾特）：
     * Autowired
     * Map<String, T> instances;
     * 或者
     * Autowired
     * List<T> instances;
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return getApplicationContext().getBeansOfType(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }


    /**
     * @param resources 资源目录spring文件
     * @return
     */
    public static ClassPathXmlApplicationContext createSpringApplicationContextByClassPathXml(String... resources) {
        return new ClassPathXmlApplicationContext(resources);
    }

    /**
     * @param filePaths 文件系统形式的spring文件
     * @return
     */
    public static FileSystemXmlApplicationContext createSpringApplicationContextByFileSystemXml(String... filePaths) {
        return new FileSystemXmlApplicationContext(filePaths);
    }

    /**
     * 通过ServletContext对象获取ApplicationContext
     *
     * @param servletContext ServletContext对象
     * @return
     */
    public static WebApplicationContext getWebApplicationContext(ServletContext servletContext) {
        return WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }

    /**
     * 创建Bean工厂
     *
     * @param resources 资源目录spring文件
     * @return
     */
    public static XmlBeanFactory createBeanFactoryByClassPathXml(String resources) {
        Resource resource = new ClassPathResource(resources);
        return new XmlBeanFactory(resource);
    }

    /**
     * 获取一个bean
     *
     * @param applicationContext
     * @param beanId             bean的名称，也就是配置bean的id值
     * @param <T>
     * @return
     */
    public static <T> T getBean(ApplicationContext applicationContext, String beanId) {
        if (applicationContext == null) {
            return null;
        }
        if (beanId == null) {
            return null;
        }
        Object bean = applicationContext.getBean(beanId);
        if (bean != null) {
            return (T) bean;
        } else {
            return null;
        }
    }

    /**
     * 获取一个bean
     *
     * @param beanFactory
     * @param beanId      bean的名称，也就是配置bean的id值
     * @param <T>
     * @return
     */
    public static <T> T getBean(BeanFactory beanFactory, String beanId) {
        if (beanFactory == null) {
            return null;
        }
        if (beanId == null) {
            return null;
        }
        Object bean = beanFactory.getBean(beanId);
        if (bean != null) {
            return (T) bean;
        } else {
            return null;
        }
    }

    /**
     * 获取一个bean
     *
     * @param applicationContext
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(ApplicationContext applicationContext, Class<T> clazz) {
        if (applicationContext == null) {
            return null;
        }
        if (clazz == null) {
            return null;
        }
        return applicationContext.getBean(clazz);
    }

    /**
     * 获取一个bean
     *
     * @param beanFactory
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(BeanFactory beanFactory, Class<T> clazz) {
        if (beanFactory == null) {
            return null;
        }
        if (clazz == null) {
            return null;
        }
        return beanFactory.getBean(clazz);
    }

}
