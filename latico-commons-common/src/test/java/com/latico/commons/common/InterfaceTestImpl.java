package com.latico.commons.common;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-03-26 16:49
 * @Version: 1.0
 */
public class InterfaceTestImpl implements InterfaceTest {

    public static void main(String[] args) {
        InterfaceTestImpl impl = new InterfaceTestImpl();
        System.out.println(impl.get("jajg"));
    }
}
