/**
 * <PRE>
 * 对于NIO的API，如果客户端异常退出，而服务端捕获到异常后，没有关闭跟这个客户端连接的SocketChannel，
 * 那么就会出现死循环，该SelectorKey会一直处于可读状态，Selector会一直监听到这个SelectorKey
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-03-25 9:56
 * @Version: 1.0
 */
package com.latico.commons.net.socket.nio.tcp;