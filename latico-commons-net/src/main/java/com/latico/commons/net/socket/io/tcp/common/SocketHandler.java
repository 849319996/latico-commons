package com.latico.commons.net.socket.io.tcp.common;

import com.latico.commons.common.util.collections.ArrayUtils;
import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.net.TcpSocketUtils;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <PRE>
 *     假如是用线程方式启动，会不断的从输入流中读取数据进缓存队列中
 *     Socket流处理器
 *    1、 支持阻塞和非阻塞方式，默认使用阻塞方式，效率更高
 *    2、 注意：假如是使用对象传输，客户端在发送对象给服务端的时候，一个对象只能发一次，如果只是改了参数，同一个对象发了多次，服务端接收到的数据会有问题.
 *
 * 3、在网络通讯中，主机与客户端若使用ObjectInputStream与ObjectOutputStream建立对象通讯时，有时会发生线程阻塞问题。
 * 这是因为当从InputStream创建一个ObjectInputStream时，需要从流中读入并验证一个Header，这时如果对方的ObjectOutputStream没有写入一个Header，ObjectInputStream的构造函数便会阻塞（block）。
 * 解决这个问题的方法是调整ObjectInputStream与ObjectOutputStream的声明顺序
 * 比如：主机端先建立ObjectInputStream后建立ObjectOutputStream，则对应地客户端要先建立ObjectOutputStream后建立ObjectInputStream

 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-16 17:47
 * @Version: 1.0
 */
