package com.latico.commons.net.socket.io.udp;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.net.UdpSocketUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * <PRE>
 * 服务器端，发送用的socket和接收用的socket的关系应该跟客户端相反
 *
 * DatagramPacket包含了客户端的IP和端口，所以做业务处理的时候，需要注意判断是哪个客户端IP的包
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-15 15:07
 * @Version: 1.0
 */
public class UdpSocketServer implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(UdpSocketServer.class);

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
    private String localHost;
    /**
     * 本端发送包的时候的端口，可选，如果小于等于0，那就是使用随机端口，默认使用随机端口
     */
    private int localSendPort = -1;

    /**
     * 本地接收消息的端口
     */
    private int localReceivePort = -1;
    /**
     * 发送用的socket，跟接收的一样
     */
    private DatagramSocket sendSocket;
    /**
     * 接收消息的socket
     */
    private DatagramSocket receiveSocket;

    /**
     * 接收数据队列，DatagramPacket包含了客户端的IP和端口，所以做业务处理的时候，需要注意判断是哪个客户端IP的包
     */
    private BlockingQueue<DatagramPacket> receiveQueue;

    /**
     * 创建socket的时候，绑定端口重试次数
     */
    private int bandPortRetries = 3;

    /**
     * @param localReceivePort 如果不指定接收端口，也就是小于等于0的时候，那就使用发送端口作为接收端端口，一般情况就是这样使用的
     */
    public UdpSocketServer(int localReceivePort, int receiveQueueSize) {
        this(null, -1, localReceivePort, receiveQueueSize, charsetDefault);
    }

    /**
     * @param localHost        指定只监听本端的这个IP，可以为空
     * @param localSendPort 本端发送包的时候的端口，可选，如果小于等于0，那就是使用随机端口
     * @param localReceivePort 如果不指定接收端口，也就是小于等于0的时候，那就使用发送端口作为接收端端口，一般情况就是这样使用的
     * @param receiveQueueSize
     * @param charset
     */
    public UdpSocketServer(String localHost, int localSendPort, int localReceivePort, int receiveQueueSize, String charset) {
        this.localHost = localHost;

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

        while (status) {
            DatagramPacket receivePacket = UdpSocketUtils.createReceiveDatagramPacketDefault();
            receiveSocket.receive(receivePacket);
            this.receiveQueue.add(receivePacket);
        }
    }

    /**
     * 创建socket，先创建监听端，后创建发送端
     */
    private void createSocket() {
        receiveSocket = UdpSocketUtils.createUdpDatagramSocket(localHost, localReceivePort, bandPortRetries, -1);
        if (receiveSocket != null) {
            LOG.info("创建接收Socket成功,绑定本地Socket:{}", receiveSocket.getLocalSocketAddress());
        }

        //小于等于0时，使用接收端也作为发送端
        if (this.localSendPort <= 0) {
            sendSocket = receiveSocket;
        }else{
            sendSocket = UdpSocketUtils.createUdpDatagramSocket(localHost, localSendPort, bandPortRetries, -1);
        }
        if (sendSocket != null) {
            LOG.info("创建发送Socket成功,绑定本地Socket:{}", sendSocket.getLocalSocketAddress());
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
    public boolean sendData(byte[] sendDataBytes, String targetHost, int targetPort) {
        InetSocketAddress socketAddress = new InetSocketAddress(targetHost, targetPort);
        return sendData(sendDataBytes, socketAddress);
    }

    /**
     * 发送数据
     * @param sendDataBytes
     * @return
     */
    public boolean sendData(byte[] sendDataBytes, SocketAddress socketAddress) {
        if (!status) {
            LOG.warn("没有处于运行状态，不能发送数据");
            return false;
        }
        boolean sendStatus;
        try {
            DatagramPacket sendDatagramPacket = UdpSocketUtils.createSendDatagramPacket(socketAddress, sendDataBytes);
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
     * @param packet
     * @return
     */
    public boolean sendData(DatagramPacket packet) {
        boolean sendStatus;
        try {
            sendSocket.send(packet);
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
    public boolean sendData(String data, String targetHost, int targetPort) {
        boolean sendStatus;
        try {
            sendStatus = sendData(data.getBytes(charset), targetHost, targetPort);
        } catch (Exception e) {
            LOG.error(e);
            sendStatus = false;
        }
        return sendStatus;
    }
    /**
     * 发送数据
     * @param data 字符串数据
     * @return
     */
    public boolean sendData(String data, SocketAddress socketAddress) {
        boolean sendStatus;
        try {
            sendStatus = sendData(data.getBytes(charset), socketAddress);
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
    public String getReceivePacketToString() {

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
