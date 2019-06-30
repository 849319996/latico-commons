package com.latico.commons.common.util.net;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;

import java.net.*;

/**
 * <PRE>
 * UDP协议的socket工具
 * 一、UDP是无连接协议，所以不需要创建连接，数据发送逻辑是：
 * 1、创建一个UDP Socket绑定一个本地端口（可以指定端口或者随机端口）；
 * 2、用Socket发送报文，目标IP和目标端口都是在报文中指定，所以一个UDP socket可以发送给任何IP和端口组合；
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-15 10:12
 * @Version: 1.0
 */
public class UdpSocketUtils {

    private static final Logger LOG = LoggerFactory.getLogger(UdpSocketUtils.class);

    /**
     * 发送数据
     *
     * @param targetHost 目标主机
     * @param targetPort 目标端口
     * @param data       数据，字节数组
     * @return
     */
    public static boolean sendData(String targetHost, int targetPort, byte[] data) {
        DatagramSocket udpSocket = createUdpDatagramSocket(3, 0);
        return sendData(udpSocket, targetHost, targetPort, data, data.length);
    }

    /**
     * 发送数据
     *
     * @param udpSocket  本地的UDP socket对象
     * @param targetHost 目标主机
     * @param targetPort 目标端口
     * @param data       数据，字节数组
     * @return
     */
    public static boolean sendData(DatagramSocket udpSocket, String targetHost, int targetPort, byte[] data) {
        return sendData(udpSocket, targetHost, targetPort, data, data.length);
    }

    /**
     * 发送数据
     *
     * @param udpSocket  本地的UDP socket对象
     * @param targetHost 目标主机
     * @param targetPort 目标端口
     * @param data       数据，字节数组
     * @param dataLen    要从字节数组里面取多少个字节发送
     * @return
     */
    public static boolean sendData(DatagramSocket udpSocket, String targetHost, int targetPort, byte[] data, int dataLen) {
        if (udpSocket == null || StringUtils.isBlank(targetHost) || data == null) {
            LOG.warn("传入数据有空不能创建UDP [Socket:{}] [targetHost:{}]", udpSocket, targetHost);
            return false;
        }
        boolean sendStatus = false;
        try {
            //组装数据包
            DatagramPacket udpDataPacket = new DatagramPacket(data, dataLen, InetAddress.getByName(targetHost), targetPort);
            udpSocket.send(udpDataPacket);
            sendStatus = true;
        } catch (Exception e) {
            sendStatus = false;
            LOG.error("UDP数据发送失败", e);
        }

        return sendStatus;
    }

    /**
     * 获取UDP报文Socket，随机绑定一个本地端口，失败会重试
     * @param retries 必须大于等于1
     * @param socketTimeout 可以小于等于0，这样就无限制
     * @return
     * @throws Exception
     */
    public static DatagramSocket createUdpDatagramSocket(int retries, int socketTimeout) {
        return createUdpDatagramSocket(null, -1, retries, socketTimeout);
    }

    /**
     * 获取UDP报文Socket，绑定端口，失败会重试
     * @param localHost     指定本端主机,只监听该IP下的，可以为空
     * @param localPort 要绑定的本地端口，不指定就输入小于等于0，那就会自动随机获取空闲端口
     * @param retries 必须大于等于1
     * @param socketTimeout 可以小于等于0，这样就无限制
     * @return
     */
    public static DatagramSocket createUdpDatagramSocket(String localHost, int localPort, int retries, int socketTimeout) {
        if(retries-- <= 0){
            LOG.error("尝试次数用完，尝试获取端口失败");
            return null;
        }
        //获取失败的时候，重试等待时间
        long bindingPortWaitTime = 1000;
        DatagramSocket udpSocket = null;

        try {
            //指定端口方式
            if (localPort >= 1) {
                if (localHost == null || "".equals(localHost)) {
                    udpSocket = new DatagramSocket(localPort);
                }else{
                    udpSocket = new DatagramSocket(localPort, InetAddress.getByName(localHost));
                }

            }else{
                udpSocket = new DatagramSocket();
            }

            //设置会话时间
            if (udpSocket != null && socketTimeout >= 1) {
                udpSocket.setSoTimeout(socketTimeout);//设置超时时间
            }
        } catch (Exception e) {
            LOG.warn("UDP绑定本地端口 [{}]失败，还剩[{}]次机会，[{}]ms后重试 ...", localPort >=1 ? localPort:"随机", retries, bindingPortWaitTime);
        }

        if (udpSocket == null) {
            //获取前等待
            try {Thread.sleep(bindingPortWaitTime);} catch (InterruptedException e) {}
            udpSocket = createUdpDatagramSocket(localHost, localPort, retries, socketTimeout);
        }

        //打印结果日志
        if (udpSocket != null){
            LOG.info("UDP Socket创建成功,绑定本地端口 [{}]", localPort >=1 ? localPort:"随机");
        }else{
            LOG.info("UDP Socket创建失败,绑定本地端口 [{}]", localPort >=1 ? localPort:"随机");
        }

        return udpSocket;
    }

