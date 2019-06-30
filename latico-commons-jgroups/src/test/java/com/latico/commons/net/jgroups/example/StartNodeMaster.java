package com.latico.commons.net.jgroups.example;

import com.latico.commons.common.util.logging.LogUtils;
import com.latico.commons.net.jgroups.JGroupsUtils;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 
 * <pre>
 * tcp模式下:
 * 如果是同一台机器测试,请注意在
 * TCPPING 元素下修改 initial_hosts的配置端口:
 * 例如:"${jgroups.tcpping.initial_hosts:192.168.19.100[7800],192.168.19.100[7801]}
 * 如果是多台机器测试,请注意在
 * TCPPING 元素下修改 initial_hosts的ip,端口随意:
 * 例如:"${jgroups.tcpping.initial_hosts:192.168.19.100[7800],192.168.19.178[7800]}
 * 
 * udp模式下:
 * 同一台机器的不同端口(端口是动态的)可通信.
 * 不同机器之间的ip多播可能会受到一些因素限制而造成节点之间无法彼此发现.
 * </pre>
 *
 * @author F.Fang
 * @version $Id: StartNodeMaster.java, v 0.1 2014年10月15日 上午5:31:32 F.Fang Exp $
 */
public class StartNodeMaster {

    public static void main(String[] args) {
        LogUtils.loadLogBackConfigDefault();
//        JGroupsNodeExample node = new JGroupsNodeExample();
        JGroupsNodeExample node = null;
        try {
            node = JGroupsUtils.createNodeByResourceXmlConfigFile(JGroupsNodeExample.class, "/config/jgroups-udp.xml", "cluster1", "node1");

            Set<String> stateSyncData = new HashSet<>();

            stateSyncData.add("1");
            stateSyncData.add("2");
            stateSyncData.add("3");
            node.initSyncStateData(stateSyncData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 使用控制台发送消息给Node2.
        Scanner scanner = new Scanner(System.in);
        while(true){
            String text = scanner.next();
            if("exit".equals(text)){
                break;
            }
            node.sendMsg(null,text);


        }

    }

}