package com.latico.commons.net.cmdclient;

import com.latico.commons.net.cmdclient.bean.EnableModel;
import com.latico.commons.net.cmdclient.enums.CmdClientTypeEnum;
import com.latico.commons.net.cmdclient.impl.SshCmdClient;
import com.latico.commons.net.cmdclient.impl.SysLocalCmdClient;
import com.latico.commons.net.cmdclient.impl.TelnetCmdClient;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;


/**
 * <PRE>
 * 连接器工厂根据类型判断用哪种类型的连接器
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年3月20日</B>
 * @author    <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @since     <B>JDK1.6</B>
 */
public class CmdClientFactory {
	/** 日志 */
	private static final Logger LOG = LoggerFactory.getLogger(CmdClientFactory.class);
	
	public static CmdClientFactory getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public static boolean isEnableModel(String manufacturer, String model) {
		EnableModel enableModel = CmdClientConfig.getInstance().getEnableModel(manufacturer, model);
		return enableModel.isEnable();
	}

	/**
	 * 拿到enable密码
	 * @param manufacturer
	 * @param model
	 * @return
	 */
	public static String getEnablePwd(String manufacturer, String model) {
		EnableModel enableModel = CmdClientConfig.getInstance().getEnableModel(manufacturer, model);
		return enableModel.getEnablePwd();
	}
	public static EnableModel getEnableModel(String manufacturer, String model) {
		EnableModel enableModel = CmdClientConfig.getInstance().getEnableModel(manufacturer, model);
		return enableModel;
	}

	private static class SingletonHolder {
		private static final CmdClientFactory INSTANCE = new CmdClientFactory();
	}
	
	/**
	 * 获取命令行连接客户端
	 * @param type 命令行类型 有Telnet、SSH、本地系统命令行
	 * @return 统一的对外使用接口对象
	 */
	public static CmdClient getCmdClient(CmdClientTypeEnum type){
		CmdClient cmdClient = null;
		
		if(CmdClientTypeEnum.Telnet == type){
			cmdClient = getTelnetClient();
			
		}else if(CmdClientTypeEnum.SSH == type){
			cmdClient = getSshClient();
			
		}else if(CmdClientTypeEnum.SystemCmdLine == type){
			cmdClient = getSysLocalCmdClient();
			
		}else{
			LOG.warn("没有找到指定的连接类型 [{}]，采用默认使用Telnet方式", type);
			cmdClient = getTelnetClient();
		}
		
		return cmdClient;
	}
	
	public static CmdClient getTelnetClient(){
		return new TelnetCmdClient();
	}
	
	public static CmdClient getSshClient(){
		return new SshCmdClient();
	}
	
	public static CmdClient getSysLocalCmdClient(){
		return new SysLocalCmdClient();
	}
	
	/**
	 * 默认是telnet方式
	 * @param cmdType 连接类型
	 * @return
	 */
	public static CmdClient getCmdClient(String cmdType){
		CmdClientTypeEnum connectType = getCmdType(cmdType);
		
		return getCmdClient(connectType);
	}
	
	/**
	 * 
	 * @param cmdType
	 * @return
	 */
	public static CmdClientTypeEnum getCmdType(String cmdType) {
		if(StringUtils.isEmpty(cmdType)){
			return CmdClientTypeEnum.Telnet;
		}
		
		CmdClientTypeEnum connectType = null;
		if(StringUtils.isNumber(cmdType)){
			int cmdTypeId = Integer.parseInt(cmdType);
			connectType = CmdClientTypeEnum.getEnumById(cmdTypeId);
			
		}else{
			connectType = CmdClientTypeEnum.getEnumByName(cmdType);
		}
		if(connectType == null){
			return CmdClientTypeEnum.Telnet;
		}
		return connectType;
	}
	
	/**
	 * 默认是telnet方式
	 * @param cmdType 连接类型
	 * @return
	 */
	public static CmdClient getCmdClient(int cmdType){
		
		CmdClientTypeEnum connectType = CmdClientTypeEnum.getEnumById(cmdType);
		return getCmdClient(connectType);
	}
	
	public static CmdClient getCmdClientWithLogin(int cmdType, String ip, String username, String password){
		return getCmdClientWithLogin(cmdType, ip, -1, username, password);
	}
	
	public static CmdClient getCmdClientWithLogin(int cmdType, String ip, int port, String username, String password){
		CmdClient client = getCmdClient(cmdType);
		client.login(ip, port, username, password);
		if(!client.isLogin()){
//			LOG.error("登陆类型:{} 登陆 {}失败", client.getCmdClientType().getName(), client.getLoginInfo());
			client.close();
		}
		return client;
	}
	
	public static CmdClient getCmdClientWithLogin(String cmdType, String ip, String username, String password){
		return getCmdClientWithLogin(cmdType, ip, -1, username, password);
	}
	
	public static CmdClient getCmdClientWithLogin(String cmdType, String ip, int port, String username, String password){
		CmdClient client = getCmdClient(cmdType);
		client.login(ip, port, username, password);
		if(!client.isLogin()){
//			LOG.error("登陆类型:{} 登陆 {}失败", client.getCmdClientType().getName(), client.getLoginInfo());
			client.close();
		}
		return client;
	}
	
	/**
	 * 登录，获取命令行客户端连接
	 * @param cmdType 命令类型，telnet或者SSH，或者使用数字，0是telnet，1是ssh，3是本地命令行
	 * @param ip 登录设备的IP
	 * @param port 登录设备的端口
	 * @param username 用户名
	 * @param password 密码
	 * @param isEnable 是否执行enable命令
	 * @param enablePwd 执行了enable命令的情况下，如果需要输入密码
	 * @return
	 */
	public static CmdClient getCmdClientWithLogin(String cmdType, String ip, int port,
                                                  String username, String password, boolean isEnable, String enablePwd){
		CmdClient client = getCmdClient(cmdType);
		client.login(ip, port, username, password, isEnable, enablePwd);
		if(!client.isLogin()){
			client.close();
		}
		return client;
	}
	
	/**
	 * 登录，获取命令行客户端连接
	 * 登录的时候使用厂家型号，程序会根据指定的厂家自动执行返回报文一次性返回模式，
	 * 还有自动判断是否执行enable模式，
	 * 可能存在问题：由于自动执行enable模式，假如设备的出厂enable密码被修改了，那就会报错。
	 * @param cmdType 命令类型，telnet或者SSH，或者使用数字，0是telnet，1是ssh，3是本地命令行
	 * @param ip 登录设备的IP
	 * @param port 登录设备的端口
	 * @param username 用户名
	 * @param password 密码
	 * @param manufacturer 指定登录的厂家
	 * @param model 指定登录的型号
	 * @return
	 */
	public static CmdClient getCmdClientWithLogin(String cmdType, String ip, int port,
                                                  String username, String password, String manufacturer, String model){
		CmdClient client = getCmdClient(cmdType);
		client.login(ip, port, username, password, manufacturer, model);
		if(!client.isLogin()){
			client.close();
		}
		return client;
	}
	

}
