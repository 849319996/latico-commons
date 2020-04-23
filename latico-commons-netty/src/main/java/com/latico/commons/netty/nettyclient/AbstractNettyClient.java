package com.latico.commons.netty.nettyclient;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;
import com.latico.commons.netty.NettyTcpUtils;
import com.latico.commons.netty.nettyclient.impl.str.StringClientChannelInboundHandlerImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * <PRE>
 * </PRE>
 *
 * @author: latico
 * @date: 2019-12-05 14:26
 * @version: 1.0
 */
public abstract class AbstractNettyClient<MSG> implements NettyClient<MSG> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractNettyClient.class);
    protected ChannelFuture channelFuture;
    protected Channel channel;
    protected boolean status;

    /**
     * 接受队列
     */
    protected BlockingQueue<MSG> receiveQueue;

    /**
     * 接收队列默认大小
     */
    protected static final int receiveQueueSizeDefault = 10000;

    /**
     * 接收队列大小
     */
    protected int maxReceiveQueueSize;
    /**
     * 最大帧长度，单位：字节数
     */
    protected int maxFrameLength = 8192;

    /**
     * 默认的，接收到最后一个数据后，等待waitUntilNoData毫秒后，还是没有新数据，那么就返回
     */
    protected static final long defaultWaitUntilNoDataTime = 100;
    protected String remoteHost;
    protected int remotePort;

    @Override
    public boolean isStatusValid() {
        return status;
    }

    @Override
    public MSG getOneReceivedData(long timeout) {
        if (!status) {
            return null;
        }
        try {
            MSG data = receiveQueue.poll(timeout, TimeUnit.MILLISECONDS);
            return data;
        } catch (Exception e) {
            LOG.error("", e);
        }
        return null;
    }

    @Override
    public List<MSG> getAllReceivedData(long timeout) {
        return getAllReceivedData(timeout, defaultWaitUntilNoDataTime);
    }

    @Override
    public String getAllReceivedDataToString(long timeout) {
        List<MSG> allReceivedData = getAllReceivedData(timeout);
        return StringUtils.join(allReceivedData);
    }

    @Override
    public String getAllReceivedDataToString(long timeout, long waitUntilNoData) {
        List<MSG> allReceivedData = getAllReceivedData(timeout, waitUntilNoData);
        return StringUtils.join(allReceivedData);
    }

    @Override
    public List<MSG> getAllReceivedData(long timeout, long waitUntilNoData) {
        if (!status) {
            return null;
        }
        List<MSG> datas = new ArrayList<>();
        boolean isFirst = true;
        try {
            while (status) {
                MSG data = null;

                //如果是第一个的时候，需要等待，不是第一个就不需要等待超时
                if (isFirst) {
                    data = receiveQueue.poll(timeout, TimeUnit.MILLISECONDS);
                    isFirst = false;
                }else{
                    data = receiveQueue.poll(waitUntilNoData, TimeUnit.MILLISECONDS);
                }

                if (data == null) {
                    break;
                }

                datas.add(data);
            }

        } catch (Exception e) {
            LOG.error("", e);
        }
        return datas;
    }

    @Override
    public boolean sendMsg(Object msg) {
        if (status) {
            LOG.debug("发送消息:{}", msg);
            channel.writeAndFlush(msg);
            return true;
        }
        return false;
    }

    @Override
    public void close() {
        this.status = false;
        NettyTcpUtils.closeAll(channel);
    }


    /**
     * 初始化接收队列
     * @param receiveQueueSize
     */
    protected void initReceiveQueue(int receiveQueueSize) {
        if (receiveQueueSize <= 0) {
            this.maxReceiveQueueSize = receiveQueueSizeDefault;
        } else {
            this.maxReceiveQueueSize = receiveQueueSize;
        }

        this.receiveQueue = new ArrayBlockingQueue(receiveQueueSize);

    }

    @Override
    public int getMaxReceiveQueueSize() {
        return this.maxReceiveQueueSize;
    }

    /**
     * 添加接收数据尽量，调用者不要使用此方法
     * @param data
     */
    @Override
    public void addReceivedData(MSG data){
        receiveQueue.add(data);
    }

    @Override
    public boolean init(String remoteHost, int remotePort, int maxReceiveQueueSize) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
        this.maxReceiveQueueSize = maxReceiveQueueSize;

        try {
            initReceiveQueue(maxReceiveQueueSize);
            this.channelFuture = initNettyClient();
            this.channel = channelFuture.channel();
            this.status = channel.isActive();
        } catch (Exception e) {
            this.status = false;
            LOG.error("", e);
        }
        return this.status;
    }

    /**
     * 子类实现初始化netty客户端
     * @return
     */
    protected abstract ChannelFuture initNettyClient();

    /**
     * 因为有分隔符，必定是字符串了，所以直接在这里完成所有操作
     * @param remoteHost          远程主机
     * @param remotePort          远程端口
     * @param maxReceiveQueueSize 接收数据的队列大小，如果是0或者负数，就会使用默认大小10000
     * @param maxFrameLength      最大的帧长度，单位：字节数，如果是输入0或者-1，那么默认使用8K的字节数
     * @param delimiters          分隔符，可以多个
     * @return
     */
    @Override
    public boolean init(String remoteHost, int remotePort, int maxReceiveQueueSize, int maxFrameLength, List<String> delimiters) {
        this.maxReceiveQueueSize = maxReceiveQueueSize;

        if (maxFrameLength >= 1) {
            this.maxFrameLength = maxFrameLength;
        } else {
            maxFrameLength = this.maxFrameLength;
        }
        try {
            initReceiveQueue(maxReceiveQueueSize);
            SimpleChannelInboundHandler simpleChannelInboundHandler = new StringClientChannelInboundHandlerImpl(this);
            DelimiterBasedFrameDecoder delimiterBasedFrameDecoder = getDelimiterBasedFrameDecoder(maxFrameLength, delimiters);
            //使用分隔符方式
            this.channelFuture = NettyTcpUtils.createClientByServiceHandlerByStringWithFrameDecoder(remoteHost,  remotePort, delimiterBasedFrameDecoder, simpleChannelInboundHandler);

            this.channel = channelFuture.channel();
            this.status = channel.isActive();

        } catch (Exception e) {
            this.status = false;
            LOG.error("", e);
        }
        LOG.info("NettyClient初始化完成，状态:{}", status);
        return this.status;
    }

    /**
     * 配置分隔符
     * @return
     * @param maxFrameLength
     * @param delimiters
     */
    private DelimiterBasedFrameDecoder getDelimiterBasedFrameDecoder(int maxFrameLength, List<String> delimiters) {
        ByteBuf[] bufs = new ByteBuf[delimiters.size()];
        for (int i = 0; i < delimiters.size(); i++) {
            bufs[i] = Unpooled.copiedBuffer(delimiters.get(i).getBytes());
        }

        return new DelimiterBasedFrameDecoder(
                maxFrameLength, bufs);
    }
}
