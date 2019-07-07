package com.latico.commons.net.jgroups;

import com.latico.commons.common.util.string.StringUtils;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Receiver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;

/**
 * <PRE>
 * JGroups工具类
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-10 17:36
 * @Version: 1.0
 */
public class JGroupsUtils {

    /**
     * 通过资源配置文件，创建接收器
     * @param nodeClass
     * @param resourcesXmlConfFile xml配置文件，jgroups支持tcp/tcp-nio/udp
     * @param clusterName
     * @param nodeName
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T extends AbstractJGroupsNode> T createNodeByResourceXmlConfigFile(Class<T> nodeClass, String resourcesXmlConfFile, String clusterName, String nodeName) throws Exception {

        T node = nodeClass.newInstance();
        node.setClusterName(clusterName);
        node.setNodeName(nodeName);
        JChannel channel = createJChannelByResourceXmlConfigFile(node, clusterName, resourcesXmlConfFile);
        if (node.getNodeHolder() == null) {
            node.setNodeHolder(channel);
        }
        return node;

    }

    /**
     * 通过资源配置文件，创建接收器
     * @param nodeClass
     * @param confFilePath xml配置文件，jgroups支持tcp/tcp-nio/udp
     * @param clusterName
     * @param nodeName
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T extends AbstractJGroupsNode> T createNodeByXmlConfigFile(Class<T> nodeClass, String confFilePath, String clusterName, String nodeName) throws Exception {

        T node = nodeClass.newInstance();
        node.setClusterName(clusterName);
        node.setNodeName(nodeName);

        JChannel channel = createJChannelByXmlConfigFile(node, clusterName, confFilePath);
        if (node.getNodeHolder() == null) {
            node.setNodeHolder(channel);
        }

        return node;
    }

    /**
     * 通过资源文件创建会话通道
     * @param receiver          接收器
     * @param clusterName
     * @param resourcesConfFile xml配置文件，jgroups支持tcp/tcp-nio/udp
     * @return
     * @throws Exception
     */
    public static JChannel createJChannelByResourceXmlConfigFile(Receiver receiver, String clusterName, String resourcesConfFile) throws Exception {
        InputStream is = null;
        JChannel channel;
        try {
            //资源路径用绝对定位方式
            if (!resourcesConfFile.startsWith("/")) {
                resourcesConfFile = "/" + resourcesConfFile;
            }
            is = JGroupsUtils.class.getResourceAsStream(resourcesConfFile);
            channel = new JChannel(is);

            //如果是AbstractJGroupsNode，那可以提前把JChannel添加进去
            if (AbstractJGroupsNode.class.isAssignableFrom(receiver.getClass())) {
                AbstractJGroupsNode abstractJGroupsNode = (AbstractJGroupsNode)receiver;
                abstractJGroupsNode.setNodeHolder(channel);
            }

            //先设置接受者
            channel.setReceiver(receiver);

            //最后一步，连接集群
            channel.connect(clusterName);
        } catch (Exception e) {
            throw new Exception("连接JGroups节点异常!---" + clusterName, e);
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }

        }
        return channel;
    }

    /**
     * 同步系统文件的方式创建会话通道
     * @param clusterName
     * @param confFilePath xml配置文件，jgroups支持tcp/tcp-nio/udp
     * @param receiver
     * @return
     * @throws Exception
     */
    public static JChannel createJChannelByXmlConfigFile(Receiver receiver, String clusterName, String confFilePath) throws Exception {
        JChannel channel;
        try {
            channel = new JChannel(new File(confFilePath));

            //如果是AbstractJGroupsNode，那可以提前把JChannel添加进去
            if (AbstractJGroupsNode.class.isAssignableFrom(receiver.getClass())) {
                AbstractJGroupsNode abstractJGroupsNode = (AbstractJGroupsNode)receiver;
                abstractJGroupsNode.setNodeHolder(channel);
            }

            //先设置接受者
            channel.setReceiver(receiver);

            //最后一步，连接集群
            channel.connect(clusterName);
        } catch (Exception e) {
            throw new Exception("连接JGroups节点异常!---" + clusterName, e);
        }
        return channel;
    }

    public static InetSocketAddress getInetSocketAddress(Address address) {
        String str = address.toString();
        int last = str.lastIndexOf("-");


        String host = "";
        int port = 0;
        if (last != -1) {
            host = str.substring(0, last - 1);
            port = Integer.parseInt(str.substring(last + 1));
        }
        return InetSocketAddress.createUnresolved(host, port);
    }

    /**
     * 判断两个地址是否相等
     * @param address1
     * @param address2
     * @return
     */
    public static boolean equals(Address address1, Address address2) {
//        InetSocketAddress socketAddress1 = JGroupsUtils.getInetSocketAddress(address1);
//        InetSocketAddress socketAddress2 = JGroupsUtils.getInetSocketAddress(address2);
//
//        if (StringUtils.equals(socketAddress2.getHostName(), socketAddress1.getHostName()) && StringUtils.equals(socketAddress2.getPort(), socketAddress1.getPort())) {
//            return true;
//        }
//        return false;
        if (StringUtils.equals(address1, address2)) {
            return true;
        }
        return false;
    }
}
