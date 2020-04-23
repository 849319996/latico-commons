package com.catt.commons.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * <PRE>
 * GroovyScriptEngine 工具
 *
 * GroovyScriptEngine可以从url（文件夹，远程协议地址，jar包）等位置动态加装resource（script或则Class），同时对
 * 编译后的class字节码进行了缓存，当文件内容更新或者文件依赖的类更新时，会自动更新缓存。
 * </PRE>
 *
 * @author: latico
 * @date: 2020-04-23 13:56
 * @version: 1.0
 */
public class GroovyScriptEngineUtils {

    /**
     * 执行
     * @param groovyScriptUrl 要执行脚本的URL（文件夹，远程协议地址，jar包）等位置
     * @param scriptName 如果存在就会更新
     * @param variables 变量
     * @param <T>
     * @return 返回结果
     * @throws Exception
     */
    public static <T> T evaluate(String groovyScriptUrl, String scriptName, Map<String, Object> variables) throws Exception {
        Binding binding = new Binding();
        if (variables != null && !variables.isEmpty()) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                binding.setProperty(entry.getKey(), entry.getValue());
            }
        }
        GroovyScriptEngine engine = new GroovyScriptEngine(groovyScriptUrl);
        return (T)engine.run(scriptName, binding);
    }

}
