package com.latico.commons.spring.test;

import com.latico.commons.spring.util.SpringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = {"classpath:test/spring-annotation.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringAnnotationTest extends AbstractJUnit4SpringContextTests {
    @Autowired
    Person person;

    @Autowired
    Study study;

    @Test
    public void scanComponent() {
//        ClassPathXmlApplicationContext applicationContext = SpringUtils.createSpringApplicationContextByClassPathXml("spring-annotation.xml");
//        applicationContext.start();
        System.out.println(person.doSomeThing("abc"));
        System.out.println(study.doSomeThing("abc"));

        Person person = applicationContext.getBean(Person.class);
        System.out.println(person.doSomeThing("abc"));
        Study study = applicationContext.getBean(Study.class);
        System.out.println(study.doSomeThing("abc"));

        person = applicationContext.getBean(Student.class);
        System.out.println(person.doSomeThing("abc"));
        study = applicationContext.getBean(com.latico.commons.spring.test.bean.Student.class);
        System.out.println(study.doSomeThing("abc"));

        person = SpringUtils.getBean(applicationContext, "Student");
        System.out.println(person.doSomeThing("abc"));
        study = SpringUtils.getBean(applicationContext, "Student");
        System.out.println(study.doSomeThing("abc"));

//        applicationContext.destroy();
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
}