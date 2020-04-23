package com.latico.commons.common.util.codec.aca;

/**
 * <PRE>
 *     非对称加密算法接口类
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

 另外可以：

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

 * </PRE>
 *
 * @author: latico
 * @date: 2019-06-03 15:47
 * @version: 1.0
 */
public interface AsymmetricCryptAlgorithm {

    /**
     * 随机生成的秘钥来创建公钥和私钥
     * @throws Exception
     */
    void init() throws Exception;
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
     * @param charset   字符串数据转换字节时用的字符集，包括秘钥的转换
     */
    void init(String secretKey, String charset) throws Exception;

    /**
     * 通过公钥和私钥初始化对象，
     * @param publicKeyBase64 可选
     * @param privateKeyBase64 可选
     */
    void initByPublicAndPrivateKey(String publicKeyBase64, String privateKeyBase64) throws Exception;

    /**
     * 获取公钥的Base64编码格式
     * @return
     */
    public String getPublicKeyBase64();

    /**
     * 获取私钥的Base64编码格式
     * @return
     */
    public String getPrivateKeyBase64();

    /**
     * 对信息生成数字签名（用私钥）
     *
     * @param content 原数据
     * @return 数字签名
     * @throws Exception
     */
    public String sign(byte[] content) throws Exception;
    public String sign(String content) throws Exception;

    /**
     * 校验数字签名（用公钥）
     *
     * @param content 原数据
     * @param sign    数字签名
     * @return
     * @throws Exception
     */
    public boolean verifySign(byte[] content, String sign) throws Exception;
    public boolean verifySign(String content, String sign) throws Exception;

    /**
     * 公钥加密
     *
     * @param content 待加密内容
     * @return 加密后的密文 byte[]
     * @throws Exception
     */
    public byte[] encryptByPublicKey(byte[] content) throws Exception;

    public byte[] encryptByPublicKey(String content) throws Exception;

    public String encryptByPublicKeyToStr(String content) throws Exception;

    /**
     * 私钥加密
     *
     * @param content
     * @return
     * @throws Exception
     */
    public byte[] encryptByPrivateKey(byte[] content) throws Exception;

    public byte[] encryptByPrivateKey(String content) throws Exception;

    public String encryptByPrivateKeyToStr(String content) throws Exception;

    /**
     * 公钥解密
     *
     * @param content 待加密内容
     * @return 加密后的密文 byte[]
     * @throws Exception
     */
    public byte[] decryptByPublicKey(byte[] content) throws Exception;

    public byte[] decryptByPublicKey(String content) throws Exception;

    public String decryptByPublicKeyToStr(String content) throws Exception;

    /**
     * 私钥解密
     *
     * @param content
     * @return
     * @throws Exception
     */
    public byte[] decryptByPrivateKey(byte[] content) throws Exception;

    public byte[] decryptByPrivateKey(String content) throws Exception;

    public String decryptByPrivateKeyToStr(String content) throws Exception;

}
