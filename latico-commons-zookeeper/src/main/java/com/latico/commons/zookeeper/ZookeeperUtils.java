package com.latico.commons.zookeeper;

import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.io.PropertiesUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.other.PathUtils;
import com.latico.commons.common.util.string.StringUtils;
import com.latico.commons.common.util.system.SystemUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerMain;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-30 11:31
 * @Version: 1.0
 */
public class ZookeeperUtils {
    public static final Logger LOG = LoggerFactory.getLogger(ZookeeperUtils.class);

    /**
     * 启动zookeeper服务器
     *
     * 会自动判断是单机模式还是集群模式
     * 会自动判断创建myid文件
     *
     * 要判断，是否有配置集群，用哪种方式，集群模式会配置server.开头的配置
     * @param zkProp 配置，也就是zoo.cfg的内容
     * @return
     */
    public static boolean createServer(Properties zkProp) {
//        是否集群
        boolean isCluster = isCluster(zkProp);

        LOG.info("判断zookeeper模式是否集群模式:{}, 配置:{}", isCluster, zkProp);

//        创建数据文件夹和myid文件
        if (!createDataDir(zkProp)) {
            return false;
        }
        if (!createDataLogDir(zkProp)) {
            return false;
        }

        if (isCluster) {
//            集群模式需要创建myid文件
            boolean succ = createMyidFileByCluster(zkProp);
            if (!succ) {
                return false;
            }

            return createServerCluster(zkProp);
        } else {
            return createServerStandalone(zkProp);
        }

    }

    /**
     * 创建myid文件
     * @param zkProp
     */
    private static boolean createMyidFileByCluster(Properties zkProp) {
        try {
            String dataDir = zkProp.getProperty("dataDir");
            String myidFile = PathUtils.combine(dataDir, "myid");

//        已经存在就不管了
            if (FileUtils.exists(myidFile)) {
                return true;
            }

//        寻找myid的值

            Pattern keyPat = Pattern.compile("server\\.(\\d+)");
            Pattern valuePat = Pattern.compile("(\\S+?):(\\d+):(\\d+)");
            String myid = null;

//        通过IP去匹配
            List<String> allLocalInetAddressIP = SystemUtils.getAllLocalInetAddressIP();
            String localHostName = SystemUtils.getLocalHostName();
            for (Map.Entry<Object, Object> entry : zkProp.entrySet()) {
                Matcher keyMatcher = keyPat.matcher(entry.getKey().toString());
                if (keyMatcher.find()) {
                    Matcher valueMatcher = valuePat.matcher(entry.getValue().toString());
                    if (valueMatcher.find()) {
                        String host = valueMatcher.group(1);
                        if (allLocalInetAddressIP.contains(host) || localHostName.equals(host)) {
                            myid = keyMatcher.group(1);
                            LOG.info("自动搜索myid时通过主机:[{}]找到myid为:{}", host, myid);
                            break;
                        }
                    }

                }
            }

            if (StringUtils.isBlank(myid)) {
                LOG.error("创建myid文件:[{}],内容:[{}], 失败", myidFile, myid);
                return false;
            } else {
                File file = FileUtils.createFile(myidFile);
                FileUtils.write(file, myid);
                LOG.info("创建myid文件:[{}],内容:[{}], 成功", myidFile, myid);
            }
            return true;
        } catch (Exception e) {
            LOG.error(e);
        }

        return false;
    }

    /**
     * 创建数据目录
     * @param zkProp
     * @return
     */
    private static boolean createDataDir(Properties zkProp) {
        String dataDir = zkProp.getProperty("dataDir");

        if (FileUtils.exists(dataDir)) {
            return true;
        }
        try {
            FileUtils.createDir(dataDir);
            return true;
        } catch (IOException e) {
            LOG.error(e);
        }
        return false;
    }

    /**
     * 创建数据日志目录
     * @param zkProp
     * @return
     */
    private static boolean createDataLogDir(Properties zkProp) {
        String dataDir = zkProp.getProperty("dataLogDir");

        if (FileUtils.exists(dataDir)) {
            return true;
        }
        try {
            FileUtils.createDir(dataDir);
            return true;
        } catch (IOException e) {
            LOG.error(e);
        }
        return false;
    }

