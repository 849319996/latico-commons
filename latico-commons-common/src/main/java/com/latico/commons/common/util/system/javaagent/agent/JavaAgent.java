package com.latico.commons.common.util.system.javaagent.agent;

import java.lang.instrument.Instrumentation;

/**
 * JVM代理功能组件.
 * 基于 JVM TI (JVM Tool Interface) 实现的 Java ClassFile 的增强
 *
 *  注意：
 *  需要先把这个类打包成一个jar，例如打包成jvm-agent.jar，
 *  使用方式有两种：
 *  1、然后需要在启动脚本添加参数 -javaagent:./lib/jvm-agent.jar；
 *  2、程序已经启动了，在程序创建一个新进场内部动态调用加载jvm-agent.jar；
 * @Author: LanDingDong
 * @Date: 2018/12/08 14:31:28
 * @Version: 1.0
 */
public class JavaAgent {
    private static Instrumentation instrumentation;

    public JavaAgent() {
    }

    public static Instrumentation getInstn() {
        return instrumentation;
    }

    /**
     *
     * @param agentArgs
     * @param instrumentation
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {

        JavaAgent.instrumentation = instrumentation;
    }



    public static void agentmain(String args, Instrumentation instrumentation) {
        JavaAgent.instrumentation = instrumentation;
    }
}
