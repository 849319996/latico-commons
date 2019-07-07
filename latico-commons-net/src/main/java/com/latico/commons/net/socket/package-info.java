/**
 * <PRE>
 1、目前的socket接收数据线程是直接入队列，让外界获取，可以使用观察者模式进行优化，收到数据后，推送给观察者。
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-02-06 22:53
 * @Version: 1.0
 */
package com.latico.commons.net.socket;