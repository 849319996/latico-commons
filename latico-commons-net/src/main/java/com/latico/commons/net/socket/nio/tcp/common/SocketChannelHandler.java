package com.latico.commons.net.socket.nio.tcp.common;

import com.latico.commons.common.util.collections.ArrayUtils;
import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.reflect.ObjectUtils;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <PRE>
 *     假如是用线程方式启动，会不断的从输入流中读取数据进缓存队列中
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-20 17:22
 * @Version: 1.0
 */
public class SocketChannelHandler implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(SocketChannelHandler.class);
    private final Charset charsetObj;
    /**
     * 缓冲区大小，1KB
     */
    private int byteBuffSize = 1024;
    private final SocketChannel socketChannel;
    private int receiveQueueSize;
    private String charset = "UTF-8";
    private AtomicBoolean status = new AtomicBoolean(false);
    /**
     * 字节形式接收数据时使用的队列，假如是用线程形式启动，会一直坚挺输入流，把输入流字节数据存入次队列
     */
    private BlockingQueue<byte[]> receiveBytesQueue;

    /**
     * 是不是Socket关闭
     */
    private AtomicBoolean socketClosed = new AtomicBoolean(false);

    public SocketChannelHandler(SocketChannel socketChannel) {
        this(socketChannel, 10000, "UTF-8");
    }

    /**
     * @param socketChannel
     * @param receiveQueueSize 当使用线程方式启动时，接收的字节数组的个数，建议100-10000
     * @param charset
     */
    public SocketChannelHandler(SocketChannel socketChannel, int receiveQueueSize, String charset) {

        this.socketChannel = socketChannel;
        this.receiveQueueSize = receiveQueueSize;
        this.charset = charset;
        this.charsetObj = Charset.forName(charset);

        try {
            if (socketChannel.finishConnect()) {
                status.set(true);
            }
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    @Override
    public void run() {
        this.receiveBytesQueue = new ArrayBlockingQueue<>(this.receiveQueueSize);
        try {
            startReceiveByteListen();
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            close();
        }
        LOG.info("SocketChannelHandler线程结束");
    }

    /**
     * 是否有效
     *
     * @return
     */
    public boolean isValid() {
        return status.get() && !socketClosed.get() && socketChannel.isConnected() && socketChannel.isOpen();
    }

    public void startAsThread() {
        new Thread(this).start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
    }

    /**
     * 启动数据接收
     */
    private void startReceiveByteListen() throws Exception {

        final long sleepWhenNotData = 100;

        ByteBuffer buffer = ByteBuffer.allocate(byteBuffSize);
        while (isValid()) {
            buffer.clear();
            int readSize = socketChannel.read(buffer);

            if (readSize == -1) {
                LOG.debug("读取到流结束,请关闭连接");
                socketClosed.set(true);
                break;
            }
            //读取不到数据，下一轮监听
            if (readSize == 0) {
                //数据为空，睡眠
                Thread.sleep(sleepWhenNotData);
                continue;
            }
            LOG.info("本次读取流的大小:{}", readSize);
            byte[] array = buffer.array();
            if (readSize == byteBuffSize) {
                receiveBytesQueue.add(array);
            } else {
                //根据接收到的实际字节数截取子数组
                byte[] subarray = ArrayUtils.subarray(array, 0, readSize);
                receiveBytesQueue.add(subarray);
            }

        }
    }

    /**
     * 启动了线程方式才需要关闭，如果没有使用线程方式，不要关闭
     */
    public void close() {
        status.set(false);
        IOUtils.close(socketChannel);
        LOG.info("SocketChannelHandler关闭完成");
    }

    public boolean sendData(ByteBuffer byteBuffer) {
        if (!isValid()) {
            return false;
        }
        try {
            socketChannel.write(byteBuffer);
            return true;
        } catch (Exception e) {
            LOG.error(e);
        }
        return false;
    }

    public boolean sendData(byte[] bytes) {
        if (!isValid()) {
            return false;
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return sendData(byteBuffer);
    }

    public boolean sendData(String data) {
        if (!isValid()) {
            return false;
        }
        ByteBuffer byteBuffer = charsetObj.encode(data);
        return sendData(byteBuffer);
    }

    public boolean sendData(Object data) {
        if (!isValid()) {
            return false;
        }
        try {
            byte[] bytes = ObjectUtils.toBytes(data);
            return sendData(bytes);
        } catch (Exception e) {
            LOG.error(e);
        }

        return false;
    }

    /**
     * 无限等待获取一个对象
     *
     * @return
     */
    public byte[] getAndRemoveReceiveDataBytes() {
        try {
            while (true) {
                if (!isValid()) {
                    LOG.error("连接已断开");
                    break;
                }
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
     *
     * @return
     */
    public byte[] getAndRemoveReceiveDataBytes(long timeout) {
        if (isValid()) {
            try {
                return receiveBytesQueue.poll(timeout, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                LOG.error(e);
            }
        }
        return null;
    }


    /**
     * 无限等待从接收队列中获取当前所有的字节数据
     *
     * @return
     */
    public byte[] getAndRemoveCurrentAllReceiveDataByte() {

        byte[] bytes = new byte[]{};
        long waitTimeWhenHasData = 100;
        try {
            byte[] bytesTmp = null;

            if (isValid()) {
                bytesTmp = receiveBytesQueue.poll(1, TimeUnit.SECONDS);
            }
            do {
                if (bytesTmp == null) {
                    if (bytes.length >= 1) {
                        break;
                    }
                } else {
                    if (bytesTmp.length >= 1) {
                        bytes = ArrayUtils.addAll(bytes, bytesTmp);
                    }
                }

                if (!isValid()) {
                    LOG.error("已关闭，结束读取");
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
     *
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
     *
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

    public Object getAndRemoveReceiveDataObject() {
        byte[] bytes = getAndRemoveCurrentAllReceiveDataByte();
        try {
            return ObjectUtils.toObject(bytes);
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    public Object getAndRemoveReceiveDataObject(long timeout) {
        byte[] bytes = getAndRemoveCurrentAllReceiveDataByte(timeout);
        try {
            return ObjectUtils.toObject(bytes);
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    /**
     * 无限等待获取一个对象
     *
     * @return
     */
    public byte[] getAndRemoveCurrentAllReceiveDataByte(long timeout) {
        if (!isValid()) {
            return null;
        }
        long waitTimeWhenHasData = 100;
        try {
            byte[] bytes = receiveBytesQueue.poll(timeout, TimeUnit.MILLISECONDS);
            if (bytes == null) {
                return null;
            }

            while (receiveBytesQueue.size() >= 1) {
                byte[] bytesTmp = receiveBytesQueue.poll(waitTimeWhenHasData, TimeUnit.MILLISECONDS);
                if (bytesTmp == null) {
                    break;
                } else {
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
     * 读取缓冲区现有的所有字节，并转化成字符串
     *
     * @return
     * @throws Exception
     */
    public String readStrings() throws Exception {
        byte[] bytes = readByteDatas();
        return new String(bytes, charset);
    }

    /**
     * 读取缓冲区现有的所有字节，然后转化成一个对象
     *
     * @return
     * @throws Exception
     */
    public Object readObject() throws Exception {
        byte[] bytes = readByteDatas();
        try {
            return ObjectUtils.toObject(bytes);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 读取缓冲区现有的所有字节
     *
     * @return
     * @throws Exception
     */
    public byte[] readByteDatas() throws Exception {
        byte[] result = new byte[0];

        ByteBuffer buffer = ByteBuffer.allocate(byteBuffSize);
        try {
            while (isValid()) {
                buffer.clear();
                int readSize = socketChannel.read(buffer);

                if (readSize == -1) {
                    LOG.info("读取到流结束,请关闭连接");
                    socketClosed.set(true);
                    break;
                }
                //读取不到数据
                if (readSize == 0) {
                    break;
                }
                LOG.info("本次读取流的大小:{}", readSize);
                byte[] array = buffer.array();

                if (readSize < byteBuffSize) {
                    //根据接收到的实际字节数截取子数组
                    array = ArrayUtils.subarray(array, 0, readSize);
                }
                result = ArrayUtils.addAll(result, array);

            }

        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    /**
     * 判断Socket流是否已经断开关闭
     *
     * @return
     */
    public boolean isSocketClosed() {
        return socketClosed.get();
    }

    public int getByteBuffSize() {
        return byteBuffSize;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public int getReceiveQueueSize() {
        return receiveQueueSize;
    }

    public String getCharset() {
        return charset;
    }

    public boolean getStatus() {
        return status.get();
    }

    public BlockingQueue<byte[]> getReceiveBytesQueue() {
        return receiveBytesQueue;
    }

}
