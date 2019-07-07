package com.latico.commons.common.util.logging;

/**
 * <PRE>
 * 多功能日志检测实现工厂,
 1、先在配置类进行日志初始化：LogUtils.loadLogBackConfigDefault();
 2、初始化日志对象：private final static Log LOG = LogFactory.getLog(VersionExample.class);
 3、使用日志对象打印日志：LOG.info("123");
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-27 11:48:39
 * @Version: 1.0
 */
public class LoggerFactory extends LogFactory {

}
