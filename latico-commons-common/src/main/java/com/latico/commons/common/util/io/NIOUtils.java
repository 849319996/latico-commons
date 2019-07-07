package com.latico.commons.common.util.io;

import com.latico.commons.common.envm.CharsetType;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * <PRE>
 * NIO工具类
 * <p>
 * NIO主要有三大核心部分：Channel(通道)，Buffer(缓冲区), Selector。传统IO基于字节流和字符流进行操作，而NIO基于Channel和Buffer(缓冲区)进行操作，数据总是从通道读取到缓冲区中，或者从缓冲区写入到通道中。Selector(选择区)用于监听多个通道的事件（比如：连接打开，数据到达）。因此，单个线程可以监听多个数据通道。
 * <p>
 * NIO和传统IO（一下简称IO）之间第一个最大的区别是，IO是面向流的，NIO是面向缓冲区的。 Java IO面向流意味着每次从流中读一个或多个字节，直至读取所有字节，它们没有被缓存在任何地方。此外，它不能前后移动流中的数据。如果需要前后移动从流中读取的数据，需要先将它缓存到一个缓冲区。NIO的缓冲导向方法略有不同。数据读取到一个它稍后处理的缓冲区，需要时可在缓冲区中前后移动。这就增加了处理过程中的灵活性。但是，还需要检查是否该缓冲区中包含所有您需要处理的数据。而且，需确保当更多的数据读入缓冲区时，不要覆盖缓冲区里尚未处理的数据。
 * <p>
 * IO的各种流是阻塞的。这意味着，当一个线程调用read() 或 write()时，该线程被阻塞，直到有一些数据被读取，或数据完全写入。该线程在此期间不能再干任何事情了。 NIO的非阻塞模式，使一个线程从某通道发送请求读取数据，但是它仅能得到目前可用的数据，如果目前没有数据可用时，就什么都不会获取。而不是保持线程阻塞，所以直至数据变得可以读取之前，该线程可以继续做其他的事情。 非阻塞写也是如此。一个线程请求写入一些数据到某通道，但不需要等待它完全写入，这个线程同时可以去做别的事情。 线程通常将非阻塞IO的空闲时间用于在其它通道上执行IO操作，所以一个单独的线程现在可以管理多个输入和输出通道（channel）。
 * <p>
 * Channel
 * 首先说一下Channel，国内大多翻译成“通道”。Channel和IO中的Stream(流)是差不多一个等级的。只不过Stream是单向的，譬如：InputStream, OutputStream.而Channel是双向的，既可以用来进行读操作，又可以用来进行写操作。
 * NIO中的Channel的主要实现有：
 * <p>
 * FileChannel
 * <p>
 * DatagramChannel
 * <p>
 * SocketChannel
 * <p>
 * ServerSocketChannel
 * <p>
 * 这里看名字就可以猜出个所以然来：分别可以对应文件IO、UDP和TCP（Server和Client）。下面演示的案例基本上就是围绕这4个类型的Channel进行陈述的。
 * <p>
 * Buffer
 * NIO中的关键Buffer实现有：ByteBuffer, CharBuffer, DoubleBuffer, FloatBuffer, IntBuffer, LongBuffer, ShortBuffer，分别对应基本数据类型: byte, char, double, float, int, long, short。当然NIO中还有MappedByteBuffer, HeapByteBuffer, DirectByteBuffer等这里先不进行陈述。
 * <p>
 * Selector
 * Selector运行单线程处理多个Channel，如果你的应用打开了多个通道，但每个连接的流量都很低，使用Selector就会很方便。例如在一个聊天服务器中。要使用Selector, 得向Selector注册Channel，然后调用它的select()方法。这个方法会一直阻塞到某个注册的通道有事件就绪。一旦这个方法返回，线程就可以处理这些事件，事件的例子有如新的连接进来、数据接收等。
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-20 13:46
 * @Version: 1.0
 */
