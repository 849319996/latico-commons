package com.latico.commons.mq.rocketmq;

/**
 * <PRE>
 * 阿里巴巴贡献给apache的消息队列
 *
 一、windows方式启动

 1、 启动NAMESERVER
 Cmd命令框执行进入至‘MQ文件夹\bin’下，然后执行‘start mqnamesrv.cmd’，启动NAMESERVER。成功后会弹出提示框，此框勿关闭。

 2、 启动BROKER
 Cmd命令框执行进入至‘MQ文件夹\bin’下，然后执行‘start mqbroker.cmd -n 127.0.0.1:9876 autoCreateTopicEnable=true’，启动BROKER。成功后会弹出提示框，此框勿关闭。
 假如弹出提示框提示‘错误: 找不到或无法加载主类 xxxxxx’。打开runbroker.cmd，然后将‘%CLASSPATH%’加上英文双引号。保存并重新执行start语句。
 二、linux方式启动
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-26 22:05
 * @version: 1.0
 */
public class RocketMQUtils {
}
