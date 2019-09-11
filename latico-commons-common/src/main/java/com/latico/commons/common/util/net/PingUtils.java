package com.latico.commons.common.util.net;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-08-14 11:48
 * @Version: 1.0
 */
public class PingUtils {
    private static final Logger LOG = LoggerFactory.getLogger(PingUtils.class);

    /**
     * 通过原生JDK的方式检测，效率高，但是对于windows XP底层存在很大bug，而且好像不支持多线程，具体自己了解，好像不建议使用
     * 存在问题：必须在同一个局域网下的IP才能使用，局限性比较大
     * @param host
     * @param timeout 单位毫秒
     * @return
     */
    public static boolean isReachableByInetAddress(String host, int timeout) {
        try {
            InetAddress inet = InetAddress.getByName(host);
            return inet.isReachable(timeout);
        } catch (Exception e) {
            LOG.error("Host is NOT reachable:{} timeout:{}", e, host, timeout);
        }
        return false;
    }

}
