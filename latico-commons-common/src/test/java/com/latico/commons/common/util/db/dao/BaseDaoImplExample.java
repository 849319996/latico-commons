package com.latico.commons.common.util.db.dao;

import com.latico.commons.common.util.compare.CompareBean;
import com.latico.commons.common.util.string.StringUtils;

import java.sql.Connection;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-24 9:17
 * @Version: 1.0
 */
public class BaseDaoImplExample extends BaseDao<CompareBean> {

    @Override
    public String getCharset() {
        if (StringUtils.isBlank(this.charset)) {
            this.charset = "GBK";
        }
        return super.getCharset();
    }

    @Override
    protected Connection getConnection() {
        return null;
    }

}
