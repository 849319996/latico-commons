package com.latico.commons.common.util.io;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <PRE>
 *  目前不建议使用， 因为AIO的实施需充分调用OS参与，
 *  IO需要操作系统支持、并发也同样需要操作系统的支持，所以性能方面不同操作系统差异会比较明显。
 *
 * 同步阻塞：你到饭馆点餐，然后在那等着，还要一边喊：好了没啊！
 * 同步非阻塞：在饭馆点完餐，就去遛狗了。不过溜一会儿，就回饭馆喊一声：好了没啊！
 * 异步阻塞：遛狗的时候，接到饭馆电话，说饭做好了，让您亲自去拿。
 * 异步非阻塞：饭馆打电话说，我们知道您的位置，一会给你送过来，安心遛狗就可以了。
 *
 * Reactor与Proactor
 * 两种IO多路复用方案:Reactor and Proactor。
 * Reactor模式是基于同步I/O的，而Proactor模式是和异步I/O相关的。
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-02-05 19:55
 * @Version: 1.0
 */
public class AIOUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AIOUtils.class);

    /**
     * future方式
     * @param file
     * @param charset
     * @return
     * @throws Exception
     */
    public static String readToStringByFuture(String file, String charset) throws Exception {
        AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get(file), StandardOpenOption.READ);
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        StringBuilder sb = new StringBuilder();
        long position = 0;
        try {
            while (true) {
                buffer.clear();
                Future<Integer> future = channel.read(buffer, position);
                while(!future.isDone()) {
                    Thread.sleep(50);
                }
                Integer size = future.get();

                if (size <= 0) {
                    break;
                }
                byte[] bytes = buffer.array();
                sb.append(new String(bytes, 0, size, charset));
                position += size;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.close(channel);
        }

        return sb.toString();
    }

    /**
     * 异步回调方式
     * @param file
     * @param charset
     * @return
     * @throws Exception
     */
    public static String readToStringByCompletionHandler(String file, String charset) throws Exception {

        AsynchronousFileChannel fileChannel =
                AsynchronousFileChannel.open(Paths.get(file), StandardOpenOption.READ);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        StringBuilder sb = new StringBuilder();
        AtomicLong position = new AtomicLong(0);
        AtomicBoolean isFalse = new AtomicBoolean(false);
        AtomicBoolean isFinish = new AtomicBoolean(false);
        AtomicBoolean currentIsFinish = new AtomicBoolean(true);

        //创建异步回调
        CompletionHandler<Integer, ByteBuffer> completionHandler = new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                position.addAndGet(result);
                if (result <= 0) {
                    isFinish.set(true);
                    return;
                }

                byte[] array = buffer.array();

                try {
                    sb.append(new String(array, 0, result, charset));
                } catch (Exception e) {
                    LOG.error(e);

                }
                buffer.clear();
                currentIsFinish.set(true);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer buffer) {
                LOG.error(exc);
                isFalse.set(true);

                int limit = buffer.limit();
                position.addAndGet(limit);
                if (limit <= 0) {
                    isFinish.set(true);
                    return;
                }
                byte[] array = buffer.array();

                try {
                    sb.append(new String(array, 0, limit, charset));
                } catch (Exception e) {
                    LOG.error(e);

                }
                buffer.clear();
            }
        };

        while (!isFinish.get() && !isFalse.get()) {
            if (currentIsFinish.get()) {
                currentIsFinish.set(false);
                fileChannel.read(buffer, position.get(), buffer, completionHandler);
            } else {
                Thread.sleep(50);
            }

        }

        return sb.toString();
    }

    /**
     * future方式
     * @param file
     * @param data
     * @param charset
     * @throws Exception
     */
    public static void writeStringByFuture(String file, String data, String charset) throws Exception {
        byte[] dataBytes = data.getBytes(charset);
        FileUtils.createFile(file);
        AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get(file), StandardOpenOption.WRITE);
        ByteBuffer buffer = ByteBuffer.wrap(dataBytes);
        try {

            Future<Integer> future = channel.write(buffer, 0);
            while(!future.isDone()) {
                Thread.sleep(50);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.close(channel);
        }
    }

    /**
     * 异步回调方式
     * @param file
     * @param data
     * @param charset
     * @throws Exception
     */
    public static void writeStringByCompletionHandler(String file, String data, String charset) throws Exception {
        byte[] dataBytes = data.getBytes(charset);
        FileUtils.createFile(file);
        ByteBuffer buffer = ByteBuffer.wrap(dataBytes);
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(Paths.get(file), StandardOpenOption.WRITE);
        AtomicBoolean isFalse = new AtomicBoolean(false);
        AtomicBoolean isFinish = new AtomicBoolean(false);

        //创建异步回调
        CompletionHandler<Integer, ByteBuffer> completionHandler = new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                isFinish.set(true);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer buffer) {
                LOG.error(exc);
                isFalse.set(true);
                isFinish.set(true);
            }
        };

        fileChannel.write(buffer, 0, buffer, completionHandler);
        while (!isFinish.get() && !isFalse.get()) {
            Thread.sleep(50);
        }

    }
}
