package com.latico.commons.net.socket.nio.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * <PRE>
 * 在没有绑定固定的远程地址和端口只能使用receive和send方法，绑定了远程地址和端口能使用read和write方法
 * <p>
 * 1、没有绑定固定远程地址和端口，只能使用recive和send
 * <p>
 *        datagramChannel.send(write,socketAddress);  发送方法
 * <p>
 *        datagramChannel.receive(buffer);  读取方法，返回SocketAddress
 * <p>
 *  2、绑定固定的远程地址和远程端口
 * <p>
 *        TestDatagramSocketChannel的构造函数中加入
 * <p>
 *        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1",9998);
 *        datagramChannel.connect(socketAddress);
 * <p>
 *        后可以直接使用int readLength = datagramChannel.read(buffer);代替datagramChannel.receive(buffer); 
 * <p>
 *        使用datagramChannel.write(write);代替datagramChannel.send(write,socketAddress);发送数据
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-21 0:37
 * @version: 1.0
 */
public class UdpNioSocketServer {

    private Selector selector;
    DatagramChannel datagramChannel;
    public UdpNioSocketServer() throws IOException {
        selector = Selector.open();
        datagramChannel = DatagramChannel.open();
        datagramChannel.socket().bind(new InetSocketAddress(8888));
        datagramChannel.configureBlocking(false);
        datagramChannel.register(selector, SelectionKey.OP_READ);  //设置成读取操作
    }

    public void testChannel() throws IOException {
        byte bytes[] = new byte[1024];
        int length = 0;
        while (true) {
            int num = selector.select(5000);
            if (num > 0) {
                System.out.println("执行数量：" + num);
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                if (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    DatagramChannel datagramChannel = null;
                    if (selectionKey.isReadable()) {
                        System.out.println("执行读");
                        datagramChannel = (DatagramChannel) selectionKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(512);
                        datagramChannel.receive(buffer);

                        String str = new String(buffer.array(), 0, buffer.limit(), "utf-8");
                        System.out.println("收到数据：" + str);
                        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 9998);
                        this.datagramChannel.send(ByteBuffer.wrap(str.getBytes("utf-8")), socketAddress);


                        datagramChannel.register(selector, SelectionKey.OP_WRITE);
                    } else if (selectionKey.isWritable()) {
                        System.out.println("执行写");
                        datagramChannel = (DatagramChannel) selectionKey.channel();
                        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 9998);
                        datagramChannel.connect(socketAddress);
                        String string = "123456789";
                        byte[] t = string.getBytes("UTF-8");
                        ByteBuffer write = ByteBuffer.wrap(t);
                        while (write.hasRemaining()) {
                            datagramChannel.send(write, socketAddress);
                        }
                        datagramChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isAcceptable()) {
                        System.out.println(selectionKey.hashCode() + "接收事件");
                        DatagramChannel ssChannel = (DatagramChannel) selectionKey.channel();
                        ssChannel.configureBlocking(false);
                        ssChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
                    }
                    iterator.remove();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new UdpNioSocketServer().testChannel();
    }

}
