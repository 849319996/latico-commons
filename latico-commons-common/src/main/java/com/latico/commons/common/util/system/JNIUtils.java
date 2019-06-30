package com.latico.commons.common.util.system;

import java.io.File;

/**
 * <PRE>
 JNI（Java Native Interface），Java本地接口，是为方便java调用C或者C++等本地代码所封装的一层接口。由于Java的跨平台性导致本地交互能力不好，一些和操作系统相关的特性Java无法完成，于是Java提供了JNI专门用于和本地代码交互。
 无论是 Linux，Windows 还是 Mac OS，或者一些汇编语言写的底层硬件驱动都是 C/C++ 写的。Java和C/C++不同 ，它不会直接编译成平台机器码，而是编译成虚拟机可以运行的Java字节码的.class文件，通过JIT（Just-In-Time）技术即时编译成本地机器码，所以有效率就比不上C/C++代码，JNI技术就解决了这一痛点，JNI 可以说是 C 语言和 Java 语言交流的适配器、中间件。其实主要是定义了一些JNI函数，让开发者可以通过调用这些函数实现Java代码调用C/C++的代码，C/C++的代码也可以调用Java的代码，这样就可以发挥各个语言的特点了。
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-02-05 22:39
 * @Version: 1.0
 */
public class JNIUtils {

    protected JNIUtils() { }

    /**
     * 库文件可以在任意位置
     * @param libFile 库文件路径
     */
    public static void loadByJniLibFile(String libFile) {
        File file = new File(libFile);
        //使用绝对路径
        String absolutePath = file.getAbsolutePath();
        System.load(absolutePath);
    }

    /**
     * 库文件的位置必须放在 System.getProperty("java.library.path") 目录下
     * @param libName 库文件名称，不带后缀
     */
    public static void loadByJniLibName(String libName) {
        System.loadLibrary(libName);
    }
}
