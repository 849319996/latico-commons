package com.latico.commons.common.util.other;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * <PRE>
 * 用于代替空指针判断的if else代码
 * </PRE>
 *
 * @author: latico
 * @date: 2019-08-20 15:10
 * @version: 1.0
 */
public class OptionalUtils {

    /**
     * 创建一个
     * @param value
     * @param <T>
     * @return
     */
    public static <T, R> Optional<T> getOptional(T value) {
        return Optional.ofNullable(value);
    }

    /**
     * Map方法执行
     * @param value
     * @param function
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> Optional<R> map(T value, Function<? super T, ? extends R> function) {
        Optional<T> optional = Optional.ofNullable(value);
        return optional.map(function);
    }

    /**
     * @param optional
     * @param predicates
     * @param <T>
     * @return
     */
    public static <T> Optional<T> filter(Optional<T> optional, Predicate<? super T>... predicates) {
        for (Predicate<? super T> predicate : predicates) {
            optional = optional.filter(predicate);
        }
        return optional;
    }


}
