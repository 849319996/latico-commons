package com.latico.commons.common.util.proxy.jdk.expample;

/**
 * <PRE>
 * 接口实现类
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2018-12-31 19:53
 * @Version: 1.0
 */
public class InterfaceExampleImpl implements InterfaceExample {
    @Override
    public String get(String param) {
        return "示例get处理完成:" + param;
    }
}
