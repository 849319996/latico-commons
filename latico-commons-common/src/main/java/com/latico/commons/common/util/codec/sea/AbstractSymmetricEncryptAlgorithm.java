package com.latico.commons.common.util.codec.sea;

import com.latico.commons.common.envm.CharsetType;

import javax.crypto.Cipher;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-06-03 14:08
 * @Version: 1.0
 */
public abstract class AbstractSymmetricEncryptAlgorithm implements SymmetricEncryptAlgorithm {

    protected static final String defaultSecretKey = "national";

    /**
     * 默认加密编码
     */
    protected final static String DEFAULT_CHARSET = CharsetType.UTF8;

    /**
     * 加密计算工具
     */
    protected Cipher encryptCipher;

    /**
     * 解密计算工具
     */
    protected Cipher decryptCipher;

    /**
     * 字符集
     */
    protected String charset;

    /**
     * 原字符秘钥
     */
    protected String secretKey;

    @Override
    public void init(String secretKey) throws Exception {
        if (secretKey == null) {
            init(defaultSecretKey, DEFAULT_CHARSET);
        } else {
            init(secretKey, DEFAULT_CHARSET);
        }
    }

    /**
     * 加密
     *
     * @param content       待加密内容
     * @return 加密后的密文 byte[]
     */
    @Override
    public byte[] encrypt(byte[] content) throws Exception {
        return encryptCipher.doFinal(content);
    }

    /**
     * 解密
     *
     * @param content       待解密内容
     * @return 解密后的明文 byte[]
     */
    @Override
    public byte[] decrypt(byte[] content) throws Exception {
        return decryptCipher.doFinal(content);
    }

}
