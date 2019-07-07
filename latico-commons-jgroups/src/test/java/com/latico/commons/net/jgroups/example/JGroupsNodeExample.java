package com.latico.commons.net.jgroups.example;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.net.jgroups.AbstractJGroupsNode;
import org.jgroups.Address;
import org.jgroups.Message;
import org.jgroups.View;

import java.util.Set;

/**
 * <PRE>
 *
 * </PRE>
 * @Author: latico
 * @Date: 2019-01-11 22:58:16
 * @Version: 1.0
 */
public class JGroupsNodeExample extends AbstractJGroupsNode<Set<String>> {

    private static final Logger LOG = LoggerFactory.getLogger(JGroupsNodeExample.class);

    public JGroupsNodeExample() {

    }

    public JGroupsNodeExample(String clusterName, String nodeName) {
        super(clusterName, nodeName);
    }

    @Override
    protected void handleSendMsg(Address dest, Object msg, boolean status) {
        LOG.info("发送消息:{}, 状态:{}", msg, status);
    }

    @Override
    protected void handleReceiveMsg(Message msg) {
        //获取内容的时候，因为是泛型，所以必须先获取，再传到日志里面
        Object object = msg.getObject();
        LOG.info("收到内容:{}", object);

        stateSyncDataLock.lock();
        this.stateSyncData.add(object.toString());
        stateSyncDataLock.unlock();

        LOG.info("收到数据后同步完成状态数据:{}", this.stateSyncData);
    }

    @Override
    protected void handleClusterChange(View view) {
        LOG.info("收到集群视图变更:{}", view);
    }

}