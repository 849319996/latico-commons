package com.latico.commons.db.mycat.dao;

import com.latico.commons.common.util.db.dao.BaseDao;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.db.mycat.Config;
import com.latico.commons.db.mycat.entity.Demo;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-06-17 17:13
 * @Version: 1.0
 */
public class DemoDao extends BaseDao<Demo> {
    private static final Logger LOG = LoggerFactory.getLogger(DemoDao.class);
    @Override
    protected Connection getConnection() {
        try {
            return Config.getInstance().getDataSource().getConnection();
        } catch (SQLException e) {
            LOG.error(e);
        }
        return null;
    }


}
