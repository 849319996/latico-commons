package com.latico.commons.common.util.codec.aca.common;

import java.lang.annotation.*;

/**
 * <PRE>
 * 非对称加密算法类型
 * </PRE>
 *
 * @author: latico
 * @date: 2019-06-03 14:53
 * @version: 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AsymmetricCryptAlgorithmTypeAnnotation {
    AsymmetricCryptAlgorithmType type();
}
