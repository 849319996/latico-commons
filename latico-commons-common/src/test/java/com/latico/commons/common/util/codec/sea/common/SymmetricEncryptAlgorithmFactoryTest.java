package com.latico.commons.common.util.codec.sea.common;

import com.latico.commons.common.util.codec.sea.SymmetricEncryptAlgorithm;
import org.junit.Test;

public class SymmetricEncryptAlgorithmFactoryTest {

    @Test
    public void testDES() throws Exception {
        SymmetricEncryptAlgorithm symmetricEncryptAlgorithm = SymmetricEncryptAlgorithmFactory.createInstance(SymmetricEncryptAlgorithmType.DES, "ABC");
        String encryptToStr = symmetricEncryptAlgorithm.encryptToStr("ABCAJGJ你好");
        System.out.println(symmetricEncryptAlgorithm.decryptToStr(encryptToStr));
    }
    @Test
    public void testAES() throws Exception {
        SymmetricEncryptAlgorithm symmetricEncryptAlgorithm = SymmetricEncryptAlgorithmFactory.createInstance(SymmetricEncryptAlgorithmType.AES, "ABC");
        String encryptToStr = symmetricEncryptAlgorithm.encryptToStr("ABCAJGJ你好");
        System.out.println(symmetricEncryptAlgorithm.decryptToStr(encryptToStr));
    }


}