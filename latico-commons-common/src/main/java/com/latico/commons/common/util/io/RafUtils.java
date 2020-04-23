package com.latico.commons.common.util.io;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <PRE>
 * 随机访问文件工具类
 * 对的RandomAccessFile操作
 * <p>
 * r：以只读方式打开指定文件。如果试图对该RandomAccessFile指定的文件执行写入方法则会抛出IOException
 * rw：以读取、写入方式打开指定文件。如果该文件不存在，则尝试创建文件
 * rws：以读取、写入方式打开指定文件。相对于rw模式，还要求对文件的内容或元数据的每个更新都同步写入到底层存储设备，
 * 默认情形下(rw模式下),是使用buffer的,只有cache满的或者使用RandomAccessFile.close()关闭流的时候儿才真正的写到文件
 * rwd：与rws类似，只是仅对文件的内容同步更新到磁盘，而不修改文件的元数据
 * </PRE>
 *
 * @author: latico
 * @date: 2020-01-08 14:16
 * @version: 1.0
 */
public class RafUtils {
    private static final Logger LOG = LoggerFactory.getLogger(RafUtils.class);
    /**
     * r：以只读方式打开指定文件。如果试图对该RandomAccessFile指定的文件执行写入方法则会抛出IOException
     */
    public static final String MODE_R = "r";
    /**
     * rw：以读取、写入方式打开指定文件。如果该文件不存在，则尝试创建文件
     */
    public static final String MODE_RW = "rw";
    /**
     * rws：以读取、写入方式打开指定文件。相对于rw模式，还要求对文件的内容或元数据的每个更新都同步写入到底层存储设备，
     * 默认情形下(rw模式下),是使用buffer的,只有cache满的或者使用RandomAccessFile.close()关闭流的时候儿才真正的写到文件
     */
    public static final String MODE_RWS = "rws";
    /**
     * rwd：与rws类似，只是仅对文件的内容同步更新到磁盘，而不修改文件的元数据
     */
    public static final String MODE_RWD = "rwd";

