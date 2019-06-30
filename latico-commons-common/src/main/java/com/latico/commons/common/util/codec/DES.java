package com.latico.commons.common.util.codec;

import com.latico.commons.common.util.codec.sea.SymmetricEncryptAlgorithm;
import com.latico.commons.common.util.codec.sea.common.SymmetricEncryptAlgorithmFactory;
import com.latico.commons.common.util.codec.sea.common.SymmetricEncryptAlgorithmType;

/**
 * <PRE>
 * DES算法对称加解密技术，生成对象的目的是为了提高效率，复用加解密的对象
 *
 * 过时，请使用{@link SymmetricEncryptAlgorithmFactory}
 *
 example:
 DES des = new DES("abc");
 String encrypt = des.encrypt("12345");
 System.out.println(encrypt);
 System.out.println(des.decrypt(encrypt));

 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年8月29日</B>
 * @author    <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @since     <B>JDK1.6</B>
 */
@Deprecated
public class DES {

    private SymmetricEncryptAlgorithm instance;

    private static final SymmetricEncryptAlgorithmType TYPE = SymmetricEncryptAlgorithmType.DES;

    /**
     * 默认构造方法，使用默认秘钥
     */
    public DES() throws Exception {
        this.instance = SymmetricEncryptAlgorithmFactory.createInstance(TYPE, null);
    }

    /**
     * 指定秘钥
     * @param srcKey 秘钥
     * @throws Exception
     */
    public DES(String srcKey) throws Exception {
        this.instance = SymmetricEncryptAlgorithmFactory.createInstance(TYPE, srcKey);
    }
    /**
     * 指定秘钥，指定字符集
     *
     * @param strkey   秘钥
     * @param charset 字符集
     * @throws Exception
     */
    public DES(String strkey, String charset) throws Exception {
        this.instance = SymmetricEncryptAlgorithmFactory.createInstance(TYPE, strkey, charset);
    }


    /**
     * 加密
     * @param data 数据
     * @return
     * @throws Exception
     */
    public byte[] encrypt(byte[] data) throws Exception {
        return instance.encrypt(data);
    }

    /**
     * 加密
     * @param data
     * @return
     * @throws Exception
     */
    public String encrypt(String data) throws Exception {
        return instance.encryptToStr(data);
    }

    /**
     * 解密
     * @param data
     * @return
     * @throws Exception
     */
    public byte[] decrypt(byte[] data) throws Exception {
        return instance.decrypt(data);

    }

    /**
     * 解密
     * @param data
     * @return
     * @throws Exception
     */
    public String decrypt(String data) throws Exception {
        return instance.decryptToStr(data);
    }

}
