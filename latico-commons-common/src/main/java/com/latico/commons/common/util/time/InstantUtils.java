package com.latico.commons.common.util.time;

import java.time.Instant;

/**
 * <PRE>
 * 秒操作工具
 * </PRE>
 *
 * @author: latico
 * @date: 2020-04-10 18:13
 * @version: 1.0
 */
public class InstantUtils {
    public static Instant getCurrentInstant() {
        return Instant.now();
    }

    public static long getSecond(Instant instant) {
        return instant.getEpochSecond();
    }

    public static long getMilliSecond(Instant instant) {
        return instant.toEpochMilli();
    }

}