    /**
     * 判断是不是集群模式
     * @param zkProp
     * @return true:是集群模式，false:单机模式
     */
    private static boolean isCluster(Properties zkProp) {
        boolean isCluster = false;
        for (Map.Entry<Object, Object> entry : zkProp.entrySet()) {
            if (entry.getKey().toString().startsWith("server.")) {
                isCluster = true;
                break;
            }
        }
        return isCluster;
    }

    /**
     * 单机模式启动
     * @param zkProp
     * @return
     */
    public static boolean createServerStandalone(Properties zkProp) {
        LOG.info("单机模式启动zookeeper, 配置:{}", zkProp);
        QuorumPeerConfig quorumConfig = new QuorumPeerConfig();
        try {
            quorumConfig.parseProperties(zkProp);
            ZooKeeperServerMain zkServer = new ZooKeeperServerMain();
            ServerConfig config = new ServerConfig();
            config.readFrom(quorumConfig);
            zkServer.runFromConfig(config);

            return true;
        } catch (Exception e) {
            LOG.error("单机模式启动异常", e);
        }
        return false;
    }

    /**
     * 集群模式启动
     * @param zkProp
     * @return
     */
    public static boolean createServerCluster(Properties zkProp) {
        LOG.info("集群模式启动zookeeper, 配置:{}", zkProp);
        try {
            QuorumPeerConfig quorumConfig = new QuorumPeerConfig();
            quorumConfig.parseProperties(zkProp);
            QuorumPeerMain peer = new QuorumPeerMain();
            peer.runFromConfig(quorumConfig);
            return true;
        } catch (Exception e) {
            LOG.error("集群模式启动异常", e);
        }
        return false;
    }

