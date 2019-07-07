package com.latico.commons.db.mycat;

import com.latico.commons.common.config.AbstractConfig;
import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.io.PropertiesUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.other.PathUtils;
import com.latico.commons.db.datasource.DataSourceParam;
import com.latico.commons.db.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-06-17 17:13
 * @Version: 1.0
 */
public class Config extends AbstractConfig {

    private static final Logger LOG = LoggerFactory.getLogger(Config.class);

    /** PROJECT_NAME 项目名称 */
    private static final String PROJECT_NAME = "mycat";
    /**
     * 私有单例对象，需要使用volatile，让其他线程直接可见
     */
    private static volatile Config instance;

    /**
     * 数据源
     */
    private volatile DataSource dataSource;

    private DataSourceParam dataSourceParam;

    /**
     * 提供给外界获取单例的方法
     * @return 单例对象
     */
    public static Config getInstance() {
        //第一层检测，在锁的外面判断是否为空，效率高
        if (instance == null) {
            //开始进入加锁创建对象
            synchronized (Config.class) {
                //第二层检测，因为第一层检测期间可能其他线程正则创建，但是未创建完成，所以需要在这里进行二次判断
                if (instance == null) {
                    //创建实例
                    instance = new Config();
                }
            }
        }
        return instance;
    }

    /**
     * 私有构造方法，让外界不能创建对象，在这里做初始化逻辑处理
     */
    private Config() {
        initOrRefreshConfig();
        initDb();
    }

    @Override
    protected String getResourcesConfigFilePath() {
        return null;
    }

    @Override
    protected String getConfigFilePath() {
        return null;
    }

    @Override
    protected boolean initConfig(String fileContent) throws Exception {
        return false;
    }

    @Override
    protected void initOtherConfig() {
    }

    /**
     * 初始化数据库配置
     */
    private void initDb() {
        //初始化数据源
        final String dbFilePath = PathUtils.adapterFilePathSupportWebContainer(CONFIG_FILE_ROOT_DIR + "db.properties");
        if (FileUtils.exists(dbFilePath)) {
            try {
                Properties properties = PropertiesUtils.readPropertiesFromFile(dbFilePath, CharsetType.UTF8);
                List<DataSourceParam> dataSourceParams = DataSourceUtils.getMultiDataSourceParam(properties);
                for (DataSourceParam param : dataSourceParams) {
                    if (PROJECT_NAME.equals(param.getId())) {
                        this.dataSource = DataSourceUtils.createDruidDataSource(param);
                        this.dataSourceParam = param;
                    }
                }

                if (this.dataSource == null) {
                    LOG.warn("没有找到ID为datanet的数据源");
                }
            } catch (Exception e) {
                LOG.error(e);
            }

        }else{
            LOG.warn("数据库配置文件不存在,可能影响脚本模板加载:{}", dbFilePath);
        }


    }

    /**
     * 拿到数据源
     * @return
     */
    public DataSource getDataSource() {
        return dataSource;
    }
}
