package com.latico.commons.net.jgroups;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.net.Ipv4Utils;
import org.jgroups.*;
import org.jgroups.util.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <PRE>
 * 抽象节点， 消息接收通过继承ReceiverAdapter或者实现Receiver，节点会话通道通过创建一个JChannel
 * 1、连接集群的时候，假如集群不存在，那么就会创建一个，创建集群的那个节点就会成为集群创建者；
 * 2、如果集群创建者退出集群，那么其他节点会选任新的节点作为集群创建者；
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-10 18:20
 * @Version: 1.0
 */
public abstract class AbstractJGroupsNode<SYNC_DATA> extends ReceiverAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractJGroupsNode.class);

    /**
     * 同步数据的时候的超时时间，单位毫秒
     */
    private long syncTimeout = 15000;
    /**
     * 集群创建者地址,主机名或者IP
     */
    private volatile String creatorHost;
    private volatile Address creator;
    private volatile InetSocketAddress creatorInetSocketAddress;

    /**
     * 集群创建者端口
     */
    private volatile int creatorPort;

    /**
     * 集群名称
     */
    protected String clusterName;

    /**
     * 节点名称
     */
    protected String nodeName;

    /**
     * 集群节点会话信道，我们把它定义成节点持有者，用于发送数据获取节点信息等
     */
    protected JChannel nodeHolder;

    /**
     * 节点间初始化的同步数据.
     */
    protected SYNC_DATA stateSyncData;

    /**
     * 状态数据同步锁
     */
    protected ReentrantLock stateSyncDataLock = new ReentrantLock();


    public AbstractJGroupsNode() {
    }

    public AbstractJGroupsNode(String clusterName, String nodeName) {

        this.clusterName = clusterName;
        this.nodeName = nodeName;
    }

    @Override
    public void viewAccepted(View view) {
        //赋值创建者
        try {
//            获取集群创建者节点地址，也就是主节点
            Address creator = view.getCreator();
            updateCreatorInfo(creator);

            if (this.nodeHolder != null) {
                LOG.info("集群视图变更:当前节点名称:[{}],当前节点地址:[{}],集群创建者:[{}],集群主节点:[{}-{}], 当前节点是否主节点:[{}],集群所有成员:[{}]", this.nodeName, this.nodeHolder.getAddressAsString(), view.getCreator(), getMasterNodeHostString(), getMasterNodePort(), isMasterNode() ? "是" : "否", view.getMembers());
            } else {
                LOG.info("集群视图变更:当前节点名称:[{}],当前节点地址:[{}],集群创建者:[{}],集群主节点:[{}-{}], 当前节点是否主节点:[{}],集群所有成员:[{}]", this.nodeName, Ipv4Utils.getLocalHostAddress(), view.getCreator(), getMasterNodeHostString(), getMasterNodePort(), isMasterNode() ? "是" : "否", view.getMembers());
            }

            //处理集群视图改变事件
            handleClusterChange(view);
        } catch (Exception e) {
            LOG.error("集群视图变更事件处理发生异常:{}", e, clusterName);
        }

    }

    /**
     * 从节点调用了读取同步状态方法org.jgroups.JChannel#getState(org.jgroups.Address, long)方法后，
     * 会从主节点的com.latico.commons.jgroups.AbstractJGroupsNode#getState(java.io.OutputStream)方法中拿到主节点的数据，
     * 然后通过com.latico.commons.jgroups.AbstractJGroupsNode#setState(java.io.InputStream)方法进行从节点接收数据处理
     *
     * @param input
     */
    @Override
    public void setState(InputStream input) {
        stateSyncDataLock.lock();
        boolean status = false;
        try {
            LOG.info("节点[{}][{}]-从节点开始从主节点接收数据...", clusterName, nodeHolder.getAddressAsString());
            //从节点从主节点拿回的数据，转化成跟主节点格式一样
            SYNC_DATA syncData = (SYNC_DATA) Util.objectFromStream(new DataInputStream(input));
            if (syncData != null) {
                this.stateSyncData = syncData;
                status = true;
            }

        } catch (Exception e) {
            LOG.error("从主节点同步数据到从节点发生异常!", e);
        } finally {
            stateSyncDataLock.unlock();
        }
        if (status) {
            LOG.info("节点[{}][{}]-从节点从主节点接收数据完成,状态:正常", clusterName, nodeHolder.getAddressAsString());
        } else {
            LOG.error("节点[{}][{}]-从节点从主节点接收数据完成,状态:异常", clusterName, nodeHolder.getAddressAsString());
        }
        LOG.debug("节点[{}][{}]-同步数据结果:{}", clusterName, nodeHolder.getAddressAsString(), this.stateSyncData);

    }

    /**
     * 从节点会从主节点同步数据，就是调用了该方法，stateSyncData就是要从主节点读取的对象数据
     *
     * @param output
     */
    @Override
    public void getState(OutputStream output) {
        boolean status = false;
        stateSyncDataLock.lock();
        try {
            if (stateSyncData != null) {
                LOG.info("节点[{}][{}]-主节点开始发送数据给从节点", clusterName, nodeHolder.getAddressAsString());
                //把主节点的stateSyncData数据传输给请求同步数据的从节点，数据量过大可能会造成节点的状态同步时间过长.
                Util.objectToStream(stateSyncData, new DataOutputStream(output));
                status = true;
            }
        } catch (Exception e) {
            LOG.error("主节点发送数据给从节点发生异常", e);
            status = false;
        } finally {
            stateSyncDataLock.unlock();
        }
        if (status) {
            LOG.info("节点[{}][{}]-主节点发送数据给从节点完成,状态:正常", clusterName, nodeHolder.getAddressAsString());
        } else {
            LOG.error("节点[{}][{}]-主节点发送数据给从节点完成,状态:异常", clusterName, nodeHolder.getAddressAsString());
        }

    }

    /**
     * 初始化同步数据，一般是主节点,初始化的时候，把初始化数据上传到集群
     *
     * @param stateSyncData
     * @return
     */
    public boolean initSyncStateData(SYNC_DATA stateSyncData) {
        return initSyncStateData(stateSyncData, syncTimeout);
    }

    /**
     * 初始化同步数据，一般是主节点,初始化的时候，把初始化数据上传到集群
     * 该方法可以调用，按需使用
     *
     * @param stateSyncData
     * @return
     */
    public boolean initSyncStateData(SYNC_DATA stateSyncData, long timeout) {
        stateSyncDataLock.lock();
        try {
            if (stateSyncData != null) {
                this.stateSyncData = stateSyncData;
            }
            return syncStateData(null, timeout);
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            stateSyncDataLock.unlock();
        }
        return true;
    }

    /**
     * 同步状态数据,把当前同步数据节点数据发送到集群上通知其他节点
     *
     * @return true:同步成功
     */
    public boolean syncStateData() {
        return syncStateData(null, syncTimeout);
    }

    /**
     * 同步状态数据,把当前同步数据节点数据发送到集群上通知其他节点
     *
     * @return true:同步成功
     */
    private boolean syncStateData(Address target, long timeout) {
        boolean status = false;
        if (nodeHolder != null) {
            if (isMasterNode()) {
                LOG.info("节点[{}][{}]-是主节点,不需要进行同步数据", clusterName, nodeHolder.getAddressAsString());
                status = true;
            } else {
                try {
                    LOG.info("节点[{}][{}]-开始从主节点同步数据...", clusterName, nodeHolder.getAddressAsString());
                    if (timeout <= 0) {
                        timeout = syncTimeout;
                    }
                    nodeHolder.getState(target, timeout);
                    status = true;
                } catch (Exception e) {
                    //从节点第一次进行数据同步会报超时异常(原因未明)，但是setState方法能成功调用完成，XXX org.jgroups.StateTransferException: timeout during state transfer
                    LOG.error("从主节点同步数据到从节点发生异常!", e);
                    status = false;
                }
            }

        }
        if (status) {
            LOG.info("节点[{}][{}]-执行同步方法完成,状态:正常", clusterName, nodeHolder.getAddressAsString());
        } else {
            LOG.error("节点[{}][{}]-执行同步方法完成,状态:异常", clusterName, nodeHolder.getAddressAsString());
        }

        return status;
    }


    /**
     * 定义判断当前节点是不是主节点
     *
     * @return true:是主节点
     */
    public boolean isMasterNode() {
        return JGroupsUtils.equals(creator, nodeHolder.getAddress());
    }


    /**
     * 获取主节点地址，目前是使用集群当前创建者
     * 如果主节点挂了，那么排在最前面的从节点会变成集群创建者和集群主节点，
     * 我们把集群创建者和集群主节点一直会保持是属于同一个节点
     * @return
     */
    public String getMasterNodeHostString() {
        return creatorHost;
    }

    /**
     * 获取主节点地址，目前是使用集群当前创建者
     *
     * @return
     */
    public int getMasterNodePort() {
        return creatorPort;
    }

    /**
     * <pre>
     * 发送消息给目标地址.
     * </pre>
     *
     * @param dest 为空表示发给所有节点.
     * @param msg  消息.
     */
    public boolean sendMsg(Address dest, Object msg) {
        boolean status = false;
        Message message = new Message(dest, msg);
        try {
            LOG.debug("节点[{}][{}]-开始发送数据:{}", clusterName, nodeHolder.getAddressAsString(), message);
            nodeHolder.send(message);
            status = true;
        } catch (Exception e) {
            LOG.error("消息发送失败!", e);
            status = false;
        }
        LOG.info("节点[{}][{}]-发送数据完成,发送状态:{}", clusterName, nodeHolder.getAddressAsString(), status ? "正常" : "异常");
        handleSendMsg(dest, msg, status);
        return status;
    }

    /**
     * 处理发送消息结果
     *
     * @param dest   目的地
     * @param msg    消息
     * @param status true:发送成功
     */
    protected abstract void handleSendMsg(Address dest, Object msg, boolean status);

    @Override
    public void receive(Message msg) {

        //子类处理业务逻辑
        try {
            LOG.info("节点[{}][{}]接收到消息,报文简要:[{}]", clusterName, nodeHolder.getAddressAsString(), msg.toString());
            handleReceiveMsg(msg);
        } catch (Exception e) {
            LOG.error("集群接收数据处理事件发生异常:{}", e, clusterName);
        }
    }

    /**
     * 判断是不是当前节点
     *
     * @param address 被判断的地址信息
     * @return
     */
    public boolean isCurrentNode(Address address) {
        return JGroupsUtils.equals(address, nodeHolder.getAddress());
    }

    /**
     * 处理接收到的报文，子类处理业务逻辑
     *
     * @param msg 收到的报文
     */
    protected abstract void handleReceiveMsg(Message msg);

    /**
     * 处理集群视图改变事件，当集群有新节点进来或者有节点退出都会触发该事件
     *
     * @param view 集群网络最新视图
     */
    protected abstract void handleClusterChange(View view);

    /**
     * 更新创建者信息
     *
     * @param creator
     */
    private synchronized void updateCreatorInfo(Address creator) {
        this.creator = creator;
        this.creatorInetSocketAddress = JGroupsUtils.getInetSocketAddress(creator);
        if (creatorInetSocketAddress != null) {
            creatorHost = creatorInetSocketAddress.getHostString();
            creatorPort = creatorInetSocketAddress.getPort();
        }
    }

    /**
     * 关闭接收自己发送给自己的消息
     */
    public void closeReceiveOwnMessages() {
        this.nodeHolder.setDiscardOwnMessages(true);
    }

    /**
     * 开启接收自己发送给自己的消息
     */
    public void openReceiveOwnMessages() {
        this.nodeHolder.setDiscardOwnMessages(false);
    }

    /**
     * 是否接收自己发送给自己的消息
     */
    public void isReceiveOwnMessages() {
        this.nodeHolder.getDiscardOwnMessages();
    }

    /**
     * 关闭节点
     */
    public void close() {
        try {
            if (this.nodeHolder != null) {
                this.nodeHolder.close();
            }

        } catch (Exception e) {
            LOG.error(e);
        }
        this.stateSyncData = null;
    }

    public JChannel getNodeHolder() {
        return nodeHolder;
    }

    public void setNodeHolder(JChannel nodeHolder) {
        this.nodeHolder = nodeHolder;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public SYNC_DATA getStateSyncData() {
        return stateSyncData;
    }

    public void setStateSyncData(SYNC_DATA stateSyncData) {
        this.stateSyncData = stateSyncData;
    }

    public String getCreatorHost() {
        return creatorHost;
    }

    public void setCreatorHost(String creatorHost) {
        this.creatorHost = creatorHost;
    }

    public int getCreatorPort() {
        return creatorPort;
    }

    public void setCreatorPort(int creatorPort) {
        this.creatorPort = creatorPort;
    }

}