    /**
     * 加载zookeeper的配置，我们定义的默认路径是：./config/zoo.cfg
     * 因为springboot的配置文件路径存放在
     * @return
     */
    public static Properties getPropertiesConfigDefault() {
        try {
            Properties properties = PropertiesUtils.readPropertiesFromFile("./conf/zoo.cfg");
            return properties;
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    /**
     * 加载zookeeper的配置，我们定义的默认路径是：./config/zoo.cfg
     * 因为springboot的配置文件路径存放在
     * @return
     */
    public static Properties getPropertiesConfigSpringboot() {
        try {
            Properties properties = PropertiesUtils.readPropertiesFromFile("./config/zoo.cfg");
            return properties;
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    /**
     * 创建的客户端
     * @param sessionTimeout 超时
     * @param watcher 节点变化时的回调方法
     * @param socketStrs 可以多个，"127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183"
     * @return
     */
    public static ZooKeeper createClient(int sessionTimeout, Watcher watcher, String... socketStrs) {
        String connectString = StringUtils.join(socketStrs, ",");
        ZooKeeper zookeeper = null;
        try {
            zookeeper = new ZooKeeper(connectString, sessionTimeout, watcher);
        } catch (Exception e) {
            LOG.error("创建zookeeper客户端失败,连接的sokects:{}", e, connectString);
        }

//        打印最后的状态
        printState(connectString, zookeeper);

        return zookeeper;
    }

    /**
     * 只是打印一下连接状态
     * @param connectString 连接信息
     * @param zk
     */
    private static void printState(String connectString, ZooKeeper zk) {
        if (zk != null) {
            ZooKeeper.States state = zk.getState();
            if (state.isAlive()) {
                LOG.info("[{}]的zookeeper连接状态:[{}]", connectString, state);
            } else {
                LOG.error("[{}]的zookeeper连接状态:[{}]", connectString, state);
            }
        } else {
            LOG.error("[{}]的zookeeper连接状态:[{}]", connectString, "失败");
        }
    }

    /**
     * 关闭客户端
     * @param zookeeper
     */
    public static void closeClient(ZooKeeper zookeeper) {
        if (zookeeper != null) {
            try {
                zookeeper.close();
            } catch (Exception e) {
                LOG.error(e);
            }
        }
    }

    /**
     * @param zookeeper
     * @param path 删除节点的路径
     * @param version 删除节点的版本，如果是 -1，那么就会无视版本好删除，可以通过调用queryNodeStat获取到最新的版本号，更新的时候就传进来，如果版本号不一致，那么就重新获取再进行更新
     * @return
     */
    public static boolean deletePath(ZooKeeper zookeeper, final String path, int version) {
        boolean status = false;
        try {
            zookeeper.delete(path, version);
            status = true;
        } catch (Exception e) {
            LOG.error("删除节点[{}]-版本[{}]出现异常", e, path, version);
            status = false;
        }
        LOG.info("删除节点路径[{}]-版本[{}], 状态[{}]", path, version, status);
        return status;
    }

    /**
     * @param zookeeper
     * @param path 删除节点的路径
     * @return
     */
    public static boolean deletePath(ZooKeeper zookeeper, final String path) {
        return deletePath(zookeeper, path, -1);
    }

    /**
     * 创建节点
     * 注意如果是自动递增序号节点类型，系统会自动添加一个递增序号在路径后面，所以返回参数是一个路径的真实路径，假如不是自动递增序号节点，返回的就是
     * @param zookeeper
     * @param path 节点路径
     * @param data 节点数据
     * @param acl 权限控制类型, 请使用:{@link ZooDefs.Ids}, 如不进行ACL权限控制就使用：ZooDefs.Ids.OPEN_ACL_UNSAFE
     * @param createMode 节点模式，请使用{@link CreateMode},
     *                  如：
     *                   永久性节点为：{@link CreateMode.PERSISTENT}，
     *                   永久性自动递增序号节点为：{@link CreateMode.PERSISTENT_SEQUENTIAL},
     *                   临时节点：{@link CreateMode.EPHEMERAL}
     *                   临时自动递增序号节点：{@link CreateMode.EPHEMERAL_SEQUENTIAL}
     *
     * @return 创建的节点的真实路径，如果创建失败，返回null,注意如果是自动递增序号节点类型，系统会自动添加一个递增序号在路径后面，所以返回参数是一个路径的真实路径，假如不是自动递增序号节点，返回的就是入参path
     */
    @SuppressWarnings("JavadocReference")
    public static String createPath(ZooKeeper zookeeper, final String path, byte data[], List<ACL> acl,
                                    CreateMode createMode) {

        boolean status = false;
        String actualPath = null;
        try {
            actualPath = zookeeper.create(path, data, acl, createMode);
            status = true;
        } catch (Exception e) {
            LOG.error("创建路径节点[{}]出现异常", e, path);
            status = false;
        }
        LOG.info("创建节点路径[{}], 状态[{}]", path, status);
        return actualPath;
    }

    /**
     * 更新路径
     * @param zookeeper
     * @param path 更新节点的路径
     * @param data 更新的数据写进路径
     * @param version 更新节点的版本，如果是 -1，那么就会无视版本好删除，可以通过调用queryNodeStat获取到最新的版本号，更新的时候就传进来，如果版本号不一致，那么就重新获取再进行更新；
     * @return
     */
    public static Stat updatePath(ZooKeeper zookeeper, final String path, byte data[], int version) {
        boolean status = false;
        Stat stat = null;
        try {
            stat = zookeeper.setData(path, data, version);
            status = true;
        } catch (Exception e) {
//            抛了异常，说明操作失败，建议客户端要进行尝试操作
            LOG.error("更新节点[{}]-版本[{}]出现异常", e, path, version);
            status = false;
        }
        LOG.info("更新节点路径[{}]-版本[{}], 状态[{}]", path, version, status);
        return stat;
    }

    /**
     * 更新路径
     * @param zookeeper
     * @param path 更新节点的路径
     * @param data 更新的数据写进路径
     * @return
     */
    public static Stat updatePath(ZooKeeper zookeeper, final String path, byte data[]) {
        return updatePath(zookeeper, path, data, -1);
    }

    /**
     * 查询一个节点的子节点名字列表
     * @param zookeeper
     * @param path
     * @param isCallWatch 是否调用回调方法
     * @return 如果异常就返回null，返回的子的名字，当前层的名字，不带父路径，跟在命令行客户端的 'ls' 命令返回的效果一样
     */
    public static List<String> getChildrenNames(ZooKeeper zookeeper, final String path, boolean isCallWatch) {
        try {
            List<String> childrenNames = zookeeper.getChildren(path, isCallWatch);

            return childrenNames;
        } catch (Exception e) {
            LOG.error("查询节点的子节点路径[{}]出现异常", e, path);
        }
        return null;
    }

    /**
     * 查询一个节点的子节点名字列表
     * @param zookeeper
     * @param path
     * @param isCallWatch 是否调用回调方法
     * @return 如果异常就返回null，返回的子的路径，当前层的路径，带父路径
     */
    public static List<String> getChildrenPaths(ZooKeeper zookeeper, final String path, boolean isCallWatch) {
        List<String> childrenNames = getChildrenNames(zookeeper, path, isCallWatch);
        if (childrenNames == null) {
            return null;
        }
        List<String> childrenPaths = new ArrayList<>();

        for (String childrenName : childrenNames) {
            childrenPaths.add(concatPath(path, childrenName));
        }
        return childrenPaths;
    }

    /**
     * 连接两个路径
     * @param parentPath 父路径
     * @param subPath 子路径
     * @return 利用 / 符号进行拼接
     */
    public static String concatPath(String parentPath, String subPath) {
        if (StringUtils.isBlank(parentPath)) {
            return subPath;
        }

        if (StringUtils.isBlank(subPath)) {
            return parentPath;
        }

        return StringUtils.join(parentPath, "/", subPath);
    }

    /**
     * 查询一个节点的信息
     * @param zookeeper
     * @param path
     * @param watch
     * @param stat 指定节点的版本状态信息，可以通过调用queryNodeStat获取到最新的版本号，更新的时候就传进来，如果版本号不一致，那么就重新获取再进行更新
     * @return
     */
    public static byte[] getData(ZooKeeper zookeeper, String path, boolean watch, Stat stat) {
        boolean status = false;
        byte[] data = null;
        try {
            data = zookeeper.getData(path, watch, stat);
            status = true;
        } catch (Exception e) {
            LOG.error("查询数据,节点路径[{}]-版本[{}]出现异常", e, path, stat);
            status = false;
        }
        LOG.debug("查询数据,节点路径[{}]-版本[{}], 状态[{}]", path, stat, status);
        return data;
    }

    /**
     * 查询一个节点的信息
     * 不指定版本状态，拿最新的
     * @param zookeeper
     * @param path
     * @return
     */
    public static byte[] getData(ZooKeeper zookeeper, String path) {
        return getData(zookeeper, path, false, null);
    }

    /**
     * 查询节点的状态信息，包括版本，创建事件等
     *
     版本号
     对节点的每一次操作都将致使整个节点的版本号增加。每个节点维护着三个版本号，他们分别是:
     version(节点数据版本号），cversion(子节点版本号），avevsion(节点所拥有的ACL版本号）

     通过版本号，ZooKeeper实现了更新的乐观锁4，当版本号不相符时，
     则表示待更新的节点已经被其他客户端提前更新了，而当前的整个更新操作将全部失败。
     当然所有的一切ZooKeeper已经为开发者提供了保障，我们需要做的只是调用API。
     与此同时，随着分布式应用的的不断深入，需要对集群管理逐步透明化监控集群和作业状态，
     可以充分利ZK的独有特性。
     * @param zookeeper
     * @param path
     * @param watch 是否调用回调函数
     * @return
     */
    public static Stat queryNodeStat(ZooKeeper zookeeper, String path, boolean watch) {
        boolean status = false;
        Stat stat = null;
        try {
            stat = zookeeper.exists(path, watch);
            status = true;
        } catch (Exception e) {
            LOG.error("查询状态,节点路径[{}]出现异常", e, path);
            status = false;
        }
        LOG.debug("查询状态,节点路径[{}]-版本[{}], 状态[{}]", path, stat, status);
        return stat;
    }

    /**
     * 查询节点的状态信息，包括版本，创建事件等
     * @param zookeeper
     * @param path
     * @return
     */
    public static Stat queryNodeStat(ZooKeeper zookeeper, String path) {
        return queryNodeStat(zookeeper, path, false);
    }
}
