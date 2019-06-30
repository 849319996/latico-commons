package com.latico.commons.orm.mybatis;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * <PRE>
 * mybatis的分页插件
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-03-29 10:46
 * @Version: 1.0
 */
public class PageHelperUtils {

    /**
     * 开始分页
     *
     * @param pageNum  页码
     * @param pageSize 每页显示数量
     */
    public static <E> Page<E> startPage(int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize);
    }
}
