package com.latico.commons.javaagent.onload;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-07-12 11:37
 * @Version: 1.0
 */
public class OnloadApplication {
    public static void main(String[] args) {
        System.out.println("启动类");
        System.out.println("启动类打印:" + BeanExample.class.getName());
    }
}
