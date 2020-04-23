package com.catt.commons.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import javax.script.Bindings;
import java.util.Map;

/**
 * <PRE>
 * GroovyShell方式工具
 *
 * 直接使用GroovyShell，执行groovy脚本片段，GroovyShell每一次执行时代码时会动态将代码编译成java class，
 * 然后生成java对象在java虚拟机上执行，所以如果使用GroovyShell会造成class太多，性能较差。
 * </PRE>
 *
 * @author: latico
 * @date: 2020-04-23 13:55
 * @version: 1.0
 */
public class GroovyShellUtils {

    /**
     * 执行
     * @param groovyScript 脚本
     * @param variables 变量
     * @param <T>
     * @return
     */
    public static <T> T evaluate(String groovyScript, Map<String, Object> variables) {
        Binding binding = new Binding();
        if (variables != null && !variables.isEmpty()) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                binding.setProperty(entry.getKey(), entry.getValue());
            }
        }
        GroovyShell shell = new GroovyShell(binding);
        return (T)shell.evaluate(groovyScript);
    }
}
