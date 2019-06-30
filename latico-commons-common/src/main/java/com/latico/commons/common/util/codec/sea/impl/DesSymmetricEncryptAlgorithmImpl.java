package com.latico.commons.common.util.codec.sea.impl;

import com.latico.commons.common.util.codec.sea.AbstractSymmetricEncryptAlgorithm;
import com.latico.commons.common.util.codec.sea.common.SymmetricEncryptAlgorithmType;
import com.latico.commons.common.util.codec.sea.common.SymmetricEncryptAlgorithmTypeAnnotation;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

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
@SymmetricEncryptAlgorithmTypeAnnotation(type = SymmetricEncryptAlgorithmType.DES)
public class DesSymmetricEncryptAlgorithmImpl extends AbstractSymmetricEncryptAlgorithm {
    /**
     * 算法类型
     */
    protected static final String ALGORITHM_TYPE = "DES";

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

        Key key = getKey(this.secretKey.getBytes(this.charset));
        encryptCipher = Cipher.getInstance(ALGORITHM_TYPE);
        //指定是加密
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        decryptCipher = Cipher.getInstance(ALGORITHM_TYPE);
        //指定是解密
        decryptCipher.init(Cipher.DECRYPT_MODE, key);

    }

    @Override
    public byte[] encrypt(String content) throws Exception {
        return encrypt(content.getBytes(charset));
    }

    @Override
    public String encryptToStr(byte[] content) throws Exception {
        return byteArr2HexStr(encrypt(content));
    }

    @Override
    public String encryptToStr(String content) throws Exception {
        byte[] bytes = content.getBytes(charset);
        return encryptToStr(bytes);
    }

    @Override
    public byte[] decrypt(String content) throws Exception {
        byte[] bytes = hexStr2ByteArr(content);
        return decrypt(bytes);
    }

    @Override
    public String decryptToStr(byte[] content) throws Exception {
        return new String(decrypt(content), charset);
    }

    @Override
    public String decryptToStr(String content) throws Exception {
        byte[] bytes = hexStr2ByteArr(content);
        return decryptToStr(bytes);
    }

    private Key getKey(byte[] arrBTmp) {
        byte[] arrB = new byte[8];
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }

        Key key = new SecretKeySpec(arrB, ALGORITHM_TYPE);
        return key;
    }

    private String byteArr2HexStr(byte[] arrB)
            throws Exception {
        int iLen = arrB.length;
        StringBuilder sb = new StringBuilder(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp;
            for (intTmp = arrB[i]; intTmp < 0; intTmp += 256) {
                ;
            }
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }

        return sb.toString();
    }

    private byte[] hexStr2ByteArr(String strIn)
            throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i += 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }

        return arrOut;
    }

}
