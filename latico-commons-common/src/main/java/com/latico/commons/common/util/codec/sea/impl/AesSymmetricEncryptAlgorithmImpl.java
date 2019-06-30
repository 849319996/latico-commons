package com.latico.commons.common.util.codec.sea.impl;

import com.latico.commons.common.util.codec.Base64Utils;
import com.latico.commons.common.util.codec.sea.AbstractSymmetricEncryptAlgorithm;
import com.latico.commons.common.util.codec.sea.common.SymmetricEncryptAlgorithmType;
import com.latico.commons.common.util.codec.sea.common.SymmetricEncryptAlgorithmTypeAnnotation;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * <PRE>
 * AES对称加密算法实现
 * 高级加密标准(AES,Advanced Encryption Standard)为最常见的对称加密算法(微信小程序加密传输就是用这个加密算法的)。对称加密算法也就是加密和解密用相同的密钥。
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-06-03 14:14
 * @Version: 1.0
 */
@SymmetricEncryptAlgorithmTypeAnnotation(type = SymmetricEncryptAlgorithmType.AES)
public class AesSymmetricEncryptAlgorithmImpl extends AbstractSymmetricEncryptAlgorithm {
    /**
     * 算法类型
     */
    protected static final String ALGORITHM_TYPE = "AES";

    @Override
    public void init(String secretKey, String charset) throws Exception {
        if (charset == null) {
            this.charset = DEFAULT_CHARSET;
        }else{
            this.charset = charset;
        }
        if (secretKey == null) {
            this.secretKey = defaultSecretKey;
        } else {
            this.secretKey = secretKey;
        }

        String secretKeyAesStr = getStrKeyAES(this.secretKey, this.charset);
        SecretKey secretKeyAes = strKey2SecretKey(secretKeyAesStr);

//        生成加密工具
        this.encryptCipher = Cipher.getInstance(ALGORITHM_TYPE);
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeyAes);

        //        生成解密工具
        this.decryptCipher = Cipher.getInstance(ALGORITHM_TYPE);
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKeyAes);

    }

    @Override
    public byte[] encrypt(String content) throws Exception {
        byte[] bytes = content.getBytes(charset);
        return encrypt(bytes);
    }

    @Override
    public String encryptToStr(byte[] content) throws Exception {
        byte[] encrypt = encrypt(content);
        return Base64Utils.encode(encrypt);
    }

    @Override
    public String encryptToStr(String content) throws Exception {
        byte[] bytes = content.getBytes(charset);
        return encryptToStr(bytes);
    }

    @Override
    public byte[] decrypt(String content) throws Exception {
        byte[] cipherBytes = Base64Utils.decode(content);
        return decrypt(cipherBytes);
    }

    @Override
    public String decryptToStr(byte[] content) throws Exception {
        return new String(decrypt(content), charset);
    }

    @Override
    public String decryptToStr(String content) throws Exception {
        byte[] cipherBytes = Base64Utils.decode(content);
        return decryptToStr(cipherBytes);
    }

    /**
     * 获得一个 密钥长度为 256 位的 AES 密钥，
     *
     * @return 返回经 BASE64 处理之后的密钥字符串
     */
    private String getStrKeyAES(String key, String charset) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM_TYPE);
        SecureRandom secureRandom = new SecureRandom(key.getBytes(charset));
        // 这里可以是 128、192、256、越大越安全
        keyGen.init(128, secureRandom);
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * 将使用 Base64 加密后的字符串类型的 secretKey 转为 SecretKey
     *
     * @param strKey
     * @return SecretKey
     */
    private SecretKey strKey2SecretKey(String strKey) {
        byte[] bytes = Base64.getDecoder().decode(strKey);
        SecretKeySpec secretKey = new SecretKeySpec(bytes, ALGORITHM_TYPE);
        return secretKey;
    }

}
