package com.latico.commons.snappy;

import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * <PRE>
 * 压缩/解压工具
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-08-26 17:51
 * @Version: 1.0
 */
public class SnappyUtils {

    /**
     * 压缩
     * @param bytes
     * @return
     * @throws IOException
     */
    public static byte[] compress(byte[] bytes) throws IOException {
        return Snappy.compress(bytes);
    }

    /**
     * 压缩
     * @param str
     * @return
     * @throws IOException
     */
    public static byte[] compress(String str) throws IOException {
        return Snappy.compress(str.getBytes("UTF-8"));
    }

    /**
     * 压缩
     * @param str
     * @param charset
     * @return
     * @throws IOException
     */
    public static byte[] compress(String str, String charset) throws IOException {
        return Snappy.compress(str.getBytes(charset));
    }

    /**
     * 解压
     * @param bytes
     * @return
     * @throws IOException
     */
    public static byte[] uncompress(byte[] bytes) throws IOException {
        return Snappy.uncompress(bytes);
    }

    /**
     * 解压
     * @param bytes
     * @return
     * @throws IOException
     */
    public static String uncompressToStr(byte[] bytes) throws IOException {
        return new String(Snappy.uncompress(bytes), "UTF-8");
    }

    /**
     * 解压
     * @param bytes
     * @param charset
     * @return
     * @throws IOException
     */
    public static String uncompressToStr(byte[] bytes, String charset) throws IOException {
        return new String(Snappy.uncompress(bytes), charset);
    }

}