    /**
     * 只读方式打开文件
     *
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
    public static RandomAccessFile openRafByReadOnly(String fileName) throws FileNotFoundException {
        return new RandomAccessFile(fileName, MODE_R);
    }

    /**
     * 只读方式打开文件
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static RandomAccessFile openRafByReadOnly(File file) throws FileNotFoundException {
        return new RandomAccessFile(file, MODE_R);
    }

    /**
     * 读写方式打开文件，数据会放到缓冲区中，如果调用了close方法才会完全把缓冲区刷进文件
     *
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
    public static RandomAccessFile openRafByReadWrite(String fileName) throws FileNotFoundException {
        return new RandomAccessFile(fileName, MODE_RW);
    }

    /**
     * 读写方式打开文件，数据会放到缓冲区中，如果调用了close方法才会完全把缓冲区刷进文件
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static RandomAccessFile openRafByReadWrite(File file) throws FileNotFoundException {
        return new RandomAccessFile(file, MODE_RW);
    }

    /**
     * 同步(不在缓冲区停留)读写方式打开文件,支持对文件的元数据进行写入
     *
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
    public static RandomAccessFile openRafByReadWriteSync(String fileName) throws FileNotFoundException {
        return new RandomAccessFile(fileName, MODE_RWS);
    }

    /**
     * 同步(不在缓冲区停留)读写方式打开文件,支持对文件的元数据进行写入
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static RandomAccessFile openRafByReadWriteSync(File file) throws FileNotFoundException {
        return new RandomAccessFile(file, MODE_RWS);
    }

    /**
     * 同步(不在缓冲区停留)读写方式打开文件,不支持对文件的元数据进行写入，比rws需要更少的IO操作
     *
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
    public static RandomAccessFile openRafByReadWriteSyncDecrease(String fileName) throws FileNotFoundException {
        return new RandomAccessFile(fileName, MODE_RWD);
    }

    /**
     * 同步(不在缓冲区停留)读写方式打开文件,不支持对文件的元数据进行写入，比rws需要更少的IO操作
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static RandomAccessFile openRafByReadWriteSyncDecrease(File file) throws FileNotFoundException {
        return new RandomAccessFile(file, MODE_RWD);
    }


    /**
     * 读取行
     *
     * @param file
     * @param position 指定起始位置
     * @param lineSize 行的数量,可以小于等于0，这时尽最大能力读取
     * @return
     * @throws IOException
     */
    public static List<String> readLines(String file, long position, int lineSize) throws IOException {

        List<String> lines = new ArrayList<>();
        RandomAccessFile raf = null;
        try {
            raf = RafUtils.openRafByReadOnly(file);
            if (position >= 1) {
                raf.seek(position);
            }
            String line = null;
            while ((line = raf.readLine()) != null) {
                lines.add(line);

                //行数满足的话
                if (lineSize >= 1 && lines.size() >= lineSize) {
                    break;
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.close(raf);
        }
        return lines;
    }


    /**
     * 指定位置插入数据
     *
     * @param fileName 要插入的文件
     * @param position 插入的位置
     * @param data     插入的信息
     */
    public static boolean insertData(String fileName, long position, String data) throws Exception {
        File file = new File(fileName);
        return insertData(file, position, data);
    }

    /**
     * 指定位置插入数据
     * 因为文件块是连续的，在指定位置插入数据会导致后面的数据被覆盖，
     * 所以需要先把后面的数据读取到临时文件，然后再进行插入，
     * 所以为什么mysql的数据文件是顺序写，除非数据被删除，才会使用随机写吧
     *
     * @param file     要插入的文件
     * @param position 插入的位置
     * @param data     插入的信息
     */
    public static boolean insertData(File file, long position, String data) throws Exception {

        //使用临时文件保存插入点后的数据
        FileOutputStream tmpOut = null;
        FileInputStream tmpIn = null;
        RandomAccessFile raf = null;
        try {
            if (!file.exists()) {
                return false;
            }

            raf = openRafByReadWrite(file);

            //创建一个临时文件
            File tempFile = File.createTempFile(file.getName(), null);

            //使用临时文件保存插入点后的数据
            tmpOut = new FileOutputStream(tempFile);
            tmpIn = new FileInputStream(tempFile);

            //设置插入点
            raf.seek(position);

            //开始保存插入点后的数据
            byte[] bytes = new byte[512];

            //实际存入数据的字节数
            int len = 0;
            while ((len = raf.read(bytes)) >= 1) {
                //将读取的内容保存到临时文件
                tmpOut.write(bytes, 0, len);
            }

            //开始进行数据插入
            raf.seek(position);
            raf.write(data.getBytes());

            //将临时文件的内容重新放入磁盘文件内
            while ((len = tmpIn.read(bytes)) >= 1) {
                raf.write(bytes, 0, len);
            }
            return true;
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.close(raf);
            IOUtils.close(tmpIn);
            IOUtils.close(tmpOut);
        }
    }

    /**
     * 读取字节
     *
     * @param fileName 要读取的文件
     * @param position 读取的开始位置
     * @param byteSize 读取的字节数，允许大于文件大小或者小于等于0，此时会尽最大读取
     * @return
     * @throws Exception
     */
    public static byte[] readBytes(String fileName, long position, int byteSize) throws Exception {
        File file = new File(fileName);
        return readBytes(file, position, byteSize);
    }

    /**
     * 读取字节
     *
     * @param file     要读取的文件
     * @param position 读取的开始位置
     * @param byteSize 读取的字节数，允许大于文件大小或者小于等于0，此时会尽最大读取
     * @return
     * @throws Exception
     */
    public static byte[] readBytes(File file, long position, int byteSize) throws IOException {

        RandomAccessFile raf = null;
        try {
            if (!file.exists()) {
                return null;
            }

            raf = openRafByReadWrite(file);

            long length = raf.length();
            //允许大于文件大小或者小于等于0，此时会尽最大读取
            if (byteSize <= 0 || length < byteSize) {
                byteSize = (int) (length - position);
            }

            //设置插入点
            raf.seek(position);

            //开始保存插入点后的数据
            byte[] bytes = new byte[byteSize];
            raf.read(bytes);

            //开始进行数据插入
            return bytes;
        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.close(raf);
        }
    }
}
