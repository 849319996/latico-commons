package com.latico.commons.javaagent.runtime;

import com.latico.commons.javaagent.onload.BeanExample;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-07-12 11:37
 * @version: 1.0
 */
public class RuntimeApplication {
    public static void main(String[] args) {
        System.out.println("启动类");
        System.out.println("启动类打印:" + BeanExample.class.getName());
    }
}
