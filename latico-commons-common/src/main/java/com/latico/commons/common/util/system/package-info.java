/**
 * <PRE>
 * 操作系统和JVM相关
 *
 JVM中有三个非常重要的编译器：前端编译器、JIT编译器和AOT编译器。

 最常见的前端编译器是我们的Javac编译器，编译器将Java源代码编译成Java字节码文件。JIT即时编译器，最常见的是热点虚拟机中的客户端编译器和服务器编译器，将Java字节码编译成本地机器代码。AOT编译器可以直接将源代码编译为本地机器代码。这三个编译器的编译速度和质量如下：

 编译速度上，解释执行 > AOT 编译器 > JIT 编译器。
 编译质量上，JIT 编译器 > AOT 编译器 > 解释执行。
 在JVM中，通过这些不同方式的协作，可以优化JVM的编译质量和运行速度。

 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-02-06 22:43
 * @Version: 1.0
 */
package com.latico.commons.common.util.system;