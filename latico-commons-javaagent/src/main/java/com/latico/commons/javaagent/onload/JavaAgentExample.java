package com.latico.commons.javaagent.onload;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * JVM代理功能组件.
 * 基于 JVM TI (JVM Tool Interface) 实现的 Java ClassFile 的增强
 *
 *  注意：
 *  需要先把这个类打包成一个jar，例如打包成jvm-agent.jar，请使用IDEA或者eclipse生成jar的方式，
 *  里面必须包含：com.latico.commons.javaagent.JavaAgentExample和META-INF/MANIFEST.MF，也可以使用maven打包jar后，
 *  手工修改jar包内容，替换META-INF/MANIFEST.MF文件。
 *
 *  latico-commons-javaagent/build/assembly/javapath.cfg已经配置好了，使用maven install后，可以利用启动脚本启动
 *
 *  使用方式有两种：
 *  1、然后需要在启动脚本添加参数 -javaagent:./lib/jvm-agent.jar；
 *  2、程序已经启动了，在程序创建一个新进场内部动态调用加载jvm-agent.jar；
 * @Author: LanDingDong
 * @Date: 2018/12/08 14:31:28
 * @Version: 1.0
 */
public class JavaAgentExample {

    /**
     *
     * @param agentArgs
     * @param instrumentation
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("agentArgs : " + agentArgs);
//        类转换器
        ClassFileTransformer classFileTransformer = new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain, byte[] classfileBuffer)
                    throws IllegalClassFormatException {
                System.out.println("可以在此处使用ASM或者javassist修改字节码classfileBuffer:" + className);
                return classfileBuffer;
            }
        };
        instrumentation.addTransformer(classFileTransformer, true);
    }



}
