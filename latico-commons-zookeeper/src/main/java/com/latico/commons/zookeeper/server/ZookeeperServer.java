package com.latico.commons.zookeeper.server;

import com.latico.commons.common.util.io.PropertiesUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.zookeeper.ZookeeperUtils;

import java.util.Properties;

/**
 * <PRE>
 *  zookeeper服务器，单例
 *
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2019-05-23 13:45:11
 * @Version: 1.0
 */
public class ZookeeperServer {

    public static final Logger LOG = LoggerFactory.getLogger(ZookeeperServer.class);

    /**
     *
     */
    private String configFile = "./config/zoo.cfg";

    /**
     * 私有单例对象，需要使用volatile，让其他线程直接可见
     */
    private static volatile ZookeeperServer instance;

    /**
     * 状态
     */
    private boolean status;

    /**
     * 提供给外界获取单例的方法
     * @return 单例对象
     */
    public static ZookeeperServer getInstance() {
        //第一层检测，在锁的外面判断是否为空，效率高
        if (instance == null) {
            //开始进入加锁创建对象
            synchronized (ZookeeperServer.class) {
                //第二层检测，因为第一层检测期间可能其他线程正则创建，但是未创建完成，所以需要在这里进行二次判断
                if (instance == null) {
                    //创建实例
                    instance = new ZookeeperServer();
                }
            }
        }
        return instance;
    }

    /**
     * 私有构造方法，让外界不能创建对象，在这里做初始化逻辑处理
     */
    private ZookeeperServer() {
        status = startServer();
    }

    /**
     * 启动服务
     * @return
     */
    private boolean startServer() {
        try {
            Properties properties = PropertiesUtils.readPropertiesFromFile(configFile);
            boolean succ = ZookeeperUtils.createServer(properties);
            LOG.info("zookeeper启动完成, 状态:{}", succ);
            return succ;
        } catch (Exception e) {
            LOG.error(e);
        }

        return false;

    }

    public boolean getStatus() {
        return status;
    }
}
