package com.latico.commons.net.socket.io.udp;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.net.UdpSocketUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * <PRE>
 * UDP客户端
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-15 15:07
 * @Version: 1.0
 */
public class UdpSocketClient implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(UdpSocketClient.class);

    /**
     * 状态，是否关闭，true:启动，false:已关闭
     */
    private boolean status = false;

    /**
     * 数据字符集
     */
    private String charset = "UTF-8";
    /**
     * 默认字符集
     */
    private static final String charsetDefault = "UTF-8";

    /**
     * 目标主机
     */
    private String targetHost = "127.0.0.1";
    /**
     * 目标端口
     */
    private int targetPort = 7878;
    /**
     * 本端发送包的时候的端口，可选，如果小于等于0，那就是使用随机端口，默认使用随机端口
     */
    private int localSendPort = -1;

    /**
     * 本地接收消息的端口
     */
    private int localReceivePort = -1;
    /**
     * 发送用的socket
     */
    private DatagramSocket sendSocket;
    /**
     * 接收消息的socket，一般情况跟发送socket一样，当服务器端收到消息后可以提取出客户端发过来的端口，然后返回去
     */
    private DatagramSocket receiveSocket;

    /**
     * 接收数据队列
     */
    private BlockingQueue<DatagramPacket> receiveQueue;

    /**
     * 创建socket的时候，绑定端口重试次数
     */
    private int bandPortRetries = 3;

    private int socketTimeout = 300000;

    /**
     * @param targetHost 目标主机，必须
     * @param targetPort 目标端口，必须
     * @param localSendPort 本端发送包的时候的端口，可选，如果小于等于0，那就是使用随机端口
     * @param receiveQueueSize 接收队列大小
     */
    public UdpSocketClient(String targetHost, int targetPort, int localSendPort, int receiveQueueSize) {
        this(targetHost, targetPort, localSendPort, -1, receiveQueueSize, charsetDefault);
    }

    /**
     * @param targetHost 目标主机，必须
     * @param targetPort 目标端口，必须
     * @param localSendPort 本端发送包的时候的端口，可选，如果小于等于0，那就是使用随机端口
     * @param localReceivePort 如果不指定接收端口，也就是小于等于0的时候，那就使用发送端口作为接收端端口，一般情况就是这样使用的
     * @param receiveQueueSize 接收队列大小
     * @param charset 字符集
     */
    public UdpSocketClient(String targetHost, int targetPort, int localSendPort, int localReceivePort, int receiveQueueSize, String charset) {

        this.targetHost = targetHost;
        this.targetPort = targetPort;
        this.localSendPort = localSendPort;
        this.localReceivePort = localReceivePort;

        this.receiveQueue = new ArrayBlockingQueue<>(receiveQueueSize);
        this.charset = charset;

        //创建socket
        createSocket();
    }

    @Override
    public void run() {

        if (!status) {
            LOG.warn("创建socket状态失败,启动失败");
            return;
        }else{
            LOG.info("开始启动监听");
        }
        try {
            startReceiveListen();
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            close();
        }
        LOG.info("线程停止");
    }

    public void startAsThread() {
        if (status) {
            new Thread(this).start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }
    /**
     * 接收监听，同时检测是否发生异常，
     * 如果连续三次发生的异常时间间隔在100ms内，说明程序出现严重错误，得退出
     */
    private void startReceiveListen() throws Exception {

        //上一次的异常时间点
        while (status) {
            DatagramPacket receivePacket = UdpSocketUtils.createReceiveDatagramPacketDefault();
            receiveSocket.receive(receivePacket);
            this.receiveQueue.add(receivePacket);
        }
    }

    /**
     * 创建socket，先创建发送端，后创建监听端
     */
    private void createSocket() {
        sendSocket = UdpSocketUtils.createUdpDatagramSocket(null, localSendPort, bandPortRetries, socketTimeout);

        if (sendSocket != null) {
            LOG.info("创建发送Socket成功,绑定本地Socket:{}", sendSocket.getLocalSocketAddress());
        }
        //小于等于0时，使用发送端也作为接收端
        if (this.localReceivePort <= 0) {
            receiveSocket = sendSocket;
        }else{
            receiveSocket = UdpSocketUtils.createUdpDatagramSocket(null, localReceivePort, bandPortRetries, socketTimeout);
        }
        if (receiveSocket != null) {
            LOG.info("创建接收Socket成功,绑定本地Socket:{}", receiveSocket.getLocalSocketAddress());
        }
        if (sendSocket != null && receiveSocket != null) {
            LOG.info("创建Socket成功,本地发送Socket:{}, 本地接收Socket:{}", sendSocket.getLocalSocketAddress(), receiveSocket.getLocalSocketAddress());
            status = true;
        }else{
            LOG.info("创建Socket失败,本地发送Socket:{}, 本地接收Socket:{}", sendSocket.getLocalSocketAddress(), receiveSocket.getLocalSocketAddress());
            status = false;
        }

    }

    /**
     * 发送数据
     * @param sendDataBytes
     * @return
     */
    public boolean sendData(byte[] sendDataBytes) {
        if (!status) {
            LOG.warn("没有处于运行状态，不能发送数据");
            return false;
        }
        boolean sendStatus;
        try {
            DatagramPacket sendDatagramPacket = UdpSocketUtils.createSendDatagramPacket(targetHost, targetPort, sendDataBytes);
            sendSocket.send(sendDatagramPacket);
            sendStatus = true;
        } catch (Exception e) {
            sendStatus = false;
            LOG.error("UDP客户端发送数据时异常", e);
        }
        if (sendStatus) {
            LOG.info("发送数据成功");
        }else{
            LOG.info("发送数据失败");
        }
        return sendStatus;
    }

    /**
     * 发送数据
     * @param data 字符串数据
     * @return
     */
    public boolean sendData(String data) {
        boolean sendStatus;
        try {
            sendStatus = sendData(data.getBytes(charset));
        } catch (Exception e) {
            LOG.error(e);
            sendStatus = false;
        }
        return sendStatus;
    }

    /**
     * 关闭
     */
    public void close() {
        status = false;
        UdpSocketUtils.close(sendSocket);
        UdpSocketUtils.close(receiveSocket);
        LOG.info("关闭完成");
    }

    /**
     * 获取一个接收数据
     * 无限等待获取一个数据为止
     * @return
     */
    public DatagramPacket getReceivePacket() {

        try {
            return this.receiveQueue.take();
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }


    /**
     * 获取一个接收数据
     * 无限等待获取一个数据为止
     * @return
     */
    public String getOneReceiveDataToString() {

        try {
            DatagramPacket packet = this.receiveQueue.take();
            if (packet != null) {
                return UdpSocketUtils.readDatagramPacketToString(packet, charset);
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }
    /**
     * 获取一个接收数据
     * 无限等待获取一个数据为止
     * @return
     */
    public String getCurrentAllReceiveDataToString() {

        try {
            DatagramPacket packet = this.receiveQueue.take();
            if (packet != null) {
                byte[] bytes = packet.getData();
                while (this.receiveQueue.size() >= 1) {
                    DatagramPacket packetTmp = this.receiveQueue.poll();
                    if (packetTmp == null) {
                        break;
                    }else{
                        byte[] data = packetTmp.getData();
                        bytes = ArrayUtils.addAll(bytes, data);
                    }
                }
                return UdpSocketUtils.readDatagramPacketToString(packet, charset);
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }
    /**
     * 获取一个接收数据
     * 无限等待获取一个数据为止
     * @return
     */
    public List<DatagramPacket> getCurrentAllReceivePacket() {
        List<DatagramPacket> datas = new ArrayList<>();
        try {
            while (status) {
                DatagramPacket data = this.receiveQueue.poll();
                if (data == null) {
                    break;
                }else{
                    datas.add(data);
                }
            }

        } catch (Exception e) {
            LOG.error(e);
        }
        return datas;
    }


    /**
     * 获取一个接收数据
     * @param timeout 超时，毫秒
     * @return
     */
    public DatagramPacket getReceivePacket(long timeout) {

        try {
            return this.receiveQueue.poll(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }
}
