package com.latico.commons.common.util.logging;

import java.lang.annotation.*;

/**
 * <PRE>
 *  日志实现注解
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2019-06-27 11:47:35
 * @Version: 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogImplAnnotation {

    /**
     * @return 日志类型枚举
     */
    LogTypeEnum value();
}
