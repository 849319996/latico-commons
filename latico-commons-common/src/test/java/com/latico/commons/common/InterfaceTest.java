package com.latico.commons.common;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-03-26 16:49
 * @Version: 1.0
 */
public interface InterfaceTest {

    default String get(String str) {
        return "接口返回:" + str;
    }
}
