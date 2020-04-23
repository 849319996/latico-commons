package com.latico.commons.netty.nettyserver;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.math.NumberUtils;
import com.latico.commons.common.util.thread.thread.AbstractThread;
import com.latico.commons.netty.NettyTcpUtils;
import com.latico.commons.netty.nettyserver.bean.ReceiveMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-12-13 16:49
 * @version: 1.0
 */
public abstract class AbstractNettyServer<MSG> extends AbstractThread implements NettyServer<MSG> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractNettyServer.class);
    private static final Pattern socketAddressPattern = Pattern.compile("/(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)");
    protected ChannelFuture channelFuture;
    protected ChannelGroup channelGroup;
    protected boolean status;
    /**
     * 接受队列
     */
    protected BlockingQueue<ReceiveMsg<MSG>> receiveQueue;

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
    private static final long defaultWaitUntilNoDataTime = 100;
    protected int localPort;

    public AbstractNettyServer() {
        super("NettyServer", new AtomicBoolean(true));
    }

    @Override
    public boolean startNettyServer() {
        return this.startThread();
    }

    @Override
    public void closeNettyServer() {
        this.status = false;
        this.stopThread();
    }

    @Override
    public boolean isStatusValid() {
        return status && this.isRunning();
    }

    @Override
    protected void dealProcess() throws Exception {
        LOG.info("NettyServer开始监听，直到被关闭");
        NettyTcpUtils.waitToShutdownGracefully(channelFuture);
        LOG.info("NettyServer关闭完成");
    }

    @Override
    public ChannelGroup getClientChannelGroup() {
        return channelGroup;
    }

    @Override
    public boolean init(int localPort, int maxReceiveQueueSize) {
        this.localPort = localPort;

        LOG.info("NettyServer开始初始化,本地端口:{}", localPort);
        try {
            initReceiveQueue(maxReceiveQueueSize);
            //初始化的通道组，用于装客户端通道
            this.channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
            this.channelFuture = initNettyServer();
            this.status = channelFuture.isSuccess();

        } catch (Exception e) {
            this.status = false;
            LOG.error("", e);
        }
        LOG.info("NettyServer启动完成,状态:{}", status);
        return this.status;
    }

    /**
     * 子类初始化Netty服务器
     * @return
     */
    protected abstract ChannelFuture initNettyServer();

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
    public ReceiveMsg<MSG> getOneReceivedData(long timeout) {
        if (!status) {
            return null;
        }
        try {
            ReceiveMsg<MSG> data = receiveQueue.poll(timeout, TimeUnit.MILLISECONDS);
            return data;
        } catch (Exception e) {
            LOG.error("", e);
        }
        return null;
    }

    @Override
    public List<ReceiveMsg<MSG>> getAllReceivedData(long timeout) {
        return getAllReceivedData(timeout, defaultWaitUntilNoDataTime);
    }

    @Override
    public List<ReceiveMsg<MSG>> getAllReceivedData(long timeout, long waitUntilNoData) {
        if (!status) {
            return null;
        }
        List<ReceiveMsg<MSG>> datas = new ArrayList<>();
        boolean isFirst = true;
        try {
            while (status) {
                ReceiveMsg<MSG> data = null;

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

    /**
     * 添加接收数据尽量，调用者不要使用此方法
     * @param data
     */
    @Override
    public void addReceivedData(ReceiveMsg<MSG> data){
        receiveQueue.add(data);
    }

    /**
     * 添加接收数据尽量，调用者不要使用此方法
     * @param data
     */
    @Override
    public void addReceivedData(MSG data, Channel remoteChannel){
        ReceiveMsg<MSG> receiveMsg = new ReceiveMsg<>();
        receiveMsg.setMsg(data);
        receiveMsg.setRemoteChannel(remoteChannel);
        SocketAddress remoteAddress = remoteChannel.remoteAddress();
        if (remoteAddress != null) {
            String socketStr = remoteAddress.toString();
            Matcher matcher = socketAddressPattern.matcher(socketStr);
            if (matcher.find()) {
                receiveMsg.setRemoteIp(matcher.group(1));
                receiveMsg.setRemotePort(NumberUtils.toInt(matcher.group(2)));
            }
        }

        receiveQueue.add(receiveMsg);
    }

    @Override
    public boolean sendMsg(Channel remoteChannel, MSG msg) {
        if (isStatusValid()) {
            if (remoteChannel != null && remoteChannel.isActive()) {
                LOG.debug("发送消息给:{},内容:{}", remoteChannel.remoteAddress()
                        , msg);
                remoteChannel.writeAndFlush(msg);
                return true;
            } else {
                LOG.warn("客户端{}状态异常", remoteChannel.remoteAddress());
            }
        } else {
            LOG.warn("无效的状态");
        }
        return false;
    }

    @Override
    public boolean sendMsgToAllClient(MSG msg) {
        if (isStatusValid()) {
            if (channelGroup != null && channelGroup.size() >= 1) {
                LOG.debug("发送消息给所有客户端,内容:{}", msg);
                channelGroup.writeAndFlush(msg);
                return true;
            } else {
                LOG.warn("没有已连接的客户端");
            }
        } else {
            LOG.warn("无效的状态");
        }
        return false;
    }
}
