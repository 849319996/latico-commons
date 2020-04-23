package com.catt.commons.groovy;

import com.latico.commons.net.cmdclient.CmdClient;
import com.latico.commons.net.cmdclient.CmdClientFactory;
import com.latico.commons.net.cmdclient.enums.CmdClientTypeEnum;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GroovyShellUtilsTest {
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

                Object result = GroovyShellUtils.evaluate(groovyScript, variables);
                System.out.println("执行命令的结果:\r\n" + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cmdClient.close();
        }
    }
}