public class NIOUtils {

    public static String readFileToString(String path) throws Exception {
        return readFileToString(path, CharsetType.UTF8);
    }

    /**
     * 只读模式
     * @param path
     * @param charset
     * @return
     * @throws Exception
     */
    public static String readFileToString(String path, String charset) throws Exception {
        StringBuilder sb = new StringBuilder();
        FileInputStream fileInputStream = null;
        FileChannel channel = null;
        try {
            fileInputStream = new FileInputStream(path);

            //创建channel
            channel = fileInputStream.getChannel();

            //分配空间，创建缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(512);

            while (true) {
                int readSize = channel.read(byteBuffer);
                if (readSize == -1) {
                    break;
                }
                /* 注意，读取后，将位置置为0，将limit置为容量, 以备下次读入到字节缓冲中，从0开始存储   */
                byteBuffer.clear();

                //从缓冲区拿到数据
                byte[] bytes = byteBuffer.array();

                sb.append(new String(bytes, 0, readSize, charset));

            }
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.close(channel);
            IOUtils.close(fileInputStream);
        }

        return sb.toString();

    }

    public static boolean writeFile(String path, String data) throws Exception {
        return writeFile(path, data, CharsetType.UTF8, false);
    }


    public static boolean writeFile(String path, String data, boolean append) throws Exception {
        return writeFile(path, data, CharsetType.UTF8, append);
    }

    public static boolean writeFile(String path, String data, String charset, boolean append) throws Exception {
        FileUtils.createFile(path);

        FileOutputStream fos = null;
        FileChannel channel = null;
        try {
            fos = new FileOutputStream(path, append);
            channel = fos.getChannel();
            ByteBuffer byteBuffer = Charset.forName(charset).encode(data);

            int len = 0;
            while ((len = channel.write(byteBuffer)) >= 1) {
                //写入len字节
            }

            return true;

        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.close(channel);
            IOUtils.close(fos);
        }

    }


    /**
     * @param srcFile
     * @param targetFile
     * @param append
     * @return
     * @throws Exception
     */
    public static boolean copyFile(String srcFile, String targetFile, boolean append) throws Exception {
        FileInputStream fin = null;
        FileOutputStream fos = null;
        FileChannel finChannel = null;
        FileChannel fosChannel = null;

        try {
            FileUtils.createFile(targetFile);
            fin = new FileInputStream(srcFile);
            fos = new FileOutputStream(targetFile, append);

            finChannel = fin.getChannel();
            fosChannel = fos.getChannel();

            //创建缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(512);

            while (finChannel.read(byteBuffer) != -1){
                //将当前位置置为limit，然后设置当前位置为0，也就是从0到limit这块，都写入到同道中
                //当前位置置为限制，然后将当前位置置为0，目的是将有数据部分的字节，由缓冲写入到通道中。通常用在读与写之间。
                byteBuffer.flip();

                //把缓冲区数据写入目标文件
                while (fosChannel.write(byteBuffer) >= 1) {
                }

                //将当前位置置为0，然后设置limit为容量，也就是从0到limit（容量）这块，
                // 都可以利用，通道读取的数据存储到0到limit这块
                byteBuffer.clear();
            }

            finChannel.close();
            return true;
        } catch (Exception e) {
            throw e;
        }finally {

            IOUtils.close(fosChannel);
            IOUtils.close(finChannel);
            IOUtils.close(fin);
            IOUtils.close(fos);

        }
    }

    /**
     * 创建MappedByteBuffer，这个适合大文件读取
     * @param filePath
     * @return
     * @throws Exception
     */
    public static MappedByteBuffer createMappedByteBufferReadOnly(String filePath) throws Exception {
        RandomAccessFile file = null;
        FileChannel fileChannel = null;
        try {
            file = new RandomAccessFile(filePath,"rw");
            fileChannel = file.getChannel();

            return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        } catch (Exception e) {
            throw e;
        }
    }

}
