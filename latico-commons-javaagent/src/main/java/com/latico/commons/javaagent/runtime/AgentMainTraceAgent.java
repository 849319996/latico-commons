package com.latico.commons.javaagent.runtime;

import com.latico.commons.javaagent.onload.BeanExample;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

/**
 定义一个MANIFEST.MF 文件，文件中必须包含 Agent-Class;

 Manifest-Version: 1.0
 Can-Redefine-Classes: true
 Can-Retransform-Classes: true
 Agent-Class: com.latico.commons.javaagent.runtime.AgentMainTraceAgent

 创建一个 Agent-Class 指定的类，该类必须包含 agentmain 方法（参数和 premian 相同）。

 */
public class AgentMainTraceAgent {

    public static void agentmain(String agentArgs, Instrumentation inst)
            throws UnmodifiableClassException {

        System.out.println("Agent Main called");
        System.out.println("agentArgs : " + agentArgs);
//        类转换器
        ClassFileTransformer classFileTransformer = new ClassFileTransformer() {

            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain, byte[] classfileBuffer)
                    throws IllegalClassFormatException {
                System.out.println("可以在这里修改字节码，agentmain load Class  :" + className);
                return classfileBuffer;
            }
        };

        inst.addTransformer(classFileTransformer, true);

/*        执行了 inst.retransformClasses(Account.class); 这段代码的意思是，重新转换目标类，
也就是 Account 类。也就是说，你需要重新定义哪个类，需要指定，否则 JVM 不可能知道。
还有一个类似的方法 redefineClasses ，注意，这个方法是在类加载前使用的。
类加载后需要使用 retransformClasses 方法 */
        inst.retransformClasses(Account.class);
    }
}