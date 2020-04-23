package com.latico.commons.common.util.codec;


import com.latico.commons.common.util.codec.aca.AsymmetricCryptAlgorithm;
import com.latico.commons.common.util.codec.aca.common.AsymmetricCryptAlgorithmFactory;
import com.latico.commons.common.util.codec.aca.common.AsymmetricCryptAlgorithmType;
import com.latico.commons.common.util.codec.sea.SymmetricEncryptAlgorithm;
import com.latico.commons.common.util.codec.sea.common.SymmetricEncryptAlgorithmFactory;
import com.latico.commons.common.util.codec.sea.common.SymmetricEncryptAlgorithmType;

/**
 * <PRE>
 * 加解密工具
 * 对称加密：AES、DES
 * 非对称加密：RSA
 * </PRE>
 *
 * @author: latico
 * @date: 2019-07-05 10:12
 * @version: 1.0
 */
public class CryptoUtils {


    /**
     * 创建一个DES加解密对象
     * @param secretKey 秘钥
     * @return
     */
    public static SymmetricEncryptAlgorithm createDES(String secretKey) throws Exception {
        return SymmetricEncryptAlgorithmFactory.createInstance(SymmetricEncryptAlgorithmType.DES, secretKey);
    }

    /**
     * 创建一个DES加解密对象
     * @param secretKey 秘钥
     * @param charset 秘钥和数据转换时使用的字符集，可以为空，默认UTF-8
     * @return
     */
    public static SymmetricEncryptAlgorithm createDES(String secretKey, String charset) throws Exception {
        return SymmetricEncryptAlgorithmFactory.createInstance(SymmetricEncryptAlgorithmType.DES, secretKey, charset);
    }

    /**
     * 创建一个AES加解密对象
     * @param secretKey 秘钥
     * @return
     */
    public static SymmetricEncryptAlgorithm createAES(String secretKey) throws Exception {
        return SymmetricEncryptAlgorithmFactory.createInstance(SymmetricEncryptAlgorithmType.AES, secretKey);
    }

    /**
     * 创建一个AES加解密对象
     * @param secretKey 秘钥
     * @param charset 秘钥和数据转换时使用的字符集，可以为空，默认UTF-8
     * @return
     */
    public static SymmetricEncryptAlgorithm createAES(String secretKey, String charset) throws Exception {
        return SymmetricEncryptAlgorithmFactory.createInstance(SymmetricEncryptAlgorithmType.AES, secretKey, charset);
    }


    /**
     * 创建一个RSA加解密对象，里面包括了公钥私钥操作
     * @param secretKey 秘钥
     * @return
     * @throws Exception
     */
    public static AsymmetricCryptAlgorithm createRSA(String secretKey) throws Exception {
        return AsymmetricCryptAlgorithmFactory.createInstance(AsymmetricCryptAlgorithmType.RSA, secretKey);
    }


    /**
     * 创建一个RSA加解密对象，里面包括了公钥私钥操作
     * @param secretKey 秘钥
     * @param charset   秘钥和数据转换时使用的字符集，可以为空，默认UTF-8
     * @return
     * @throws Exception
     */
    public static AsymmetricCryptAlgorithm createRSA(String secretKey, String charset) throws Exception {
        return AsymmetricCryptAlgorithmFactory.createInstance(AsymmetricCryptAlgorithmType.RSA, secretKey, charset);
    }


}
