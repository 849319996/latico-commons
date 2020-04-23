package com.latico.commons.common.util.codec;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.util.string.StringUtils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * <PRE>
 * RSA算法工具类
 * </PRE>
 *
 * @author: latico
 * @date: 2019-06-03 21:33
 * @version: 1.0
 */
public class RSAUtils {
    /**
     * 默认加密编码
     */
    protected final static String DEFAULT_CHARSET = CharsetType.UTF8;
    /**
     * 算法类型
     */
    private static final String ALGORITHM_TYPE = "RSA";
    /**
     * 签名算法
     */
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥
     * @param secretKey
     * @param charset
     * @return
     * @throws Exception
     */
    public static String getPublicKeyBase64(String secretKey, String charset) throws Exception {
        KeyPair keyPair = createKeyPair(secretKey, charset);
        return getPublicKeyBase64(keyPair);
    }

    /**
     * 获取私钥
     * @param secretKey
     * @param charset
     * @return
     * @throws Exception
     */
    public static String getPrivateKeyBase64(String secretKey, String charset) throws Exception {
        KeyPair keyPair = createKeyPair(secretKey, charset);
        return getPrivateKeyBase64(keyPair);
    }

    /**
     * @param secretKey 秘钥，可选
     * @param charset 可选
     * @return 公钥和私钥密码对
     */
    public static KeyPair createKeyPair(String secretKey, String charset) throws Exception {
        SecureRandom secureRandom = null;
        if (StringUtils.isBlank(secretKey)) {
            secureRandom = new SecureRandom();
        } else {
            if (StringUtils.isBlank(charset)) {
                secureRandom = new SecureRandom(secretKey.getBytes());
            } else {
                secureRandom = new SecureRandom(secretKey.getBytes(charset));
            }
        }

        // 基于RSA算法初始化密钥对生成器, 密钥大小为96-1024位
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM_TYPE);
        keyPairGen.initialize(1024, secureRandom);
        // 生成密钥对
        KeyPair keyPair = keyPairGen.generateKeyPair();

        return keyPair;
    }

    /**
     * 获取公钥的Base64编码格式
     * @param keyPair
     * @return
     */
    private static String getPublicKeyBase64(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * 获取私钥的Base64编码格式
     * @param keyPair
     * @return
     */
    private static String getPrivateKeyBase64(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    /**
     * 将Base64编码后的公钥转换成 PublicKey 对象
     * 公钥使用X509协议
     *
     * @param publicKeyBase64
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String publicKeyBase64) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(publicKeyBase64);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_TYPE);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 将Base64编码后的私钥转换成 PrivateKey 对象
     * 使用PKCS8协议
     *
     * @param privateKeyBase64
     * @return PrivateKey
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String privateKeyBase64) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(privateKeyBase64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_TYPE);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    private static byte[] encrypt(byte[] data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    private static byte[] decrypt(byte[] data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    private static byte[] encrypt(byte[] data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    private static byte[] decrypt(byte[] data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    public static String sign(String data, String privateKeyBase64) throws Exception {
        return sign(data.getBytes(DEFAULT_CHARSET), privateKeyBase64);
    }
    /**
     * 对信息生成数字签名（用私钥）
     *
     * @param data             原数据
     * @param privateKeyBase64 私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKeyBase64) throws Exception {
        PrivateKey privateKey = getPrivateKey(privateKeyBase64);

        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data);

        return Base64Utils.encode(signature.sign());
    }

    public static boolean verifySign(String data, String publicKeyBase64, String sign) throws Exception {
        return verifySign(data.getBytes(DEFAULT_CHARSET), publicKeyBase64, sign);
    }
    /**
     * 校验数字签名（用公钥）
     *
     * @param data            原数据
     * @param publicKeyBase64 公钥
     * @param sign            数字签名
     * @return
     * @throws Exception
     */
    public static boolean verifySign(byte[] data, String publicKeyBase64, String sign)
            throws Exception {
        PublicKey publicKey = getPublicKey(publicKeyBase64);

        // 取公钥匙对象
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data);

        // 验证签名是否正常
        return signature.verify(Base64Utils.decode(sign));
    }

    public static byte[] encryptByPublicKey(byte[] data, String publicKeyBase64) throws Exception {
        PublicKey publicKey = getPublicKey(publicKeyBase64);
        return encrypt(data, publicKey);
    }

    public static byte[] encryptByPublicKey(String data, String publicKeyBase64) throws Exception {
        byte[] bytes = data.getBytes(DEFAULT_CHARSET);
        return encryptByPublicKey(bytes, publicKeyBase64);
    }

    public static String encryptByPublicKeyToStr(String data, String publicKeyBase64) throws Exception {
        byte[] bytes = encryptByPublicKey(data, publicKeyBase64);
        return Base64Utils.encode(bytes);
    }

    public static byte[] encryptByPrivateKey(byte[] data, String privateKeyBase64) throws Exception {
        PrivateKey privateKey = getPrivateKey(privateKeyBase64);
        return encrypt(data, privateKey);
    }

    public static byte[] encryptByPrivateKey(String data, String privateKeyBase64) throws Exception {
        byte[] bytes = data.getBytes(DEFAULT_CHARSET);
        return encryptByPrivateKey(bytes, privateKeyBase64);
    }

    public static String encryptByPrivateKeyToStr(String data, String privateKeyBase64) throws Exception {
        byte[] bytes = encryptByPrivateKey(data, privateKeyBase64);
        return Base64Utils.encode(bytes);
    }

    public static byte[] decryptByPublicKey(byte[] data, String publicKeyBase64) throws Exception {
        PublicKey publicKey = getPublicKey(publicKeyBase64);
        return decrypt(data, publicKey);
    }

    public static byte[] decryptByPublicKey(String data, String publicKeyBase64) throws Exception {
        byte[] bytes = Base64Utils.decode(data);
        return decryptByPublicKey(bytes, publicKeyBase64);
    }

    public static String decryptByPublicKeyToStr(String data, String publicKeyBase64) throws Exception {
        byte[] bytes = decryptByPublicKey(data, publicKeyBase64);
        return new String(bytes, DEFAULT_CHARSET);
    }

    public static byte[] decryptByPrivateKey(byte[] data, String privateKeyBase64) throws Exception {
        PrivateKey privateKey = getPrivateKey(privateKeyBase64);
        return decrypt(data, privateKey);
    }

    public static byte[] decryptByPrivateKey(String data, String privateKeyBase64) throws Exception {
        byte[] bytes = Base64Utils.decode(data);
        return decryptByPrivateKey(bytes, privateKeyBase64);
    }

    public static String decryptByPrivateKeyToStr(String data, String privateKeyBase64) throws Exception {
        byte[] bytes = decryptByPrivateKey(data, privateKeyBase64);
        return new String(bytes, DEFAULT_CHARSET);
    }

}
