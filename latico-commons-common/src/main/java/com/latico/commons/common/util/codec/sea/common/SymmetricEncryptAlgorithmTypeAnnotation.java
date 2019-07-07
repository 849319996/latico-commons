package com.latico.commons.common.util.codec.sea.common;

import java.lang.annotation.*;

/**
 * <PRE>
 * 对称加密算法类型枚举
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-06-03 14:53
 * @Version: 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SymmetricEncryptAlgorithmTypeAnnotation {
    SymmetricEncryptAlgorithmType type();
}
