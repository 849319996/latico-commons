package com.latico.commons.netty.client;

import java.util.List;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-12-05 14:25
 * @version: 1.0
 */
public interface NettyClient<T> {

    /**
     * 初始化
     * @param remoteHost 远程主机
     * @param remotePort 远程端口
     * @param receiveQueueSize 接收数据的队列大小，如果是0或者负数，就会使用默认大小10000
     * @return 是否成功
     */
    boolean init(String remoteHost, int remotePort, int receiveQueueSize);

    /**
     * 状态是否有效
     * @return true:有效，false:无效
     */
    boolean isStatusValid();

    /**
     * 获取并移除当前已经接受的数据
     * @param timeout 最长等待超时时间，单位毫秒
     * @return
     */
    T getOneReceivedData(long timeout);

    /**
     * 获取并移除当前已经接受的所有数据
     * @param timeout 最长等待超时时间，单位毫秒
     * @return
     */
    List<T> getAllReceivedData(long timeout);

    /**
     * 发送数据
     * @param data
     * @return
     */
    boolean sendData(T data);

    /**
     * 关闭
     */
    void close();

    int getReceiveQueueSize();
}
