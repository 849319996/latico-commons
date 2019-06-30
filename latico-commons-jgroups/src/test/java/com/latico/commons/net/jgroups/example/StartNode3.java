package com.latico.commons.net.jgroups.example;

import com.latico.commons.common.util.logging.LogUtils;
import com.latico.commons.net.jgroups.JGroupsUtils;

import java.util.Scanner;

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
 * @author F.Fang
 * @version $Id: StartNode2.java, v 0.1 2014年10月15日 上午5:31:44 F.Fang Exp $
 */
public class StartNode3 {

    public static void main(String[] args) {
        LogUtils.loadLogBackConfigDefault();
        JGroupsNodeExample node = null;
        try {
            node = JGroupsUtils.createNodeByResourceXmlConfigFile(JGroupsNodeExample.class, "/config/jgroups-udp.xml", "cluster1", "node3");
//            该方法可以调用，按需使用
            node.initSyncStateData(null, 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // 使用控制台发送消息给Node1.
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String text = scanner.next();
            if ("exit".equals(text)) {
                break;
            }
            node.sendMsg(null,text);
            node.syncStateData();
        }
        
    }

}