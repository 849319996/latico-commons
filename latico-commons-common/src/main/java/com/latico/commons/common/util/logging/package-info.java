/**
 * <PRE>
 * 通用日志门面
 *
 * 多功能日志检测实现工厂,
 *  1、先在配置类进行日志初始化：LogUtils.loadLogBackConfigDefault();
 *  2、初始化日志对象：private final static Log LOG = LogFactory.getLog(VersionExample.class);
 *  3、使用日志对象打印日志：LOG.info("123");
 *
 *  //用法1
 *  Log LOG1 = LoggerFactory.getLog(Version.class);
 *  Logger LOG2 = LoggerFactory.getLogger(Version.class);
 *
 *  //用法2
 *  Log LOG3 = LogUtils.getLog(Version.class);
 *  Logger LOG4 = LogUtils.getLogger(Version.class);
 *
 *  //用法3
 *  Log LOG5 = LogFactory.getLog(Version.class);
 *  Logger LOG6 = LogFactory.getLogger(Version.class);
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-02-06 22:47
 * @version: 1.0
 */
package com.latico.commons.common.util.logging;