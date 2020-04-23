/**
 * <PRE>
 数据源，也就是一般所说的数据库连接池

 数据源的集中比较。

 目前常用的数据源主要有c3p0、dbcp、proxool、druid，先来说说他们
 Spring 推荐使用dbcp；
 Hibernate 推荐使用c3p0和proxool
 1、 DBCP：apache
 DBCP(DataBase connection pool)数据库连接池。是apache上的一个 java连接池项目，也是 tomcat使用的连接池组件。单独使用dbcp需要3个包：common-dbcp.jar,common-pool.jar,common-collections.jar由于建立数据库连接是一个非常耗时耗资源的行为，所以通过连接池预先同数据库建立一些连接，放在内存中，应用程序需要建立数据库连接时直接到连接池中申请一个就行，用完后再放回去。dbcp没有自动的去回收空闲连接的功能。

 2、 C3P0：
 C3P0是一个开源的jdbc连接池，它实现了数据源和jndi绑定，支持jdbc3规范和jdbc2的标准扩展。c3p0是异步操作的，缓慢的jdbc操作通过帮助进程完成。扩展这些操作可以有效的提升性能。目前使用它的开源项目有Hibernate，Spring等。c3p0有自动回收空闲连接功能。

 3、 Proxool：Sourceforge
 Proxool是一种Java数据库连接池技术。是sourceforge下的一个开源项目,这个项目提供一个健壮、易用的连接池，最为关键的是这个连接池提供监控的功能，方便易用，便于发现连接泄漏的情况。
 综合来说，稳定性是dbcp>=c3p0>proxool

 后来阿里巴巴的druid开源了，可以是前无古人后无来者，最强没有之一，是否的稳定，在大并发中表现十分好
 * </PRE>
 *
 * @author: latico
 * @date: 2019-03-19 10:13
 * @version: 1.0
 */
package com.latico.commons.db.datasource;