package com.latico.commons.common.util.codec.aca.impl;

import com.latico.commons.common.util.codec.Base64Utils;
import com.latico.commons.common.util.codec.RSAUtils;
import com.latico.commons.common.util.codec.aca.AbstractAsymmetricCryptAlgorithm;
import com.latico.commons.common.util.codec.aca.common.AsymmetricCryptAlgorithmType;
import com.latico.commons.common.util.codec.aca.common.AsymmetricCryptAlgorithmTypeAnnotation;
import com.latico.commons.common.util.string.StringUtils;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-06-03 15:48
 * @Version: 1.0
 */
@AsymmetricCryptAlgorithmTypeAnnotation(type = AsymmetricCryptAlgorithmType.RSA)
public class RsaAsymmetricCryptAlgorithmImpl extends AbstractAsymmetricCryptAlgorithm {
    /**
     * 算法类型
     */
    protected static final String ALGORITHM_TYPE = "RSA";
    /**
     * 约定签名算法
     */
//    protected static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

    @Override
    public void init() throws Exception {
        this.charset = DEFAULT_CHARSET;
        this.secretKey = defaultSecretKey;

        KeyPair keyPair = RSAUtils.createKeyPair(null, this.charset);
        initParam(keyPair);
    }

    @Override
    public void init(String secretKey, String charset) throws Exception {

        if (charset == null) {
            this.charset = DEFAULT_CHARSET;
        } else {
            this.charset = charset;
        }
        if (secretKey == null) {
            this.secretKey = defaultSecretKey;
        } else {
            this.secretKey = secretKey;
        }
        KeyPair keyPair = RSAUtils.createKeyPair(this.secretKey, this.charset);
        initParam(keyPair);
    }

    @Override
    public void initByPublicAndPrivateKey(String publicKeyBase64, String privateKeyBase64) throws Exception {
        this.charset = DEFAULT_CHARSET;
        this.secretKey = defaultSecretKey;

        if (StringUtils.isNotBlank(publicKeyBase64)) {
            this.publicKeyBase64 = publicKeyBase64;
            this.publicKey = RSAUtils.getPublicKey(publicKeyBase64);
            this.publicEncryptCipher = Cipher.getInstance(ALGORITHM_TYPE);
            this.publicEncryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            this.publicDecryptCipher = Cipher.getInstance(ALGORITHM_TYPE);
            this.publicDecryptCipher.init(Cipher.DECRYPT_MODE, publicKey);
        }

        if (StringUtils.isNotBlank(privateKeyBase64)) {
            this.privateKeyBase64 = privateKeyBase64;
            this.privateKey = RSAUtils.getPrivateKey(privateKeyBase64);
            this.privateEncryptCipher = Cipher.getInstance(ALGORITHM_TYPE);
            this.privateEncryptCipher.init(Cipher.ENCRYPT_MODE, privateKey);
            this.privateDecryptCipher = Cipher.getInstance(ALGORITHM_TYPE);
            this.privateDecryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        }

    }

    private void initParam(KeyPair keyPair) throws Exception {

//        公钥工具
        this.publicKey = keyPair.getPublic();
        this.publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        this.publicEncryptCipher = Cipher.getInstance(ALGORITHM_TYPE);
        this.publicEncryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        this.publicDecryptCipher = Cipher.getInstance(ALGORITHM_TYPE);
        this.publicDecryptCipher.init(Cipher.DECRYPT_MODE, publicKey);

//        私钥工具
        this.privateKey = keyPair.getPrivate();
        this.privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        this.privateEncryptCipher = Cipher.getInstance(ALGORITHM_TYPE);
        this.privateEncryptCipher.init(Cipher.ENCRYPT_MODE, privateKey);
        this.privateDecryptCipher = Cipher.getInstance(ALGORITHM_TYPE);
        this.privateDecryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
    }

    @Override
    public String sign(byte[] data) throws Exception {
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data);

        return Base64Utils.encode(signature.sign());
    }

    @Override
    public String sign(String content) throws Exception {
        return sign(content.getBytes(charset));
    }

    @Override
    public boolean verifySign(byte[] data, String sign) throws Exception {
        // 取公钥匙对象
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data);

        // 验证签名是否正常
        return signature.verify(Base64Utils.decode(sign));
    }

    @Override
    public boolean verifySign(String content, String sign) throws Exception {
        return verifySign(content.getBytes(charset), sign);
    }

    @Override
    public String encryptByPublicKeyToStr(String content) throws Exception {
        byte[] bytes = encryptByPublicKey(content);
        return Base64Utils.encode(bytes);
    }

    @Override
    public String encryptByPrivateKeyToStr(String content) throws Exception {
        byte[] bytes = encryptByPrivateKey(content);
        return Base64Utils.encode(bytes);
    }

    @Override
    public byte[] decryptByPublicKey(String content) throws Exception {
        byte[] decode = Base64Utils.decode(content);
        return decryptByPublicKey(decode);
    }

    @Override
    public String decryptByPublicKeyToStr(String content) throws Exception {
        byte[] decode = Base64Utils.decode(content);
        byte[] bytes = decryptByPublicKey(decode);
        return new String(bytes, charset);
    }


    @Override
    public byte[] decryptByPrivateKey(String content) throws Exception {
        byte[] decode = Base64Utils.decode(content);
        return decryptByPrivateKey(decode);
    }

    @Override
    public String decryptByPrivateKeyToStr(String content) throws Exception {
        byte[] decode = Base64Utils.decode(content);
        byte[] bytes = decryptByPrivateKey(decode);
        return new String(bytes, charset);
    }


}
