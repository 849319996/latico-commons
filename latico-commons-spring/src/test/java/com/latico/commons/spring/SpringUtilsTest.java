package com.latico.commons.spring;

import com.latico.commons.spring.test.Person;
import com.latico.commons.spring.test.Study;
import com.latico.commons.spring.test.bean.ConfigBean;
import com.latico.commons.spring.util.SpringUtils;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringUtilsTest {

    @Test
    public void beanId() {
        ClassPathXmlApplicationContext applicationContext = SpringUtils.createSpringApplicationContextByClassPathXml("test/spring-beanId.xml");
        applicationContext.start();
        Person person = SpringUtils.getBean(applicationContext, "person");
        System.out.println(person.doSomeThing("abc"));
        Study study = SpringUtils.getBean(applicationContext, "study");
        System.out.println(study.doSomeThing("abc"));
        applicationContext.destroy();
    }
    @Test
    public void beanType() {
        ClassPathXmlApplicationContext applicationContext = SpringUtils.createSpringApplicationContextByClassPathXml("test/spring-beanId.xml");
        applicationContext.start();
        Person person = SpringUtils.getBean(applicationContext, Person.class);
        System.out.println(person.doSomeThing("abc"));

        Study study = SpringUtils.getBean(applicationContext, Study.class);
        System.out.println(study.doSomeThing("abc"));
        applicationContext.destroy();
    }

    @Test
    public void createSpringApplicationContextByFileSystemXml() {
    }

    @Test
    public void getWebApplicationContext() {
    }

    @Test
    public void createBeanFactoryByClassPathXml() {
    }

    @Test
    public void getBean() {
        System.out.println(ConfigBean.getInstance());
    }
}