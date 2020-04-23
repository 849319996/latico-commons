/**
 * <PRE>
 * 服务端
 * 可以直接把zookeeper内嵌进程序里面启动
 *
 * 如果是集群模式，需要修改的地方有：config/zoo.cfg文件配置集群节点，
 * 同时配置tmp/zookeeper/myid文件中配置本次启动所使用的节点序号
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-02-12 17:47
 * @version: 1.0
 */
package com.latico.commons.zookeeper.server;