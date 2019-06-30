package com.latico.commons.common.util.codec.aca.common;


import com.latico.commons.common.util.codec.aca.AsymmetricCryptAlgorithm;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.reflect.ClassUtils;
import com.latico.commons.common.util.reflect.ResourcesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <PRE>
 * 非对称加密算法工厂
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-06-03 14:50
 * @Version: 1.0
 */
public class AsymmetricCryptAlgorithmFactory {

    /**
     * 实现类
     */
    private static final Map<AsymmetricCryptAlgorithmType, Class<AsymmetricCryptAlgorithm>> IMPL_CLASSES = new HashMap<>();

    private static final Logger LOG = LoggerFactory.getLogger(AsymmetricCryptAlgorithmFactory.class);
    static {
        try {
            List<Class<AsymmetricCryptAlgorithm>> classes = ResourcesUtils.getAllImplClassByFatherClass(AsymmetricCryptAlgorithm.class, AsymmetricCryptAlgorithm.class.getPackage().getName());

            if (classes != null) {
                for (Class<AsymmetricCryptAlgorithm> implClass : classes) {
                    AsymmetricCryptAlgorithmTypeAnnotation annotation = ClassUtils.getAnnotationPresentOnClass(implClass, AsymmetricCryptAlgorithmTypeAnnotation.class);
                    if (annotation == null) {
                        continue;
                    }
                    AsymmetricCryptAlgorithmType type = annotation.type();
                    if (type == null) {
                        LOG.error("没有有指定注解的type属性:{}", implClass);
                        continue;
                    }
                    IMPL_CLASSES.put(type, implClass);
                }
            }

        } catch (Exception e) {
            LOG.error(e);
        }
    }

    /**
     * 创建一个非对称加密算法实例
     * @param type
     * @param secretKey
     * @return
     * @throws Exception
     */
    public static AsymmetricCryptAlgorithm createInstance(AsymmetricCryptAlgorithmType type, String secretKey) throws Exception {
        return createInstance(type, secretKey, null);
    }

    /**
     * 创建一个非对称加密算法实例
     * @param type
     * @param secretKey
     * @param charset
     * @return
     * @throws Exception
     */
    public static AsymmetricCryptAlgorithm createInstance(AsymmetricCryptAlgorithmType type, String secretKey, String charset) throws Exception {
        Class<AsymmetricCryptAlgorithm> implClass = IMPL_CLASSES.get(type);
        if (implClass == null) {
            return null;
        }
        AsymmetricCryptAlgorithm instance = null;
        instance = implClass.newInstance();
        instance.init(secretKey, charset);
        return instance;
    }

    /**
     * @param type 算法类型
     * @param publicKeyBase64 可选
     * @param privateKeyBase64 可选
     * @return
     * @throws Exception
     */
    public static AsymmetricCryptAlgorithm createInstanceByPublicAndPrivateKey(AsymmetricCryptAlgorithmType type, String publicKeyBase64, String privateKeyBase64) throws Exception {
        Class<AsymmetricCryptAlgorithm> implClass = IMPL_CLASSES.get(type);
        if (implClass == null) {
            return null;
        }
        AsymmetricCryptAlgorithm instance = null;
        instance = implClass.newInstance();
        instance.initByPublicAndPrivateKey(publicKeyBase64, privateKeyBase64);
        return instance;
    }
}
