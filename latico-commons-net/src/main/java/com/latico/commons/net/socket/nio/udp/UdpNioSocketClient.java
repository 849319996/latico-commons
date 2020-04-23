package com.latico.commons.net.socket.nio.udp;

import java.io.IOException;
import java.net.*;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-21 0:37
 * @version: 1.0
 */
public class UdpNioSocketClient implements Runnable {
    private DatagramSocket datagramSocket;

    public UdpNioSocketClient(int port) throws SocketException {
        datagramSocket = new DatagramSocket(port);
    }

    public void test(String str, SocketAddress address) throws Exception{
        byte [] bytes = str.getBytes("utf-8");
        DatagramPacket datagramPacket = new DatagramPacket(bytes,0,bytes.length,address);
        datagramSocket.send(datagramPacket);
    }

    public static void main(String []args) throws Exception {
        UdpNioSocketClient sed = new UdpNioSocketClient(9998);
        new Thread(sed).start();
        sed.test("1234567890",new InetSocketAddress("127.0.0.1",8888));

        Thread.sleep(1000000);
    }

    @Override
    public void run() {
        while(true){
            byte[] bytes = new byte[1024];
            DatagramPacket datagramPacket = new DatagramPacket(bytes,0,bytes.length);
            try {
                datagramSocket.receive(datagramPacket);
                System.out.println("收到数据："+new String(datagramPacket.getData(),0,datagramPacket.getLength()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
