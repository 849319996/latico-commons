/**
 * <PRE>
 * ThreadLocal相关封装
 * {@link java.lang.ThreadLocal}、{@link java.lang.InheritableThreadLocal}
 * ThreadLocal还有一个派生的子类：InheritableThreadLocal ，可以允许线程及该线程创建的子线程均可以访问同一个变量(有些OOP中的proteced的意味)
 *
 spring的事务管理中TransactionSynchronizationManager使用了ThreadLocal进行管理

 因为线程的执行是有顺序的，所以通过ThreadLocal拿到的数据肯定是线程内部唯一的副本，ThreadLocal只是用来装了各个线程对
 数据的副本存储
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-02-06 23:36
 * @version: 1.0
 */
package com.latico.commons.common.util.thread.threadlocal;