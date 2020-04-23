package com.latico.commons.common.util.codec.sea;

/**
 * <PRE>
 * 对称加密算法接口类
 *
 public void testDES() throws Exception {
 SymmetricEncryptAlgorithm symmetricEncryptAlgorithm = SymmetricEncryptAlgorithmFactory.createInstance(SymmetricEncryptAlgorithmType.DES, "ABC");
 String encryptToStr = symmetricEncryptAlgorithm.encryptToStr("ABCAJGJ你好");
 System.out.println(symmetricEncryptAlgorithm.decryptToStr(encryptToStr));
 }
 public void testAES() throws Exception {
 SymmetricEncryptAlgorithm symmetricEncryptAlgorithm = SymmetricEncryptAlgorithmFactory.createInstance(SymmetricEncryptAlgorithmType.AES, "ABC");
 String encryptToStr = symmetricEncryptAlgorithm.encryptToStr("ABCAJGJ你好");
 System.out.println(symmetricEncryptAlgorithm.decryptToStr(encryptToStr));
 }
 * </PRE>
 *
 * @author: latico
 * @date: 2019-06-03 13:57
 * @version: 1.0
 */
public interface SymmetricEncryptAlgorithm {

    /**
     * 初始化方法
     *
     * @param secretKey 秘钥
     */
    void init(String secretKey) throws Exception;

    /**
     * 初始化方法
     *
     * @param secretKey 秘钥
     * @param charset 字符串数据转换字节时用的字符集，包括秘钥的转换
     */
    void init(String secretKey, String charset) throws Exception;

    /**
     * 加密
     *
     * @param content 待加密内容
     * @return 加密后的密文 byte[]
     * @throws Exception
     */
    public byte[] encrypt(byte[] content) throws Exception;

    /**
     * 加密
     *
     * @param content
     * @return
     * @throws Exception
     */
    public byte[] encrypt(String content) throws Exception;

    /**
     * 加密
     *
     * @param content
     * @return
     * @throws Exception
     */
    public String encryptToStr(byte[] content) throws Exception;
    /**
     * 加密
     *
     * @param content
     * @return
     * @throws Exception
     */
    public String encryptToStr(String content) throws Exception;

    /**
     * 解密
     *
     * @param content 待解密内容
     * @return 解密后的明文 byte[]
     * @throws Exception
     */
    public byte[] decrypt(byte[] content) throws Exception;

    /**
     * 解密
     *
     * @param content
     * @return
     * @throws Exception
     */
    public byte[] decrypt(String content) throws Exception;

    /**
     * 解密
     *
     * @param content
     * @return
     * @throws Exception
     */
    public String decryptToStr(byte[] content) throws Exception;
    /**
     * 解密
     *
     * @param content
     * @return
     * @throws Exception
     */
    public String decryptToStr(String content) throws Exception;
}
