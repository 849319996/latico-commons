package com.latico.commons.common.util.codec.aca.common;

import java.lang.annotation.*;

/**
 * <PRE>
 * 非对称加密算法类型
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
public @interface AsymmetricCryptAlgorithmTypeAnnotation {
    AsymmetricCryptAlgorithmType type();
}
