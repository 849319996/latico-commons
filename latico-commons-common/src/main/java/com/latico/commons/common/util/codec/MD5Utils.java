package com.latico.commons.common.util.codec;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

import java.security.NoSuchAlgorithmException;

/**
 * <PRE>
 *  MD5工具类,支持使用默认编码格式的，指定编码格式的，区分结果大小写的
 *  
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2019-06-24 00:26:59
 * @Version: 1.0
 */
public class MD5Utils {
    /** LOG 日志工具 */
    private static final Logger LOG = LoggerFactory.getLogger(MD5Utils.class);
    /**
     * hexDigitsLowerCase 用来将字节转换成十六进制表示的字符,小写形式
     */
    private static final char hexDigitsLowerCase[] = {'0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * hexDigitsUpperCase 用来将字节转换成十六进制表示的字符,大写形式
     */
    private static final char hexDigitsUpperCase[] = {'0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'};

    private static final String UTF8 = "UTF-8";

    private static final String GBK = "GBK";

    private static final String MD5 = "MD5";

    public static String toLowerCaseMd5(byte[] source) {
        try {
            return getMD5(source, hexDigitsLowerCase);
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    /**
     * 使用默认编码(UTF-8)的小写形式MD5
     *
     * @param source 源字符串
     * @return MD5值的16进制表达形式字符串
     */
    public static String toLowerCaseMd5(String source) {
        return getMD5(source, UTF8, hexDigitsLowerCase);
    }

    /**
     * 指定编码的小写形式MD5
     *
     * @param source  源字符串
     * @param charset 字符集
     * @return MD5值的16进制表达形式字符串
     */
    public static String toLowerCaseMd5(String source, String charset) {
        return getMD5(source, charset, hexDigitsLowerCase);
    }

    /**
     * 使用默认编码(UTF-8)的大写形式MD5
     *
     * @param source 源字符串
     * @return MD5值的16进制表达形式字符串
     */
    public static String toUpperCaseMd5(String source) {
        return getMD5(source, UTF8, hexDigitsUpperCase);
    }

    /**
     * 指定编码的大写形式MD5
     *
     * @param source  源字符串
     * @param charset 字符集
     * @return MD5值的16进制表达形式字符串
     */
    public static String toUpperCaseMd5(String source, String charset) {
        return getMD5(source, charset, hexDigitsUpperCase);
    }

    /**
     * 使用系统UTF-8编码的小写形式MD5
     *
     * @param source 源字符串
     * @return MD5值的16进制表达形式字符串
     */
    public static String toLowerCaseMd5ByUTF8(String source) {
        return getMD5(source, UTF8, hexDigitsLowerCase);
    }

    /**
     * 使用系统UTF-8编码的大写形式MD5
     *
     * @param source 源字符串
     * @return MD5值的16进制表达形式字符串
     */
    public static String toUpperCaseMd5ByUTF8(String source) {
        return getMD5(source, UTF8, hexDigitsUpperCase);
    }

    /**
     * 使用系统GBK编码的小写形式MD5
     *
     * @param source 源字符串
     * @return MD5值的16进制表达形式字符串
     */
    public static String toLowerCaseMd5ByGBK(String source) {
        return getMD5(source, GBK, hexDigitsLowerCase);
    }

    /**
     * 使用系统GBK编码的大写形式MD5
     *
     * @param source 源字符串
     * @return MD5值的16进制表达形式字符串
     */
    public static String toUpperCaseMd5ByGBK(String source) {
        return getMD5(source, GBK, hexDigitsUpperCase);
    }

    /**
     * 获取字节数组形式的MD5的字节数组形式的值
     * MD5 的计算结果是一个128位的长整数, 用字节表示就是16 个字节
     * 默认使用UTF-8字符集
     * @param source 源字符串
     * @return MD5值字节表达形式
     */
    public static byte[] getByteMD5(String source) {
        return getByteMD5(source, UTF8);
    }

    /**
     * 获取字节数组形式的MD5的字节数组形式的值
     * MD5 的计算结果是一个128位的长整数, 用字节表示就是16 个字节
     *
     * @param source 源字符串
     * @param charset 指定字符集
     * @return MD5值字节表达形式
     */
    public static byte[] getByteMD5(String source, String charset) {
        byte[] valueMD5 = null;
        if (source != null) {
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance(MD5);
                md.update(source.getBytes(charset));
                valueMD5 = md.digest();
            } catch (Exception e) {
                LOG.error(e);
            }

        }
        return valueMD5;
    }

    /**
     * 获取MD5字符串
     *
     * @param source    源字符串
     * @param charset   编码格式
     * @param hexDigits 十六进制数组
     * @return MD5十六进制表达形式
     */
    private static String getMD5(String source, String charset, char[] hexDigits) {
        String md5Str = null;

        if (source != null) {
            try {
                md5Str = getMD5(source.getBytes(charset), hexDigits);
            } catch (Exception e) {
                e.printStackTrace(System.err);
                LOG.error("计算MD5异常", e);
            }
        }

        return md5Str;
    }

    /**
     * 计算并返回一个字节数组的MD5值;
     * 流程：
     *
     * @param source    源字符串
     * @param hexDigits 十六进制字符
     * @return MD5值的十六进制字符串
     * @throws NoSuchAlgorithmException
     */
    private static String getMD5(byte[] source, char[] hexDigits) throws NoSuchAlgorithmException {

        java.security.MessageDigest md = java.security.MessageDigest.getInstance(MD5);
        // MD5 的计算结果是一个128
        md.update(source);
        // 位的长整数，用字节表示就是16 个字节
        byte tmp[] = md.digest();

        // 每个字节用十六进制表示的话，使用两个字符，所以表示成十六进制需要32 个字符
        char str[] = new char[16 * 2];

        // 表示转换结果中对应的字符位置
        int k = 0;

        // 从第一个字节开始，将MD5的每一个字节, 转换成十六进制字符
        for (int i = 0; i < 16; i++) {

            // 取第i 个字节
            byte b = tmp[i];

            // 取字节中高4位的数字转换，>>>为逻辑右移，将符号位一起右移
            str[k++] = hexDigits[b >>> 4 & 0x0f];

            // 取字节中低4位的数字转换
            str[k++] = hexDigits[b & 0x0f];
        }
        // 将换后的结果转换为字符串
        return new String(str);
    }

}
