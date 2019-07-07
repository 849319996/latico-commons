
package com.latico.commons.net.cmdclient.impl;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.envm.LineSeparator;
import com.latico.commons.net.cmdclient.AbstractCmdClient;
import com.latico.commons.net.cmdclient.CmdClientConfig;
import com.latico.commons.net.cmdclient.enums.CmdClientTypeEnum;
import com.latico.commons.net.cmdclient.enums.KeyboardValue;
import com.latico.commons.net.cmdclient.enums.TerminalTypeEnum;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.PrintStream;


/**
 * <PRE>
 *  telnet客户端类，里面提供连接，发指令、获取返回信息，断开连接
 * 1.telnet有VT100 VT102 VT220 VTNT ANSI LINUX等协议。
 *  默认VT100，win telnet linux时，有乱码，则改用VT200
 * 2.vt100控制码(ansi控制码)过滤的问题,可以过滤，也可以在服务设置不要。
 * 	不过滤都是一些乱码。是以\033[***一个字母结尾的格式。
 * 3.中文乱码的问题。
 * </PRE>
 * @project   <B>项目：VPDN-Ping</B>
 * @copyright <B>技术支持：广东凯通软件开发技术有限公司 (c)</B>
 * @version   <B>V1.0 2016年3月31日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class TelnetCmdClient extends AbstractCmdClient {

	/** 日志 */
	private static final Logger LOG = LoggerFactory.getLogger(TelnetCmdClient.class);

	/** apache commons TelnetClinet对象 */
	private TelnetClient telnetClient;

	public TelnetCmdClient() {
		this(TerminalTypeEnum.VT220, CharsetType.DEFAULT);
	}
	public TelnetCmdClient(TerminalTypeEnum terminalType, String dataCharset) {
		super(terminalType, dataCharset);
		remotePort = 23;
		cmdClientType = CmdClientTypeEnum.Telnet;
	}


	@Override
	public void releaseSubResources() {
		try {
			if (telnetClient != null){
				telnetClient.disconnect();
			}
		} catch (Exception e) {
			LOG.error(getLoginInfo() + "telnet关闭异常，telnet的socket连接可能未能关闭", e);
		}finally{
			telnetClient = null;
		}
	}
	
	/** 判断是否还连接着 */
	public boolean isConnected() {
		return this.telnetClient.isConnected();
	}

	public String getConnHostIp() {
		return remoteIp;
	}
	
	@Override
	public boolean connect(String ip, int port) {
		if(port >= 1){
			remotePort = port;
		}
		remoteIp = ip;
		 //触发端口映射
        portIpMap();
		try {
			telnetClient = new TelnetClient(terminalType.getName());
			telnetClient.setConnectTimeout(connTimeout);
			telnetClient.connect(remoteIp, remotePort);

			//apache的内部控制超时,会话超时
			telnetClient.setSoTimeout(CmdClientConfig.getInstance().getCmdSessionTimeout());
			dataReader = telnetClient.getInputStream();
			dataWriter = new PrintStream(telnetClient.getOutputStream());
			loginStatus = true;
		} catch (Exception e) {
			loginStatus = false;
			loginError = true;
			LOG.error("Telnet连接 [" + remoteIp + ":" + remotePort + "] 失败", e);
		}
		return loginStatus;
	}
	
	@Override
	public boolean connect(String ip) {
		return connect(ip, remotePort);
	}
	
	@Override
	public boolean login(String ip, int port, String username, String password,
	        boolean enable, String enablePwd) {
		if(port >= 1){
			remotePort = port;
		}
		remoteIp = ip;
		remoteUsername = username;
		remotePassword = password;
		this.enable = enable;
		this.enablePwd = enablePwd;
	
		if(StringUtils.isEmpty(remoteIp)){
			LOG.error("{} IP地址为空,不允许登录操作", getLoginInfo());
			loginStatus = false;
			return loginStatus;
		}

		//有些是不需要用户名
//		if(remoteUsername == null){
//			LOG.error("{} 用户名为空,不允许登录操作", getLoginInfo());
//			loginStatus = false;
//			return loginStatus;
//		}
		//有些是不需要密码
//		if(remotePassword == null){
//			LOG.error("{} 密码为空,不允许登录操作", getLoginInfo());
//			loginStatus = false;
//			return loginStatus;
//		}
		
		try {
			LOG.info("开始Telnet连接 {}", getLoginInfo());
			loginStatus = connect(remoteIp, remotePort);
			
			if(loginStatus){
				String returnData = null;
				
				returnData = readRawAutoAnswerNo(null, null, true, connTimeout);
				loginStatus = checkLoginProgressReturnDataIsSucc(returnData, false);
				
				if(loginStatus){
					//用户名不为空的时候才进行输入
					if(remoteUsername != null && !"".equals(remoteUsername)){
						LOG.info("{} 输入用户名：{}", remoteIp, remoteUsername);
						loginStatus = execCommand(remoteUsername);
						//如果发送用户名成功
						if(loginStatus){
							returnData = readRawAutoAnswerNo(null, null, true, connTimeout);
							loginStatus = checkLoginProgressReturnDataIsSucc(returnData, false);

						}
					}else{
						LOG.info("{} 用户名为空，不输入", remoteIp);
					}
				}
				
				if(loginStatus){
					if(remotePassword != null && !"".equals(remotePassword)){
						LOG.info("{} 输入密码：{}", remoteIp, remotePassword);
						closeInputToDetail();
						loginStatus = execCommand(remotePassword);
						openInputToDetail();
						addExecCmdToExecDetail("***" + LineSeparator.DEFAULT);
					}else{
						LOG.info("{} 密码为空，不输入", remoteIp);
						execCommand(KeyboardValue.ENTER);
					}

				}
				
				//执行跳过的步骤的命令
				if(loginStatus){
					executeSkipStepCmds();
				}
				
				//读取最后的结束符
				String loginResult = null;
				if(loginStatus){
					loginResult = readRawAutoAnswerNo(null, null, true, connTimeout);
					checkLoginProgressReturnDataIsSucc(loginResult, false);
				}
				if(loginStatus){
					loginStatus = execCommand(KeyboardValue.ENTER);
				}
				if(loginStatus){
					returnData = readRawAutoAnswerNo(null, null, true, connTimeout);
					loginStatus = checkLoginProgressReturnDataIsSucc(returnData, false);
					if(loginStatus){
						loginResult = loginResult + returnData;
					}
				}
				//校验登录状态
				checkLoginStatus(loginResult);
			
				//enable处理
				enableHandle(enable, enablePwd);
			}else{
				LOG.info("Telnet连接不上对端Socket[{}:{}]", remoteIp, remotePort);
			}
			
		} catch (Exception e) {
			loginStatus = false;
			loginError = true;
			LOG.error("Telnet连接" + getLoginInfo() + "发生异常", e);
			
		}finally{
			if(!loginStatus){
				close();
			}
			LOG.info("Telnet连接最终状态: {}", getLoginInfo());
		}
		
		return loginStatus;
	}
	
	@Override
	public void resetPtySize(){
		
	}
}
