package com.latico.commons.orm.mybatis.mapper;

import com.latico.commons.orm.mybatis.po.User;

import java.util.List;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2018-12-31 22:58
 * @Version: 1.0
 */
public interface UserMapper {

    List<User> findAll();
}
