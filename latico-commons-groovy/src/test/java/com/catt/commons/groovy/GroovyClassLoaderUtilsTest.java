package com.catt.commons.groovy;

import groovy.lang.GroovyObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class GroovyClassLoaderUtilsTest {

    @Test
    public void evaluate() {
        String helloClassScript = "package com.util\n" + "\n" + "class GroovyHello {\n" + "    String sayHello(String name) {\n"
                + "        print 'GroovyHello call '\n" + "        name\n" + "    }\n" + "}";

        try {
            Object result = GroovyClassLoaderUtils.evaluate(helloClassScript, "sayHello", "zhangsan");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}