public class SocketHandler implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(SocketHandler.class);
    private Socket socket;
    private String charset = "UTF-8";

    /**
     * 总状态
     */
    private AtomicBoolean status = new AtomicBoolean(false);

    /**
     * 客户端模式
     */
    private boolean clientMode;

    /** dataReader 数据读取流//需要用字节流读取，用字符流会有中文乱码 */
    protected InputStream dataReader;

    /**
     * 数据缓冲
     */
    protected BufferedInputStream buffDataReader;

    /** dataWriter，数据输出流，打印输出流 ，方便发送 */
    protected PrintStream dataWriter;

    /** objReader 读对象 */
    protected ObjectInputStream objReader;

    /** objWriter 写对象 */
    protected ObjectOutputStream objWriter;

    /**
     * 接收对象数据时使用的队列，对象必须序列化
     */
    private BlockingQueue<Object> receiveObjectQueue;

    /**
     * 字节形式接收数据时使用的队列
     */
    private BlockingQueue<byte[]> receiveBytesQueue;

    private int receiveQueueSize = 10000;
    /**
     * 是否传输对象,true：只开放对象的发送和接收，false:只开放字节的队列接收字节形式的数据
     */
    private boolean transferObject;

    /**
     * 是否阻塞模式，默认true，效果和效率更好
     */
    private boolean blockMode = true;

    /**
     * 字节缓冲区大小，1KB
     */
    private int byteBuffSize = 1024;

    public SocketHandler(Socket socket, boolean clientMode) {
        this(socket, clientMode, 10000);
    }
    /**
     * @param clientMode   是不是客户端模式，如果是在客户端创建的socket需要为true，
     *                     因为对于对象型传输流创建，客户端跟服务端创建流的顺序不一样，
     *                     客户端应该先创建输出流后创建输入流，服务端相反
     * @param socket 通信socket
     * @param receiveQueueSize 接收数据的时候的队列大小，当对象型时是对象个数，当字节行数据时是字节数组的个数
     */
    public SocketHandler(Socket socket, boolean clientMode, int receiveQueueSize) {
        this(socket, clientMode, receiveQueueSize, false, null);
    }
    /**
     * @param clientMode       是不是客户端模式，如果是在客户端创建的socket需要为true，
     *                         因为对于对象型传输流创建，客户端跟服务端创建流的顺序不一样，
     *                         客户端应该先创建输出流后创建输入流，服务端相反
     * @param socket 通信socket
     * @param receiveQueueSize 接收数据的时候的队列大小，当对象型时是对象个数，当字节行数据时是字节数组的个数
     * @param isTransferObject 是否使用传输对象模式
     * @param charset
     */
    public SocketHandler(Socket socket, boolean clientMode, int receiveQueueSize, boolean isTransferObject, String charset) {
        this.clientMode = clientMode;
        this.socket = socket;
        this.receiveQueueSize = receiveQueueSize;
        this.transferObject = isTransferObject;
        if (charset != null) {
            this.charset = charset;
        }

    }

    @Override
    public void run() {
        //如果是传输对象模式，就用对象队列
        if (transferObject) {
            this.receiveObjectQueue = new ArrayBlockingQueue<>(receiveQueueSize);
        }else{
            this.receiveBytesQueue = new ArrayBlockingQueue<>(receiveQueueSize);
        }

        //创建数据流
        createStream();

        LOG.info("启动TcpSocketHandler");
        try {
            if (transferObject) {
                startReceiveObjectListen();

            }else{
                //对于字节型，可以阻塞和非阻塞
                if (blockMode) {
                    //阻塞方式
                    startReceiveByteListenBlock();
                }else{
                    //非阻塞方式
                    startReceiveByteListenNotBlock();
                }

            }

        } catch (Exception e) {
            LOG.error(e);
        } finally {
            close();
        }
        LOG.info("线程停止");
    }

    /**
     * 创建输入输出流
     */
    private void createStream() {
        try {
            //客户端模式建立流的顺序
            if (clientMode) {
                // 获取输出流对象
                dataWriter = new PrintStream(socket.getOutputStream());

                //如果传输对象的情况下需要构造对象输入输出流
                if (transferObject) {
                    //客户端，先创建对象输出流
                    objWriter = new ObjectOutputStream(socket.getOutputStream());
                }

                // 获取输入流对象
                dataReader = socket.getInputStream();

                //用缓冲区包装
                buffDataReader = new BufferedInputStream(dataReader);

                //如果传输对象的情况下需要构造对象输入输出流
                if (transferObject) {
                    //后创建创建对象输入流
                    objReader = new ObjectInputStream(dataReader);
                }

            }else{
            //服务端模式建立流的顺序
                // 获取输入流对象
                dataReader = socket.getInputStream();

                //用缓冲区包装
                buffDataReader = new BufferedInputStream(dataReader);

                //如果传输对象的情况下需要构造对象输入输出流
                if (transferObject) {
                    //后创建创建对象输入流
                    objReader = new ObjectInputStream(dataReader);
                }

                // 获取输出流对象
                dataWriter = new PrintStream(socket.getOutputStream());

                //如果传输对象的情况下需要构造对象输入输出流
                if (transferObject) {
                    //客户端，先创建对象输出流
                    objWriter = new ObjectOutputStream(socket.getOutputStream());
                }
            }

            status.set(true);
        } catch (Exception e) {
            LOG.error(e);
            status.set(false);
        }
    }

    /**
     * 监听对象
     * @throws Exception
     */
    private void startReceiveObjectListen() throws Exception {
        LOG.info("启动对象类型数据监听获取");
        while (isValid()) {
            if (!isValid()) {
                break;
            }
            Object object = null;
            try {
                object = objReader.readObject();
            } catch (EOFException e) {
                //readObject非阻塞，获取不到对象的时候会报这个异常，不处理
            }

            if (object != null) {
                receiveObjectQueue.add(object);
            }else{
                Thread.sleep(100);
            }

        }

    }

    private boolean isValid() {
        return status.get() && !socket.isClosed() && socket.isBound() && socket.isConnected();
    }

    /**
     * 非阻塞方式（一般情况不建议使用）
     * 会先判断流中是否有数据才读取，
     * 有点：非阻塞
     * 缺点：需要手动睡眠，不好控制效率
     * @throws Exception
     */
    private void startReceiveByteListenNotBlock() throws Exception {

        LOG.info("启动字节类型数据监听获取(IO不阻塞)");
        final long sleepWhenNotHasData = 200;
        final long sleepWhenHasData = 50;

        while (true) {
            if(!isValid()){
                break;
            }
            int available = buffDataReader.available();

            //流中没有数据的时候，睡眠更长时间
            if (available <= 0) {
                Thread.sleep(sleepWhenNotHasData);

            }else{
                byte[] buff = new byte[available];
                buffDataReader.read(buff);

                LOG.debug("本次读取到字节数:" + available);
                receiveBytesQueue.add(buff);
                //流中有数据的时候，睡眠一下让更多数据进入流中
                Thread.sleep(sleepWhenHasData);
            }
        }
    }


    /**
     * 阻塞方式
     * @throws Exception
     */
    private void startReceiveByteListenBlock() throws Exception {

        LOG.info("启动字节类型数据监听获取(IO阻塞)");

        while (true) {
            if(!isValid()){
                break;
            }
            byte[] buff = new byte[byteBuffSize];
            int readSize = buffDataReader.read(buff);

            LOG.debug("本次读取到字节数:" + readSize);

            //读取到流结束，当对端把流关闭了，那么流就断开，调用吗read()方法会返回-1，调用多少次都是返回-1
            if (readSize <= -1) {
                LOG.info("读取到流结束");
                break;
            }

            //读取不到数据，下一轮监听
            if (readSize == 0) {
                continue;
            }

            //如果读取到的跟缓存区大小一样，那么直接添加，不需要截取
            if (readSize == byteBuffSize) {
                receiveBytesQueue.add(buff);
            }else{
                //根据接收到的实际字节数截取子数组
                byte[] subarray = ArrayUtils.subarray(buff, 0, readSize);
                receiveBytesQueue.add(subarray);
            }


        }
    }
    /**
     * 关闭
     */
    public void close() {
        status.set(false);
        TcpSocketUtils.close(socket);
        IOUtils.close(objReader);
        IOUtils.close(objWriter);
        IOUtils.close(buffDataReader);
        IOUtils.close(dataWriter);
        IOUtils.close(dataReader);
        LOG.info("关闭所有连接资源完成");

    }


    /**
     * 不包括换行符
     * @param data 字节数据
     * @return
     */
    public boolean sendData(byte[] data) {
        try {
            dataWriter.write(data);
            dataWriter.flush();
            return true;
        } catch (Exception e) {
            LOG.error(e);
        }
        return false;
    }


    /**
     * 自动包括换行符
     * @param data 字节数据
     * @return
     */
    public boolean sendDataAutoLineFeed(byte[] data) {
        try {
            dataWriter.write(data);
            dataWriter.println();
            dataWriter.flush();
            return true;
        } catch (Exception e) {
            LOG.error(e);
        }
        return false;
    }

    /**
     * 不包括换行符
     * @param data 字符串数据
     * @return
     */
    public boolean sendData(String data) {
        try {
            dataWriter.print(data);
            dataWriter.flush();
            LOG.info("发送数据完成:{}", data);
            return true;
        } catch (Exception e) {
            LOG.error(e);
        }
        return false;
    }

    /**
     * 发送字符串数据，自动带上系统换行符
     * @param data 字符串数据
     * @return
     */
    public boolean sendDataAutoLineFeed(String data) {
        try {
            dataWriter.println(data);
            dataWriter.flush();
            LOG.info("发送数据完成:{}", data);
            return true;
        } catch (Exception e) {
            LOG.error(e);
        }
        return false;
    }

    /**
     * 对象方式通信，发送对象
     * @param dataObj
     * @return
     */
    public boolean sendObjectData(Object dataObj) {
        if (!isValid() || !transferObject) {
            LOG.error("没有登录或者不是传输对象模式，不能发送对象数据");
            return false;
        }

        boolean succ;
        try {
            objWriter.writeObject(dataObj);
            objWriter.flush();
            succ = true;
        } catch (Exception e) {
            LOG.error("", e);
            succ = false;
        }

        return succ;
    }

    /**
     * 无限等待获取一个对象
     * @return
     */
    public Object getAndRemoveReceiveDataObject() {
        try {
            while (isValid() && transferObject) {

                Object obj = receiveObjectQueue.poll(3, TimeUnit.SECONDS);
                if (obj != null) {
                    return obj;
                }

            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    /**
     * 获取一个接收数据
     * @param timeout 超时，毫秒
     * @return
     */
    public Object getAndRemoveReceiveDataObject(long timeout) {
        if (isValid() && transferObject) {
            try {
                return receiveObjectQueue.poll(timeout, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                LOG.error(e);
            }
        }
        return null;
    }

    /**
     * 无限等待获取一个对象
     * @return
     */
    public byte[] getAndRemoveReceiveDataBytes() {
        try {
            while (isValid() && !transferObject) {
                byte[] bytes = receiveBytesQueue.poll(3, TimeUnit.SECONDS);
                if (bytes != null) {
                    return bytes;
                }

            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    /**
     * 有超时等待获取一个对象
     * @return
     */
    public byte[] getAndRemoveReceiveDataBytes(long timeout) {
        if (isValid() && !transferObject) {
            try {
                return receiveBytesQueue.poll(timeout, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                LOG.error(e);
            }
        }
        return null;
    }

    /**
     * 无限等待获取一个字节
     * @return
     */
    public byte[] getAndRemoveCurrentAllReceiveDataByte() {
        byte[] bytes = new byte[]{};
        long waitTimeWhenHasData = 100;
        try {
            byte[] bytesTmp = null;

            if (isValid() && !transferObject) {
                bytesTmp = receiveBytesQueue.poll(1, TimeUnit.SECONDS);
            }
            do {
                if (bytesTmp == null) {
                    if (bytes.length >= 1) {
                        break;
                    }
                }else{
                    if (bytesTmp.length >= 1) {
                        bytes = ArrayUtils.addAll(bytes, bytesTmp);
                    }
                }

                if (!isValid() || transferObject) {
                    break;
                }
                bytesTmp = receiveBytesQueue.poll(waitTimeWhenHasData, TimeUnit.MILLISECONDS);
            } while (true);

        } catch (Exception e) {
            LOG.error(e);
        }

        return bytes;
    }
    /**
     * 无限等待获取一个对象
     * @return
     */
    public String getAndRemoveCurrentAllReceiveDataString() {
        byte[] currentAllReceiveDataByte = getAndRemoveCurrentAllReceiveDataByte();
        if (currentAllReceiveDataByte == null) {
            return null;
        }
        try {
            return new String(currentAllReceiveDataByte, charset);
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    /**
     * 无限等待获取一个对象
     * @return
     */
    public String getAndRemoveCurrentAllReceiveDataString(long timeout) {
        byte[] currentAllReceiveDataByte = getAndRemoveCurrentAllReceiveDataByte(timeout);
        if (currentAllReceiveDataByte == null) {
            return null;
        }
        try {
            return new String(currentAllReceiveDataByte, charset);
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    /**
     * 无限等待获取一个对象
     * @return
     */
    public byte[] getAndRemoveCurrentAllReceiveDataByte(long timeout) {
        if (!isValid() || transferObject) {
            return null;
        }

        long waitTimeWhenHasData = 100;
        try {
            byte[] bytes = receiveBytesQueue.poll(timeout, TimeUnit.MILLISECONDS);
            if (bytes == null) {
                return null;
            }

            while (isValid()) {
                byte[] bytesTmp = receiveBytesQueue.poll(waitTimeWhenHasData, TimeUnit.MILLISECONDS);
                if (bytesTmp == null) {
                    break;
                }else{
                    if (bytesTmp.length >= 1) {
                        bytes = ArrayUtils.addAll(bytes, bytesTmp);
                    }

                }
            }

            if (bytes != null && bytes.length >= 1) {
                return bytes;
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }
    /**
     * @return
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * 获取状态
     * @return
     */
    public boolean getStatus() {
        return status.get();
    }


    public void startAsThread() {
        new Thread(this).start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
    }
}
