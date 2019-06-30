package com.latico.commons.net.socket.nio.tcp.server.handler.service;

import com.latico.commons.net.socket.nio.tcp.common.SelectionKeyHandler;

/**
 * <PRE>
 * 注意使用是单次任务
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-17 13:54
 * @Version: 1.0
 */
public interface TcpNioServerServiceTask extends Runnable {

    String getTaskId();

    /**
     * @param selectionKeyHandler
     */
    void setSelectionKeyHandler(SelectionKeyHandler selectionKeyHandler);

    void setCharset(String charset);

    /**
     * 接收对方发来的字节数据缓存数据时的队列大小
     * @param receiveQueueSize
     */
    void setReceiveQueueSize(int receiveQueueSize);

    /**
     * 本次执行是否已经完成了的，必须实现
     */
    boolean isFinished();

    /**
     * 关闭跟客户端的连接和释放资源
     * 一般情况，外部不需要主动调用
     * 在NIO的情况下，如果是客户端主动端口了连接，服务器端可以从读取的流返回-1来感知，
     * 然后调用该方法关闭跟此客户端的连接，要不然服务端会一直被Seletor查询到，
     * 如果跟客户端交互过程中出现了异常，最好也调用该方法。
     */
    void close();

}
