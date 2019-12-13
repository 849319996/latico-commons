package com.latico.commons.netty.nettyclient;

import java.util.List;

/**
 * <PRE>
 *  netty客户端接口
 * </PRE>
 * @param <MSG> 接收的数据格式
 * @Author: latico
 * @Date: 2019-12-05 14:25
 * @Version: 1.0
 */
public interface NettyClient<MSG> {

    /**
     * 初始化方法，建议在对象传输的时候使用，而对于字符串解析，建议使用{@link NettyClient#init(String, int, int, int, List)}
     * @param remoteHost
     * @param remotePort
     * @param maxReceiveQueueSize 接收数据时的队列最大
     * @return
     */
    boolean init(String remoteHost, int remotePort, int maxReceiveQueueSize);

    /**
     * 初始化
     * @param remoteHost 远程主机
     * @param remotePort 远程端口
     * @param maxReceiveQueueSize 接收数据的队列大小，如果是0或者负数，就会使用默认大小10000
     * @param maxFrameLength   最大的帧长度，单位：字节数，如果是输入0或者-1，那么默认使用8K的字节数
     * @param delimiters 分隔符，可以多个
     * @return 是否成功
     */
    boolean init(String remoteHost, int remotePort, int maxReceiveQueueSize, int maxFrameLength, List<String> delimiters);


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
    MSG getOneReceivedData(long timeout);

    /**
     * 获取并移除当前已经接受的所有数据
     * @param timeout 最长等待超时时间，单位毫秒
     * @return
     */
    List<MSG> getAllReceivedData(long timeout);

    /**
     * 拿到所有数据后，合并字符串
     * @param timeout
     * @return
     */
    String getAllReceivedDataToString(long timeout);

    /**
     * 拿所有数据，同时在最后等待还有没有数据传输进来
     * @param timeout
     * @param waitUntilNoData 接收到最后一个数据后，等待waitUntilNoData毫秒后，还是没有新数据，那么就返回
     * @return
     */
    List<MSG> getAllReceivedData(long timeout, long waitUntilNoData);

    /**
     * 拿到所有数据后，合并字符串
     * @param timeout
     * @param waitUntilNoData
     * @return
     */
    String getAllReceivedDataToString(long timeout, long waitUntilNoData);

    /**
     * 发送数据
     * @param msg
     * @return
     */
    boolean sendMsg(Object msg);

    /**
     * 关闭
     */
    void close();

    /**
     * @return 接收数据的队列大小
     */
    int getMaxReceiveQueueSize();

    /**
     * 添加接收数据尽量，调用者不要使用此方法
     * @param data
     */
    public void addReceivedData(MSG data);
}
