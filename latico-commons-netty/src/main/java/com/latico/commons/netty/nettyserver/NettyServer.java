package com.latico.commons.netty.nettyserver;

import com.latico.commons.netty.nettyserver.bean.ReceiveMsg;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.List;

/**
 * <PRE>
 *
 * </PRE>
 * @author: latico
 * @date: 2019-12-13 16:52:22
 * @version: 1.0
 */
public interface NettyServer<MSG> {

    /**
     * 初始化
     * @param localPort 本端端口
     * @param maxReceiveQueueSize 接收数据时的队列最大
     * @return
     */
    boolean init(int localPort, int maxReceiveQueueSize);

    /**
     * 启动netty服务器
     * @return
     */
    boolean startNettyServer();

    /**
     * 获取跟客户端通信的通道，用于发送消息
     * @return 跟客户端连接的所有
     */
    ChannelGroup getClientChannelGroup();

    /**
     * 关闭netty服务器
     * @return
     */
    void closeNettyServer();

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
    ReceiveMsg<MSG> getOneReceivedData(long timeout);

    /**
     * 获取并移除当前已经接受的所有数据
     * @param timeout 最长等待超时时间，单位毫秒
     * @return
     */
    List<ReceiveMsg<MSG>> getAllReceivedData(long timeout);

    /**
     * 拿所有数据，同时在最后等待还有没有数据传输进来
     * @param timeout
     * @param waitUntilNoData 接收到最后一个数据后，等待waitUntilNoData毫秒后，还是没有新数据，那么就返回
     * @return
     */
    List<ReceiveMsg<MSG>> getAllReceivedData(long timeout, long waitUntilNoData);

    /**
     * 添加接收数据尽量，调用者不要使用此方法
     * @param data
     */
    public void addReceivedData(ReceiveMsg<MSG> data);

    /**
     * 添加接收数据尽量，调用者不要使用此方法
     * @param data
     */
    public void addReceivedData(MSG data, Channel remoteChannel);


    /**
     * @param remoteChannel
     * @param msg
     * @return
     */
    boolean sendMsg(Channel remoteChannel, MSG msg);

    /**
     * 发送消息给所有客户端
     * @param msg
     * @return
     */
    boolean sendMsgToAllClient(MSG msg);

}
