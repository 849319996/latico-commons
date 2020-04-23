package com.catt.commons.groovy;

import com.latico.commons.net.cmdclient.CmdClient;
import com.latico.commons.net.cmdclient.CmdClientFactory;
import com.latico.commons.net.cmdclient.enums.CmdClientTypeEnum;
import org.junit.Test;

import javax.script.ScriptException;

import java.util.HashMap;
import java.util.Map;

public class GroovyScriptUtilsTest {

    @Test
    public void getGroovyScriptEngine() {
    }

    /**
     * 
     */
    @Test
    public void test(){
        CmdClient cmdClient = CmdClientFactory.getCmdClient(CmdClientTypeEnum.SSH);
        try {
            boolean isLoginSucc = cmdClient.login("172.168.10.7", 22, "cattsoft", "cattsoft");
            if (isLoginSucc) {
                String groovyScript = "return $cmdClient.execCmdAndReceiveData('ping www.baidu.com -c 5');";
                Map<String, Object> variables = new HashMap<>();
                variables.put("$cmdClient", cmdClient);

                Object result = GroovyScriptEngineManagerUtils.evaluate(groovyScript, variables);
                System.out.println("执行命令的结果:\r\n" + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cmdClient.close();
        }
    }

    @Test
    public void test2() {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        execute(Thread.currentThread().getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "T"+i).start();
        }
    }


    public static void execute(String tname) throws ScriptException {
        Map<String, Object> map = new HashMap<>();
        map.put("name",tname);
        String script = " map.remove('a');  map['name2']=$abc; return map;";
        Map<String, Object> variables = new HashMap<>();
        variables.put("$abc", "123");
        variables.put("map", map);

        Object result = GroovyScriptEngineManagerUtils.evaluate(script, variables);
        System.out.println(result);
    }


}