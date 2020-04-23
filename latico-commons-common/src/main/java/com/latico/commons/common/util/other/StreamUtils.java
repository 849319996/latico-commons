package com.latico.commons.common.util.other;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <PRE>
 * stream流式处理
 * </PRE>
 *
 * @author: latico
 * @date: 2019-08-21 10:06
 * @version: 1.0
 */
public class StreamUtils {

    /**
     * 过滤，支持多个
     * @param list
     * @param predicates
     * @param <T>
     * @return
     */
    public static <T> List<T> filter(List<T> list, Predicate<? super T>... predicates) {
        Stream<T> stream = list.stream();
        for (Predicate<? super T> predicate : predicates) {
            stream = stream.filter(predicate);
        }
        return stream.collect(Collectors.toList());
    }

    /**
     *
     * 过滤，支持多个
     * @param set
     * @param predicates
     * @param <T>
     * @return
     */
    public static <T> Set<T> filter(Set<T> set, Predicate<? super T>... predicates) {
        List<T> newList = null;
        Stream<T> stream = set.stream();
        for (Predicate<? super T> predicate : predicates) {
            stream = stream.filter(predicate);
        }
        return stream.collect(Collectors.toSet());
    }


}
