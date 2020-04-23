package com.latico.commons.common;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-03-26 16:49
 * @version: 1.0
 */
public interface InterfaceTest {

    default String get(String str) {
        return "接口返回:" + str;
    }
}
