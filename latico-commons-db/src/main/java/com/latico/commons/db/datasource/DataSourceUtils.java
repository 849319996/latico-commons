package com.latico.commons.db.datasource;

import com.latico.commons.db.datasource.c3p0.C3p0Utils;
import com.latico.commons.db.datasource.dbcp.DbcpUtils;
import com.latico.commons.db.datasource.druid.DruidUtils;
import com.latico.commons.db.datasource.proxool.ProxoolUtils;
import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.util.db.DBUtils;
import com.latico.commons.common.util.io.PropertiesUtils;
import com.latico.commons.common.util.reflect.ObjectUtils;

import javax.sql.DataSource;
import java.util.*;

/**
 * <PRE>
 * 数据源工具
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-02 14:52
 * @Version: 1.0
 */
public class DataSourceUtils {

    /**
     * 通过文件系统路径获取DataSourceParam
     * @param filePath
     * @return
     * @throws Exception
     */
    public static DataSourceParam getDataSourceParamByPropertiesFile(String filePath) throws Exception {
        DataSourceParam dsp = new DataSourceParam();
        Properties properties = PropertiesUtils.readPropertiesFromFile(filePath, CharsetType.UTF8);
        return getDataSourceParam(properties);
    }

    /**
     * 通过文件系统路径获取DataSourceParam
     * @param filePath
     * @return
     * @throws Exception
     */
    public static List<DataSourceParam> getMultiDataSourceParamByPropertiesFile(String filePath) throws Exception {
        DataSourceParam dsp = new DataSourceParam();
        Properties properties = PropertiesUtils.readPropertiesFromFile(filePath, CharsetType.UTF8);
        return getMultiDataSourceParam(properties);
    }

    /**
     * 通过资源路径获取
     * @param resource
     * @return
     * @throws Exception
     */
    public static DataSourceParam getDataSourceParamByPropertiesResourcesFile(String resource) throws Exception {
        DataSourceParam dsp = new DataSourceParam();
        Properties properties = PropertiesUtils.readPropertiesFromResources(resource, CharsetType.UTF8);
        return getDataSourceParam(properties);
    }
    /**
     * 通过资源路径获取
     * @param resource
     * @return
     * @throws Exception
     */
    public static List<DataSourceParam> getMultiDataSourceParamByPropertiesResourcesFile(String resource) throws Exception {
        DataSourceParam dsp = new DataSourceParam();
        Properties properties = PropertiesUtils.readPropertiesFromResources(resource, CharsetType.UTF8);
        return getMultiDataSourceParam(properties);
    }

    /**
     * 通过properties获取
     * @param properties
     * @return
     * @throws Exception
     */
    public static DataSourceParam getDataSourceParam(Properties properties) throws Exception {
        DataSourceParam dsp = new DataSourceParam();
        Map<String, Object> map = PropertiesUtils.toMapStrObj(properties);
        ObjectUtils.writeValueToSimpleField(dsp, map);
        return dsp;
    }

    /**
     * Properties配置多个数据源，通过properties获取
     * @param properties 里面有个多数据源
     * @return
     * @throws Exception
     */
    public static List<DataSourceParam> getMultiDataSourceParam(Properties properties) throws Exception {
        List<DataSourceParam> dsps = new ArrayList<>();
        final String defDbKey = "";
        Map<String, Properties> pros = new LinkedHashMap<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = entry.getKey().toString();
            int lastIndexOf = key.lastIndexOf(".");

            Properties pro = null;
            String dbKey = null;
            String filedName = null;
            if (lastIndexOf != -1) {
                dbKey = key.substring(0, lastIndexOf);
                filedName = key.substring(lastIndexOf + 1, key.length());
            }else{
                dbKey = defDbKey;
                filedName = key;
            }

            pro = pros.get(dbKey);
            if (pro == null) {
                pro = new Properties();
                pros.put(dbKey, pro);
            }
            pro.put(filedName, entry.getValue());
        }

        //判断是否数据源ID唯一
        Set<String> existsId = new HashSet<>();
        //转换
        for (Map.Entry<String, Properties> entry : pros.entrySet()) {
            Properties pro = entry.getValue();
            DataSourceParam dsp = new DataSourceParam();
            Map<String, Object> map = PropertiesUtils.toMapStrObj(pro);
            ObjectUtils.writeValueToSimpleField(dsp, map);

            //判断是否配置重复
            if (existsId.contains(dsp.getId())) {
                throw new IllegalArgumentException("数据源唯一ID有重复:" + dsp.getId());
            }
            existsId.add(dsp.getId());

            dsps.add(dsp);
        }

        return dsps;
    }
    /**
     * 创建单个Druid数据源并返回
     * @param dsp
     * @return
     */
    public static DataSource createDruidDataSource(DataSourceParam dsp)throws Exception {
        return DruidUtils.createDataSource(dsp);
    }

    /**
     * 创建单个Proxool数据源并返回，想一次性创建多个的话请使用{@link ProxoolUtils}
     * @param dsp
     * @return
     */
    public static DataSource createProxoolDataSource(DataSourceParam dsp) throws Exception {
        return ProxoolUtils.createDataSource(dsp);
    }

    /**
     * 创建单个Dbcp数据源并返回
     * @param dsp
     * @return
     */
    public static DataSource createDbcpDataSource(DataSourceParam dsp) throws Exception {
        return DbcpUtils.createDataSource(dsp);
    }
    /**
     * 创建单个Dbcp数据源并返回
     * @param dsp
     * @return
     */
    public static DataSource createC3p0DataSource(DataSourceParam dsp) throws Exception {
        return C3p0Utils.createDataSource(dsp);
    }

    /**
     * 测试数据源连接是否可用
     * @param ds 数据源
     * @return true:连接可用; false:连接不可用
     */
    public static boolean testDbAvailable(DataSourceParam ds) {
        boolean isOk = false;
        if(ds != null) {
            String driver = ds.getDriverClassName();
            String url = ds.getUrl();
            String username = ds.getUsername();
            String password = ds.getPassword();
            isOk = DBUtils.testDbAvailable(driver, url, username, password);

        }
        return isOk;
    }


}