    /**
     * 创建默认的接收数据包，用于接收数据
     * @return
     */
    public static DatagramPacket createReceiveDatagramPacketDefault() {
        return createReceiveDatagramPacket(1500);
    }
    /**
     * 创建UDP接收数据包，用于接收数据
     * @param bufByteSize 数据缓冲区最大字节数
     * @return
     */
    public static DatagramPacket createReceiveDatagramPacket(int bufByteSize) {
        //创建装数据的字节数组，UDP的默认MTU是1500
        byte[] buf = new byte[bufByteSize];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        return packet;
    }

    /**
     * 创建UDP数据包
     * @param targetIp
     * @param targetPort
     * @param sendDataBytes
     * @param dataLen
     * @return
     * @throws Exception
     */
    public static DatagramPacket createSendDatagramPacket(String targetIp, int targetPort, byte[] sendDataBytes, int dataLen) throws Exception {
        //发送到服务器的数据报包
        return new DatagramPacket(sendDataBytes, dataLen, InetAddress.getByName(targetIp), targetPort);
    }
    /**
     * 创建UDP数据包
     * @param address       目标socket地址
     * @param sendDataBytes 发送数据包
     * @param dataLen 数据长度
     * @return
     * @throws Exception
     */
    public static DatagramPacket createSendDatagramPacket(SocketAddress address, byte[] sendDataBytes, int dataLen) throws Exception {
        //发送到服务器的数据报包
        return new DatagramPacket(sendDataBytes, dataLen, address);
    }


    /**
     * 创建UDP数据包
     * @param targetIp
     * @param targetPort
     * @param sendDataBytes
     * @return
     * @throws Exception
     */
    public static DatagramPacket createSendDatagramPacket(String targetIp, int targetPort, byte[] sendDataBytes) throws Exception {
        //发送到服务器的数据报包
        return new DatagramPacket(sendDataBytes, sendDataBytes.length, InetAddress.getByName(targetIp), targetPort);
    }

    /**
     * 创建UDP数据包
     * @param address
     * @param sendDataBytes
     * @return
     * @throws Exception
     */
    public static DatagramPacket createSendDatagramPacket(SocketAddress address, byte[] sendDataBytes) throws Exception {
        //发送到服务器的数据报包
        return new DatagramPacket(sendDataBytes, sendDataBytes.length, address);
    }

    /**
     * 发送
     * @param localSocket 绑定了本地端口的socket
     * @param targetHost 目标主机
     * @param targetPort
     * @param sendDataBytes
     * @param dataLen
     * @return
     */
    public static boolean sendDatagramPacket(DatagramSocket localSocket, String targetHost, int targetPort, byte[] sendDataBytes, int dataLen) {
        boolean status = false;
        try {
            DatagramPacket sendDatagramPacket = createSendDatagramPacket(targetHost, targetPort, sendDataBytes, dataLen);
            localSocket.send(sendDatagramPacket);
            status = true;
        } catch (Exception e) {
            LOG.error(e);
            status = false;
        }
        return status;
    }


    /**
     * 指定Socket发送UDP报文出去
     * @param localSocket 绑定了本地端口的socket
     * @param targetHost 目标主机
     * @param targetPort
     * @param sendDataBytes
     * @return
     */
    public static boolean sendDatagramPacket(DatagramSocket localSocket, String targetHost, int targetPort, byte[] sendDataBytes) {
        boolean status = false;
        try {
            DatagramPacket sendDatagramPacket = createSendDatagramPacket(targetHost, targetPort, sendDataBytes, sendDataBytes.length);
            localSocket.send(sendDatagramPacket);
            status = true;
        } catch (Exception e) {
            LOG.error(e);
            status = false;
        }
        return status;
    }
    /**
     * 随机生成一个Socket发送UDP报文出去
     * @param targetHost 目标主机
     * @param targetPort
     * @param sendDataBytes
     * @return
     */
    public static boolean sendDatagramPacket(String targetHost, int targetPort, byte[] sendDataBytes) {
        boolean status = false;
        try {
            DatagramPacket sendDatagramPacket = createSendDatagramPacket(targetHost, targetPort, sendDataBytes, sendDataBytes.length);
            DatagramSocket localSocket = createUdpDatagramSocket(3, -1);
            localSocket.send(sendDatagramPacket);
            status = true;
        } catch (Exception e) {
            LOG.error(e);
            status = false;
        }
        return status;
    }

    /**
     * 关闭UDP socket
     * @param socket
     */
    public static void close(DatagramSocket socket) {
        if (socket != null) {
            socket.close();
        }
    }

    /**
     * 读报文成String
     * @param packet
     * @param charset
     * @return
     */
    public static String readDatagramPacketToString(DatagramPacket packet, String charset) {
        try {
            return new String(packet.getData(), 0, packet.getLength(), charset);
        } catch (Exception e) {
            LOG.error(e);
        }
        return "";
    }
}
