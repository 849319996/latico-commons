package com.latico.commons.net.syslog.client;/**
 * @Author: LanDingDong
 * @Date: 2018/12/06 11:09
 * @Version: 1.0
 */

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import org.graylog2.syslog4j.Syslog;
import org.graylog2.syslog4j.SyslogIF;

import java.net.URLDecoder;

/**
 * 发送信息到服务器，2表示日志级别 范围为0~7的数字编码，表示了事件的严重程度。0最高，7最低
 * *  syslog为每个事件赋予几个不同的优先级：
 * LOG_EMERG：紧急情况，需要立即通知技术人员。
 * LOG_ALERT：应该被立即改正的问题，如系统数据库被破坏，ISP连接丢失。
 * LOG_CRIT：重要情况，如硬盘错误，备用连接丢失。
 * LOG_ERR：错误，不是非常紧急，在一定时间内修复即可。
 * LOG_WARNING：警告信息，不是错误，比如系统磁盘使用了85%等。
 * LOG_NOTICE：不是错误情况，也不需要立即处理。
 * LOG_INFO：情报信息，正常的系统消息，比如骚扰报告，带宽数据等，不需要处理。
 * LOG_DEBUG：包含详细的开发情报的信息，通常只在调试一个程序时使用。
 *
 * @Author: LanDingDong
 * @Date: 2018/12/06 11:09
 * @Version: 1.0
 */
public class SyslogClient {
    private static final Logger LOG = LoggerFactory.getLogger(SyslogClient.class);
    private String host = "127.0.0.1";
    private int port = 514;
    private String charset = "UTF-8";

    /**
     * syslog 日志对象
     */
    private SyslogIF syslog;

    /**
     * 构造方法
     *
     * @param host    IP
     * @param port    默认514
     * @param charset 可以为空
     */
    public SyslogClient(String host, int port, String charset) {
        this.host = host;
        if (port >= 1) {
            this.port = port;
        }
        if (charset != null) {
            this.charset = charset;
        }
        //获取syslog的操作类，使用udp协议。syslog支持"udp", "tcp", "unix_syslog", "unix_socket"协议
        this.syslog = Syslog.getInstance("udp");
        //设置syslog服务器端地址
        syslog.getConfig().setHost(this.host);
        //设置syslog接收端口，默认514
        syslog.getConfig().setPort(this.port);
    }

    /**
     * 发送内容
     *
     * @param msg   内容
     * @param level 级别
     */
    private void sendMsg(String msg, int level) {
        if (msg == null) {
            return;
        }
        try {
            syslog.log(level, URLDecoder.decode(msg, charset));
        } catch (Exception e) {
            LOG.error("发送Syslog信息失败", e);
        }
    }

    /**
     * LOG_DEBUG：包含详细的开发情报的信息，通常只在调试一个程序时使用。
     *
     * @param msg 消息内容
     */
    public void debug(String msg) {
        sendMsg(msg, 7);
    }

    /**
     * LOG_INFO：情报信息，正常的系统消息，比如骚扰报告，带宽数据等，不需要处理。
     *
     * @param msg 消息内容
     */
    public void info(String msg) {
        sendMsg(msg, 6);
    }

    /**
     * LOG_WARNING：警告信息，不是错误，比如系统磁盘使用了85%等。
     *
     * @param msg 消息内容
     */
    public void warn(String msg) {
        sendMsg(msg, 4);
    }

    /**
     * LOG_ERR：错误，不是非常紧急，在一定时间内修复即可。
     *
     * @param msg 消息内容
     */
    public void error(String msg) {
        sendMsg(msg, 3);
    }

    /**
     * LOG_NOTICE：不是错误情况，也不需要立即处理。
     *
     * @param msg 消息内容
     */
    public void notice(String msg) {
        sendMsg(msg, 5);
    }

    /**
     * LOG_CRIT：重要情况，如硬盘错误，备用连接丢失。
     *
     * @param msg 消息内容
     */
    public void crit(String msg) {
        sendMsg(msg, 2);
    }

    /**
     * LOG_ALERT：应该被立即改正的问题，如系统数据库被破坏，ISP连接丢失。
     *
     * @param msg 消息内容
     */
    public void alert(String msg) {
        sendMsg(msg, 1);
    }

    /**
     * LOG_EMERG：紧急情况，需要立即通知技术人员。
     *
     * @param msg 消息内容
     */
    public void emerg(String msg) {
        sendMsg(msg, 0);
    }


}
