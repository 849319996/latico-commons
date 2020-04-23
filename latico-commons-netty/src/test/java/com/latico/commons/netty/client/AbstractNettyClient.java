package com.latico.commons.netty.client;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.netty.NettyTcpUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-12-05 14:26
 * @version: 1.0
 */
public abstract class AbstractNettyClient<T> implements NettyClient<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractNettyClient.class);
    protected ChannelFuture channelFuture;
    protected Channel channel;
    protected boolean status;

    /**
     * 接受队列
     */
    protected BlockingQueue<T> receiveQueue;

    /**
     * 接收队列默认大小
     */
    protected static final int receiveQueueSizeDefault = 10000;

    /**
     * 接收队列大小
     */
    protected int receiveQueueSize;

    @Override
    public boolean isStatusValid() {
        return status;
    }

    @Override
    public T getOneReceivedData(long timeout) {
        if (!status) {
            return null;
        }
        try {
            T data = receiveQueue.poll(timeout, TimeUnit.MILLISECONDS);
            return data;
        } catch (Exception e) {
            LOG.error("", e);
        }
        return null;
    }

    @Override
    public List<T> getAllReceivedData(long timeout) {
        if (!status) {
            return null;
        }
        List<T> datas = new ArrayList<>();
        boolean isFirst = true;
        try {
            while (status) {
                T data = null;

                //如果是第一个的时候，需要等待，不是第一个就不需要等待超时
                if (isFirst) {
                    data = receiveQueue.poll(timeout, TimeUnit.MILLISECONDS);
                    isFirst = false;
                }else{
                    data = receiveQueue.poll();
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
    public boolean sendData(T data) {
        if (status) {
            channel.writeAndFlush(data);
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
            this.receiveQueueSize = receiveQueueSizeDefault;
        } else {
            this.receiveQueueSize = receiveQueueSize;
        }

        this.receiveQueue = new ArrayBlockingQueue(receiveQueueSize);

    }

    @Override
    public int getReceiveQueueSize() {
        return this.receiveQueueSize;
    }
}
