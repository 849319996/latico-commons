package com.latico.commons.spring.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * <PRE>
 * spring data jpa的分页工具
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-06-13 9:59
 * @Version: 1.0
 */
public class PageableUtils {

    /**
     *
     * @param pageNum 页号
     * @param perPageSize 每页大小
     * @return
     */
    public static Pageable createPageable(int pageNum, int perPageSize) {
        PageRequest pageable = PageRequest.of(pageNum - 1, perPageSize);
        return pageable;
    }

    /**
     * @param pageNum 页号
     * @param perPageSize 每页大小
     * @param direction 方向，升序还是降序 {@link Sort.Direction}
     * @param sortFields 排序的字段，可以多个
     * @return
     */
    public static Pageable createPageable(int pageNum, int perPageSize, Sort.Direction direction, String... sortFields) {
        PageRequest pageable = PageRequest.of(pageNum - 1, perPageSize, direction, sortFields);
        return pageable;
    }
}
