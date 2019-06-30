package com.latico.commons.orm.mybatis;

import com.latico.commons.orm.mybatis.mapper.UserMapper;
import com.latico.commons.orm.mybatis.po.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class MybatisUtilsTest {

    String mybatisConfigFile = "config/mybatis_test.xml";

    @Test
    public void createSqlSessionFactoryByResourcesConfig() throws IOException {
        SqlSessionFactory sqlSessionFactory = MybatisUtils.createSqlSessionFactoryByResourcesConfigFile(mybatisConfigFile);
        SqlSession sqlSession = MybatisUtils.getSqlSessionDefault(sqlSessionFactory);

        MybatisUtils.close(sqlSession);
    }

    @Test
    public void queryType1() throws IOException {
        SqlSessionFactory sqlSessionFactory = MybatisUtils.createSqlSessionFactoryByResourcesConfigFile(mybatisConfigFile);
        SqlSession sqlSession = MybatisUtils.getSqlSessionDefault(sqlSessionFactory);
        List<Object> users = sqlSession.selectList("UserMapper.findAll");
        MybatisUtils.close(sqlSession);
        System.out.println(users);
    }
    @Test
    public void queryType2() throws IOException {
        SqlSessionFactory sqlSessionFactory = MybatisUtils.createSqlSessionFactoryByResourcesConfigFile(mybatisConfigFile);
        SqlSession sqlSession = MybatisUtils.getSqlSessionDefault(sqlSessionFactory);

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.findAll();
        MybatisUtils.close(sqlSession);
        System.out.println(users);
    }

    @Test
    public void createSqlSessionFactoryByConfigFile() throws IOException {
        SqlSessionFactory sqlSessionFactory = MybatisUtils.createSqlSessionFactoryByConfigFile("./config/mybatis_test.xml");
        SqlSession sqlSession = MybatisUtils.getSqlSessionDefault(sqlSessionFactory);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.findAll();
        MybatisUtils.close(sqlSession);
        System.out.println("打印用户"+users);
    }

    @Test
    public void pageHelper() throws IOException {
        SqlSessionFactory sqlSessionFactory = MybatisUtils.createSqlSessionFactoryByConfigFile("./config/mybatis_test.xml");
        SqlSession sqlSession = MybatisUtils.getSqlSessionDefault(sqlSessionFactory);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        PageHelperUtils.startPage(2, 1);
        List<User> users = userMapper.findAll();
        MybatisUtils.close(sqlSession);
        System.out.println("打印用户"+users);
        System.out.println(users.size());
        System.out.println(users);
    }
}