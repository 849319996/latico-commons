package com.latico.commons.common.util.system.classloader;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-07-03 14:35
 * @version: 1.0
 */
public class TestClassLoaderBean {
    public static final int finalIntData = 10;
    public static final String finalStrData = "字符串数据";
    public static int intData = 100;
    public static String strData = "字符串数据";

    static {
        intData = 1000;
        strData = "abcde";
        System.out.println("执行了静态代码块");
    }

}
