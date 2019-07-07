package com.latico.commons.common.util.codec;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * <PRE>
 *  Base64编解码工具
 *
 * </PRE>
 * @Author: latico
 * @Date: 2019-05-28 23:33:51
 * @Version: 1.0
 */
public class Base64Utils {

    /**
     * LOG 日志工具
     */
    private static final Logger LOG = LoggerFactory.getLogger(Base64Utils.class);
    /**
     * Base64编码器
     * sun自带
     */
    private final static BASE64Encoder ENCODER = new BASE64Encoder();

    /**
     * Base64解码器
     * sun自带
     */
    private final static BASE64Decoder DECODER = new BASE64Decoder();

    /**
     * 私有化构造函数
     */
    protected Base64Utils() {
    }

    /**
     * Base64编码
     * sun自带的
     *
     * @param str 原始字符串数据
     * @return Base64编码字符串
     */
    public static String encode(String str) {
        if (str == null) {
            return null;
        }
        try {
            return encode(str.getBytes());
        } catch (Exception e) {
            LOG.error("Base64加密失败，原字符串:" + str, e);
            return str;
        }
    }

    /**
     * Base64编码
     * sun自带的
     *
     * @param bytes 原始字节数据
     * @return Base64编码字符串
     */
    public static String encode(byte[] bytes) {
        try {
            return ENCODER.encodeBuffer(bytes).trim();
        } catch (Exception e) {
            String str = bytes == null ? null : new String(bytes);
            LOG.error("Base64加密失败，原字节:" + str, e);
            return str;
        }
    }

    /**
     * Base64解码
     * sun自带的
     *
     * @param base64 Base64编码字符串
     * @return 原始字节数据
     */
    public static byte[] decode(String base64) {
        if (base64 == null) {
            return null;
        }
        try {
            return DECODER.decodeBuffer(base64);
        } catch (Exception e) {
            LOG.error("Base64解密失败，原字符串:" + base64, e);
            return base64.getBytes();
        }
    }

    /**
     * 加密
     * apache包封装的方式
     *
     * @param str 字符串
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String encodeByApache(String str) {
        if (str == null) {
            return str;
        }
        return encodeByApache(str.getBytes());
    }

    /**
     * 加密
     * apache包封装的方式
     *
     * @param bytes 字节数据
     * @return
     * @throws Exception
     */
    public static String encodeByApache(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            byte[] enbytes = org.apache.commons.codec.binary.Base64.encodeBase64Chunked(bytes);
            return new String(enbytes);
        } catch (Exception e) {
            String str = new String(bytes);
            LOG.error("Base64加密失败，原字节:" + str, e);
            return str;
        }
    }

    /**
     * 解密
     * apache包封装的方式
     *
     * @param base64 密文
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static byte[] decodeByApache(String base64) {
        if (base64 == null) {
            return null;
        }
        try {
            return org.apache.commons.codec.binary.Base64.decodeBase64(new String(base64).getBytes());
        } catch (Exception e) {
            LOG.error("Base64解密失败，原字符串:" + base64, e);
            return base64.getBytes();
        }
    }
}
