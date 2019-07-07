package com.latico.commons.orm.mybatis;

import com.latico.commons.common.util.io.FileUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * <PRE>
 * Mybatis工具类
 * </PRE>
 *
 * @Author: latico
 * @Date: 2018-12-31 21:51
 * @Version: 1.0
 */
public class MybatisUtils {

    /**
     * 通过资源配置文件路径创建SqlSessionFactory对象
     * SqlSessionFactory对象创建一次可以复用
     * @param resourceFilePath 资源文件路径
     * @return
     * @throws IOException
     */
    public static SqlSessionFactory createSqlSessionFactoryByResourcesConfigFile(String resourceFilePath) throws IOException {
        Reader resourceAsReader = Resources.getResourceAsReader(resourceFilePath);
        return new SqlSessionFactoryBuilder().build(resourceAsReader);
    }

    /**
     * 通过配置文件路径创建SqlSessionFactory对象
     * SqlSessionFactory对象创建一次可以复用
     * @param configFilePath 文件路径
     * @return
     * @throws IOException
     */
    public static SqlSessionFactory createSqlSessionFactoryByConfigFile(String configFilePath) throws IOException {
        InputStream inputStream = FileUtils.openInputStream(configFilePath);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    /**
     * 默认方式获取SqlSession
     * @param sqlSessionFactory
     * @return
     */
    public static SqlSession getSqlSessionDefault(SqlSessionFactory sqlSessionFactory){
        return sqlSessionFactory.openSession();
    }

    /**
     * 获取SqlSession,插入和更新操作自动提交
     * @param sqlSessionFactory
     * @return
     */
    public static SqlSession getSqlSessionAutoCommit(SqlSessionFactory sqlSessionFactory){
        return sqlSessionFactory.openSession(true);
    }

    /**
     * 关闭SqlSession
     * @param sqlSession
     */
    public static void close(SqlSession sqlSession) {
        if (sqlSession != null) {
            sqlSession.close();
        }
    }

}
