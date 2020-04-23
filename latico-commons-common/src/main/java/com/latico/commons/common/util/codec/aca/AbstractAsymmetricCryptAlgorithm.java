package com.latico.commons.common.util.codec.aca;

import com.latico.commons.common.envm.CharsetType;

import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-06-03 16:24
 * @version: 1.0
 */
public abstract class AbstractAsymmetricCryptAlgorithm implements AsymmetricCryptAlgorithm {

    protected static final String defaultSecretKey = "national";

    /**
     * 默认加密编码
     */
    protected final static String DEFAULT_CHARSET = CharsetType.UTF8;

    /**
     * 字符集
     */
    protected String charset;

    /**
     * 原字符秘钥
     */
    protected String secretKey;

    protected PublicKey publicKey;
    protected PrivateKey privateKey;
    /**
     * 公钥经过Base64编码后的字符串
     */
    protected String publicKeyBase64;
    /**
     * 私钥经过Base64编码后的字符串
     */
    protected String privateKeyBase64;

    /**
     * 公钥加密计算工具
     */
    protected Cipher publicEncryptCipher;

    /**
     * 公钥解密计算工具
     */
    protected Cipher publicDecryptCipher;

    /**
     * 私钥加密计算工具
     */
    protected Cipher privateEncryptCipher;

    /**
     * 私钥解密计算工具
     */
    protected Cipher privateDecryptCipher;

    @Override
    public void init(String secretKey) throws Exception {
        if (secretKey == null) {
            init(defaultSecretKey, DEFAULT_CHARSET);
        } else {
            init(secretKey, DEFAULT_CHARSET);
        }
    }

    @Override
    public String getPublicKeyBase64() {
        return publicKeyBase64;
    }

    @Override
    public String getPrivateKeyBase64() {
        return privateKeyBase64;
    }

    @Override
    public byte[] encryptByPublicKey(byte[] content) throws Exception {
        return publicEncryptCipher.doFinal(content);
    }

    @Override
    public byte[] encryptByPublicKey(String content) throws Exception {
        byte[] bytes = content.getBytes(charset);
        return encryptByPublicKey(bytes);
    }

    @Override
    public byte[] encryptByPrivateKey(byte[] content) throws Exception {
        return privateEncryptCipher.doFinal(content);
    }

    @Override
    public byte[] encryptByPrivateKey(String content) throws Exception {
        byte[] bytes = content.getBytes(charset);
        return encryptByPrivateKey(bytes);
    }

    @Override
    public byte[] decryptByPublicKey(byte[] content) throws Exception {
        return publicDecryptCipher.doFinal(content);
    }

    @Override
    public byte[] decryptByPrivateKey(byte[] content) throws Exception {
        return privateDecryptCipher.doFinal(content);
    }
}
