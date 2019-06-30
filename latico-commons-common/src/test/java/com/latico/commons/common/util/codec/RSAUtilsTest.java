package com.latico.commons.common.util.codec;

import com.latico.commons.common.util.codec.aca.AsymmetricCryptAlgorithm;
import com.latico.commons.common.util.codec.aca.common.AsymmetricCryptAlgorithmFactory;
import com.latico.commons.common.util.codec.aca.common.AsymmetricCryptAlgorithmType;
import org.junit.Test;

public class RSAUtilsTest {

    /**
     * 私钥加密，公钥解密
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        AsymmetricCryptAlgorithm rsa = AsymmetricCryptAlgorithmFactory.createInstance(AsymmetricCryptAlgorithmType.RSA, "abcd");

        String privateKeyBase64 = rsa.getPrivateKeyBase64();
        String publicKeyBase64 = rsa.getPublicKeyBase64();

        String srcData = "jajgha你好";

        String encryptStr = RSAUtils.encryptByPrivateKeyToStr(srcData, privateKeyBase64);

        System.out.println(RSAUtils.decryptByPublicKeyToStr(encryptStr, publicKeyBase64));

        String sign = RSAUtils.sign(srcData, privateKeyBase64);
        System.out.println(RSAUtils.verifySign(srcData, publicKeyBase64, sign));

    }

    /**
     * 公钥加密，私钥解密
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        AsymmetricCryptAlgorithm rsa = AsymmetricCryptAlgorithmFactory.createInstance(AsymmetricCryptAlgorithmType.RSA, "abcd");

        String privateKeyBase64 = rsa.getPrivateKeyBase64();
        String publicKeyBase64 = rsa.getPublicKeyBase64();

        String srcData = "jajgha你好";

        String encryptStr = RSAUtils.encryptByPublicKeyToStr(srcData, publicKeyBase64);

        System.out.println(RSAUtils.decryptByPrivateKeyToStr(encryptStr, privateKeyBase64));

        String sign = RSAUtils.sign(srcData, privateKeyBase64);
        System.out.println(RSAUtils.verifySign(srcData, publicKeyBase64, sign));

    }

    /**
     *
     */
    @Test
    public void getPublicKeyBase64() throws Exception {
        String secretKey = "abcd";
        System.out.println(RSAUtils.getPublicKeyBase64(secretKey, null));
        System.out.println(RSAUtils.getPublicKeyBase64(secretKey, null));
        System.out.println(RSAUtils.getPublicKeyBase64(secretKey, null));
        System.out.println(RSAUtils.getPrivateKeyBase64(secretKey, null));
        System.out.println(RSAUtils.getPrivateKeyBase64(secretKey, null));
        System.out.println(RSAUtils.getPrivateKeyBase64(secretKey, null));
    }

    /**
     *
     */
    @Test
    public void test3() throws Exception {
        String secretKey = "abcd";
        String publicKeyBase64 = RSAUtils.getPublicKeyBase64(secretKey, null);
        String privateKeyBase64 = RSAUtils.getPrivateKeyBase64(secretKey, null);

        String encrypt = RSAUtils.encryptByPrivateKeyToStr("ajajg你好", privateKeyBase64);
        String result = RSAUtils.decryptByPublicKeyToStr(encrypt, publicKeyBase64);
        System.out.println(result);
    }
    /**
     *
     */
    @Test
    public void test4() throws Exception {
        String secretKey = "abcd";
        String publicKeyBase64 = RSAUtils.getPublicKeyBase64(secretKey, null);
        String privateKeyBase64 = RSAUtils.getPrivateKeyBase64(secretKey, null);

        String encrypt = RSAUtils.encryptByPublicKeyToStr("ajajg你好", publicKeyBase64);
        String result = RSAUtils.decryptByPrivateKeyToStr(encrypt, privateKeyBase64);
        System.out.println(result);
    }
}