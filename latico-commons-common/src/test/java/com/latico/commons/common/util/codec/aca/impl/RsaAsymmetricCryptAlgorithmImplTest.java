package com.latico.commons.common.util.codec.aca.impl;

import com.latico.commons.common.util.codec.Base64Utils;
import org.junit.Test;

import java.security.*;

public class RsaAsymmetricCryptAlgorithmImplTest {

    @Test
    public void init() throws Exception {
        // 基于RSA算法初始化密钥对生成器, 密钥大小为96-1024位
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024, new SecureRandom("AGB".getBytes()));

        KeyPair keyPair = keyPairGen.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        System.out.println(Base64Utils.encode(publicKey.getEncoded()));
        System.out.println("==================================");
        System.out.println(Base64Utils.encode(privateKey.getEncoded()));
    }
}