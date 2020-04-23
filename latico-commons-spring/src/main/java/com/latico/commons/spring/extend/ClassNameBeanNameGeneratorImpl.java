package com.latico.commons.spring.extend;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

/**
 * <PRE>
 * 自定义bean命名策略，使用类全名称
 * spring核心配置文件需要做如下配置：
 * <context:component-scan base-package="com.latico.commons.spring" name-generator="com.latico.commons.spring.extend.ClassNameBeanNameGeneratorImpl"/>
 *
 * 当使用了本自定义名称策略后，
 * 1、如果是通过bean的名称，也就是ID，从ApplicationContext中获取bean的时候，需要使用实现类bean的类全名称；
 * 2、如果是通过类对象获取bean，那可以使用实现类的bean或者接口bean都可以。
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-01 18:16
 * @version: 1.0
 */
public class ClassNameBeanNameGeneratorImpl implements BeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        return definition.getBeanClassName();
    }
}
