package com.catt.commons.groovy;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;

/**
 * <PRE>
 *     建议使用
 * java的ScriptEngineManager 方式实现
 * groovy遵循JSR 223标准，可以使用jdk的标准接口ScriptEngineManager调用。
 * </PRE>
 *
 * @author: latico
 * @date: 2020-04-23 10:08
 * @version: 1.0
 */
public class GroovyScriptEngineManagerUtils {

    /**
     * 脚本引擎管理器
     */
    public static final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

    /**
     * 获取一个groovy脚本引擎
     * @return
     */
    public static ScriptEngine getGroovyScriptEngine(){
        ScriptEngine engine = scriptEngineManager.getEngineByName("groovy");
        return  engine;
    }

    /**
     * 执行脚本
     * @param groovyScript 脚本
     * @param variables 变量，可以为空
     * @param <T> 结果泛型
     * @return 执行结果
     * @throws ScriptException
     */
    public static <T> T evaluate(String groovyScript, Map<String, Object> variables) throws ScriptException {
        ScriptEngine groovyScriptEngine = getGroovyScriptEngine();
        return evaluate(groovyScriptEngine, groovyScript, variables);
    }

    /**
     * 执行脚本
     * @param groovyScriptEngine 引擎
     * @param groovyScript 脚本
     * @param variables 变量，可以为空
     * @param <T> 结果泛型
     * @return 执行结果
     * @throws ScriptException
     */
    public static <T> T evaluate(ScriptEngine groovyScriptEngine, String groovyScript, Map<String, Object> variables) throws ScriptException {
        if (variables == null || variables.isEmpty()) {
            return (T) groovyScriptEngine.eval(groovyScript);
        } else {
            Bindings bindings = groovyScriptEngine.createBindings();
            bindings.putAll(variables);
            return (T) groovyScriptEngine.eval(groovyScript, bindings);
        }
    }


}
