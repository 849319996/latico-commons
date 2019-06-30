package com.latico.commons.net.cmdclient;

import static org.junit.Assert.*;

public class CmdClientFactoryTest {
    /**
     * 使用示例
     * @param args
     */
    public static void main(String[] args) {
//		Config.getInstance().init();
//		CmdClientTypeEnum connectType = CmdClientTypeEnum.SSH;
//		System.out.println(getCmdClient(0));
//		System.out.println(getCmdClient(1));
//		System.out.println(getCmdClient(2));
//		IConnecter connHelper = ConnecterFacotry.getInstance().getConnecter(connectType);
//		boolean succ = connHelper.login("172.168.10.7", "laticosoft", "laticosoft");
//		if(succ){
//			if(connHelper.sendCommand("ping 172.168.10.228 -c 4")){
//				String s = connHelper.readData(null);
//				System.out.println("提取后的数据为：\r\n" + s);
//			}
//		}
//		connHelper.logout();
//
//		String cmdType = "telnet";//模式：telnet、SSH
//		String ip = "";
//		int port = 0;
//		String username = "";
//		String password = "";
//		boolean isEnable = false; //是否是enable模式
//		String enablePwd = null; //是否需要输入enable密码
//
//		//只要isEnable为true或者enablePwd不为空其中一个条件满足，那就会在登录后主动输入enable模式。
//		CmdClient client = CmdClientFactory.getCmdClientWithLogin(cmdType, ip, port, username, password, isEnable, enablePwd);
        System.out.println(CmdClientFactory.getCmdType("0"));
        System.out.println(CmdClientFactory.getCmdType("1"));
        System.out.println(CmdClientFactory.getCmdType("ssh"));
        System.out.println(CmdClientFactory.getCmdType("telnet"));
    }
}