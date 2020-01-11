package com.latico.commons.zookeeper.zkclient;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.thread.ThreadUtils;
import com.latico.commons.zookeeper.ZookeeperUtils;
import com.latico.commons.zookeeper.client.zookeeper.ZookeeperClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import java.util.List;

public class ZookeeperClientTest {

    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperClientTest.class);

    @Test
    public void getZookeeperClient() {
        ZooKeeper zookeeper = null;
        try {
            ZookeeperClient zookeeperClient = new ZookeeperClient("127.0.0.1:2181");

            zookeeper = zookeeperClient.getZookeeperClient();

            LOG.info("Zk Status: " + zookeeper.getState());

            ZookeeperUtils.createPath(zookeeper, "/nodes", "节点集合".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            ZookeeperUtils.createPath(zookeeper, "/nodes/persistent", "持久节点".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            ZookeeperUtils.createPath(zookeeper, "/nodes/persistent_sequential1", "持久顺序节点1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
            ZookeeperUtils.createPath(zookeeper, "/nodes/persistent_sequential2", "持久顺序节点2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
            ZookeeperUtils.createPath(zookeeper, "/nodes/ephemeral", "临时节点".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            ZookeeperUtils.createPath(zookeeper, "/nodes/ephemeral_sequential1", "临时顺序节点1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            ZookeeperUtils.createPath(zookeeper, "/nodes/ephemeral_sequential2", "临时顺序节点2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            ZookeeperUtils.createPath(zookeeper, "/nodes/persistent/three", "临时顺序节点3".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            System.out.println(ZookeeperUtils.updatePath(zookeeper, "/nodes/persistent", "改变持久节点".getBytes()));

            List<String> childrenPaths = ZookeeperUtils.getChildrenPaths(zookeeper, "/nodes", true);
            for (String childrenPath : childrenPaths) {
                LOG.info("查询到节点: [" + childrenPath + ": " + new String(zookeeper.getData(childrenPath, true, null)) + "]");

                List<String> childrenPaths2 = ZookeeperUtils.getChildrenPaths(zookeeper, childrenPath, true);
                for (String childrenPath2 : childrenPaths2) {
                    ZookeeperUtils.deletePath(zookeeper, childrenPath2);
                }
                ZookeeperUtils.deletePath(zookeeper, childrenPath);
            }
            ZookeeperUtils.deletePath(zookeeper, "/nodes");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ZookeeperUtils.closeClient(zookeeper);
        }
    }

    /**
     *
     */
    @Test
    public void test(){
        ZookeeperClient zookeeperClient = new ZookeeperClient("127.0.0.1:2181");
        ZooKeeper zookeeper = null;
        zookeeper = zookeeperClient.getZookeeperClient();

        LOG.info("Zk Status: " + zookeeper.getState());

        ThreadUtils.sleepSecond(100000);
    }
}