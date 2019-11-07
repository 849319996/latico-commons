package com.latico.commons.net.syslog;

import org.graylog2.syslog4j.server.SyslogServerEventIF;
import org.graylog2.syslog4j.util.SyslogUtility;

import java.sql.Timestamp;
import java.util.Date;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-11-07 10:20
 * @Version: 1.0
 */
public class SyslogUtils {

    /**
     * 等级的字符串方式描述
     * @param event
     * @return
     */
    public static String getLevelString(SyslogServerEventIF event) {
        return SyslogUtility.getLevelString(event.getLevel());
    }
    public static String getFacilityString(SyslogServerEventIF event) {
        return SyslogUtility.getFacilityString(event.getFacility());
    }

    public static Date getDate(SyslogServerEventIF event) {
        return (event.getDate() == null ? new Date(System.currentTimeMillis()) : event.getDate());
    }
}
