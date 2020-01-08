package com.latico.commons.common.util.net;

import sun.net.util.IPAddressUtil;

/**
 * <PRE>
    IPV6工具类

 IPV6格式如下：
 IPv6的地址长度是128位（bit）。

 将这128位的地址按每16位划分为一个段，将每个段转换成十六进制数字，并用冒号隔开。

 例如：2000:0000:0000:0000:0001:2345:6789:abcd

 这个地址很长，可以用两种方法对这个地址进行压缩，

 前导零压缩法：

 将每一段的前导零省略，但是每一段都至少应该有一个数字

 例如：2000:0:0:0:1:2345:6789:abcd

 双冒号法：

 如果一个以冒号十六进制数表示法表示的IPv6地址中，如果几个连续的段值都是0，那么这些0可以简记为::。每个地址中只能有一个::。

 例如：2000::1:2345:6789:abcd

 * </PRE>
 *
 * @Author: latico
 * @Date: 2020-01-07 16:41
 * @Version: 1.0
 */
public class Ipv6Utils {

    /**
     * 是不是有效的IPV6地址
     * String ipv6 = "ABCD:EF01:2345:6789:ABCD:EF01:2345:6789";
     */
    public static boolean isIPv6LiteralAddress(String ipStr){
        return IPAddressUtil.isIPv6LiteralAddress(ipStr);
    }
}
