package com.latico.commons.common.util.codec.sea.common;


import com.latico.commons.common.util.codec.sea.SymmetricEncryptAlgorithm;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.reflect.ClassUtils;
import com.latico.commons.common.util.reflect.ResourcesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <PRE>
 * 对称加密算法工厂类
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-06-03 14:50
 * @Version: 1.0
 */
public class SymmetricEncryptAlgorithmFactory {

    /**
     * 实现类
     */
    private static final Map<SymmetricEncryptAlgorithmType, Class<SymmetricEncryptAlgorithm>> IMPL_CLASSES = new HashMap<>();

    private static final Logger LOG = LoggerFactory.getLogger(SymmetricEncryptAlgorithmFactory.class);
    static {
        try {
            List<Class<SymmetricEncryptAlgorithm>> classes = ResourcesUtils.getAllImplClassByFatherClass(SymmetricEncryptAlgorithm.class, SymmetricEncryptAlgorithm.class.getPackage().getName());

            if (classes != null) {
                for (Class<SymmetricEncryptAlgorithm> implClass : classes) {
                    SymmetricEncryptAlgorithmTypeAnnotation annotation = ClassUtils.getAnnotationPresentOnClass(implClass, SymmetricEncryptAlgorithmTypeAnnotation.class);
                    if (annotation == null) {
                        continue;
                    }
                    SymmetricEncryptAlgorithmType type = annotation.type();
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
     * 创建一个对称加密实例
     * 不指定字符集
     * @param type 对称加密类型
     * @param secretKey 秘钥
     * @return
     * @throws Exception
     */
    public static SymmetricEncryptAlgorithm createInstance(SymmetricEncryptAlgorithmType type, String secretKey) throws Exception {
        return createInstance(type, secretKey, null);
    }

    /**
     * 创建一个对称加密实例
     * @param type 对称加密类型
     * @param secretKey 秘钥
     * @param charset 字符集
     * @return
     * @throws Exception
     */
    public static SymmetricEncryptAlgorithm createInstance(SymmetricEncryptAlgorithmType type, String secretKey, String charset) throws Exception {
        Class<SymmetricEncryptAlgorithm> implClass = IMPL_CLASSES.get(type);
        if (implClass == null) {
            return null;
        }
        SymmetricEncryptAlgorithm instance = null;
        instance = implClass.newInstance();
        instance.init(secretKey, charset);
        return instance;
    }
}
