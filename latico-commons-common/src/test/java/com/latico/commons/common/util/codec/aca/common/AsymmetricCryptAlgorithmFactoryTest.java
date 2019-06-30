package com.latico.commons.common.util.codec.aca.common;

import com.latico.commons.common.util.codec.RSAUtils;
import com.latico.commons.common.util.codec.aca.AsymmetricCryptAlgorithm;
import org.junit.Test;

public class AsymmetricCryptAlgorithmFactoryTest {

    /**
     * 私钥加密，公钥解密
     * @throws Exception
     */
    @Test
    public void createInstance() throws Exception {
        AsymmetricCryptAlgorithm rsa = AsymmetricCryptAlgorithmFactory.createInstance(AsymmetricCryptAlgorithmType.RSA, "abcd");

        String srcData = "jajgha你好";
//        要使用私钥加密
        String str = rsa.encryptByPrivateKeyToStr(srcData);

//        要使用公钥解密
        String result = rsa.decryptByPublicKeyToStr(str);
        System.out.println(result);

//        认证签名
        String sign = rsa.sign(srcData);
        System.out.println(rsa.verifySign(srcData, sign));

    }

    /**
     * 公钥加密，私钥解密
     * @throws Exception
     */
    @Test
    public void createInstance2() throws Exception {
        AsymmetricCryptAlgorithm rsa = AsymmetricCryptAlgorithmFactory.createInstance(AsymmetricCryptAlgorithmType.RSA, "abcd");

        String srcData = "jajgha你好";
//        要使用私钥加密
        String str = rsa.encryptByPublicKeyToStr(srcData);

//        要使用公钥解密
        String result = rsa.decryptByPrivateKeyToStr(str);
        System.out.println(result);

//        认证签名
        String sign = rsa.sign(srcData);
        System.out.println(rsa.verifySign(srcData, sign));

    }

    @Test
    public void createInstanceByPublicAndPrivateKey() throws Exception {
        String secretKey = "abcd";
        String publicKeyBase64 = RSAUtils.getPublicKeyBase64(secretKey, null);
        String privateKeyBase64 = RSAUtils.getPrivateKeyBase64(secretKey, null);

        AsymmetricCryptAlgorithm rsa = AsymmetricCryptAlgorithmFactory.createInstanceByPublicAndPrivateKey(AsymmetricCryptAlgorithmType.RSA, publicKeyBase64, privateKeyBase64);

        String encrypt = rsa.encryptByPrivateKeyToStr("jalljg你好");
        String str = rsa.decryptByPublicKeyToStr(encrypt);
        System.out.println(str);

    }
    @Test
    public void createInstanceByPublicAndPrivateKey2() throws Exception {
        String secretKey = "abcd";
        String publicKeyBase64 = RSAUtils.getPublicKeyBase64(secretKey, null);
        String privateKeyBase64 = RSAUtils.getPrivateKeyBase64(secretKey, null);

        AsymmetricCryptAlgorithm rsa = AsymmetricCryptAlgorithmFactory.createInstanceByPublicAndPrivateKey(AsymmetricCryptAlgorithmType.RSA, publicKeyBase64, privateKeyBase64);

        String encrypt = rsa.encryptByPublicKeyToStr("jalljg你好");
        String str = rsa.decryptByPrivateKeyToStr(encrypt);
        System.out.println(str);

    }
}