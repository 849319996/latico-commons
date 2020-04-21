package com.catt.commons;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.junit.Test;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2020-04-17 18:02
 * @version: 1.0
 */
public class TestGroovy {
    @Test
    public void test() {
        Binding binding = new Binding();
        binding.setVariable("$name", "zhiyuan");
        binding.setVariable("$age", 28);
        binding.setVariable("$salary", 1000);
        GroovyShell shell = new GroovyShell(binding);

        String groovyExp = "return ($name.contains('hiy')  && $age > 27) || $salary <= 1000";
        Boolean val = (Boolean)shell.evaluate(groovyExp);
        if (val) {
            System.out.println(groovyExp);
        }
    }
}
