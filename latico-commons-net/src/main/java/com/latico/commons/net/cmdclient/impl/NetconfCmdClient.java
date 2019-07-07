
package com.latico.commons.net.cmdclient.impl;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.net.cmdclient.AbstractCmdClient;
import com.latico.commons.net.cmdclient.CmdClientConfig;
import com.latico.commons.net.cmdclient.enums.CmdClientTypeEnum;
import com.latico.commons.net.cmdclient.enums.TerminalTypeEnum;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.io.PrintStream;
import java.util.Hashtable;

/**
 * <PRE>
 * 注意：连接session成功表明登录成功，但是假如没有开放shell通道的，就会打开shell失败，
 * 但是因为已经登陆，所以采用session的连接状态作为连接状态（标志是否连接成功）
 * </PRE>
 * @project   <B>项目：VPDN-Ping</B>
 * @copyright <B>技术支持：latico</B>
 * @version   <B>V1.0 2016年3月31日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class NetconfCmdClient extends AbstractCmdClient {

	/** LOG 日志对象 */
	private static final Logger LOG = LoggerFactory.getLogger(NetconfCmdClient.class);

	/** jsch ssh 对象 */
	private JSch jsch;

	/** session 会话 */
	private Session session = null;

	/** channelShell 交互式通道的shell */
	private ChannelSubsystem channelShell;

	/** channelExec 执行脚本式的shell */
	@SuppressWarnings("unused")
	private ChannelExec channelExec;

	/**
	 * 加密和跟踪用户信息
	 */
	private UserInfo userInfo;
	
	/** defaultCutEndLineNum 默认截取最后面的行的数量 */
//	private int defaultCutEndLineNum = 2;
	
	public NetconfCmdClient() {
		this(TerminalTypeEnum.VT220, CharsetType.DEFAULT);
	}
	public NetconfCmdClient(TerminalTypeEnum terminalType, String dataCharset) {
		super(terminalType, dataCharset);
		remotePort = 830;
		cmdClientType = CmdClientTypeEnum.SSH;
	}
	
	/**
	 * <PRE>
	 * SSH用户相关信息，用于获取用户的相关合法性信息的判断，
	 * 登入SSH时的控制信息，设置不提示输入密码、不显示登入信息等
	 * MyUserInfo实现了接口UserInfo，主要是为获得运行执行的用户信息提供接口。大部分实现方法中，没有做实质性的工作，只是输出一下trace信息，帮助判断哪个方法被执行过。
	 * </PRE>
	 * <B>项	       目：</B>
	 * <B>技术支持：</B>
	 * @version   <B>V1.0 2017年3月20日</B>
	 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
	 * @since     <B>JDK1.6</B>
	 */
	public class DefaultUserInfo implements UserInfo {

		private String password;

		private String passphrase;

		private JTextField passphraseField = (JTextField) new JPasswordField(
		        20);

		public DefaultUserInfo() {
		}

		@Override
		public String getPassphrase() {

			return passphrase;
		}

		@Override
		public String getPassword() {

			return password;
		}

		@Override
		public boolean promptPassword(String message) {

			return true;
		}

		@Override
		public boolean promptPassphrase(String message) {

			Object[] ob = {passphraseField};
			int result = JOptionPane.showConfirmDialog(null, ob, message,
			        JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				passphrase = passphraseField.getText();
				return true;
			} else {
				return false;
			}
		}

		/**
		 * promptYesNo(final, String)是向用户提出一个yes或者no的问题，来决定是否允许连接远程主机。
		 * 这才是决定连接是否成功的一个关键函数。如果返回值为true. ，则允许连接；如果返回值为false，则拒绝连接。
		 */
		@Override
		public boolean promptYesNo(String message) {

			LOG.info(message);
			if (message != null
			        && message.contains("The authenticity of host")) {

				return true;
			} else {

				return false;
			}
		}

		@Override
		public void showMessage(String message) {

			JOptionPane.showMessageDialog(null, message);
		}

	}
	
//	@Override
//	public String readData(String endStr){
//		return readData(endStr, null, isLogSrcData, defaultCutEndLineNum);
//	}
	
	/**
	 * 判断是否连接
	 * 
	 * @return
	 */
	public boolean isConnected() {
		if (this.session != null) {
			loginStatus = this.session.isConnected();
		}
		return loginStatus;
	}

	@Override
	public void releaseSubResources() {
		if (null != channelShell) {
			channelShell.disconnect();
			channelShell = null;
		}
		if (null != session) {
			session.disconnect();
			session = null;
		}
	}


	
	public static void main(String[] args) {
//		CmdClient ssh = new TelnetCmdClient();
//		CmdClient ssh = new SshCmdClient();
//		ssh.login("10.1.12.1", "gzunicom", "Admin@123");
//		System.out.println("执行细节：【"+ssh.getExecDetail()+"】");
//		ssh.cleanExecDetail();
//		ssh.execCmdAndReceiveData("ping 1.1.1.1");
//		System.out.println("执行细节：【"+ssh.getExecDetail()+"】");
//		ssh.cleanExecDetail();
//		ssh.cleanExecDetail();
//		ssh.close();
		NetconfCmdClient ssh = new NetconfCmdClient();
		ssh.login("172.168.10.7", "laticosoft", "laticosoft");
		System.out.println("执行细节：【"+ssh.getExecDetail()+"】");
		ssh.cleanExecDetail();
		ssh.execCmdAndReceiveData("ping -c 4 1.1.1.1");
		System.out.println("执行细节：【"+ssh.getExecDetail()+"】");
		ssh.cleanExecDetail();
		ssh.execCmdAndReceiveData("ping -c 4 3.3.3.3");
		System.out.println("执行细节：【"+ssh.getExecDetail()+"】");
		ssh.cleanExecDetail();
		ssh.close();
	}
	@Override
	public boolean connect(String ip, int port) {
		LOG.warn("暂不支持该方法");
		return loginStatus;
	}
	@Override
	public boolean connect(String ip) {
		return connect(ip, remotePort);
	}
	
	@Override
	public boolean login(String ip, int port, String username, String password, boolean enable, String enablePwd) {
		if(port >= 1){
			remotePort = port;
		}
		remoteIp = ip;
		remoteUsername = username;
		remotePassword = password;
		this.enable = enable;
		this.enablePwd = enablePwd;
		
		loginStatus = false;
		
		if(StringUtils.isEmpty(remoteIp)){
			LOG.error("{} IP地址为空,不允许登录操作", getLoginInfo());
			loginStatus = false;
			return loginStatus;
		}
		if(remoteUsername == null){
			LOG.error("{} 用户名为空,不允许登录操作", getLoginInfo());
			loginStatus = false;
			return loginStatus;
		}
		if(remotePassword == null){
			LOG.error("{} 密码为空,不允许登录操作", getLoginInfo());
			loginStatus = false;
			return loginStatus;
		}
		try {
		    //触发端口映射
		    portIpMap();
			LOG.info("{} SSH登录开始...", getLoginInfo());
			jsch = new JSch();
			session = jsch.getSession(remoteUsername, remoteIp, remotePort);
			session.setPassword(remotePassword);
			session.setTimeout(CmdClientConfig.getInstance().getCmdSessionTimeout());
			userInfo = new DefaultUserInfo();
			session.setUserInfo(userInfo);
			Hashtable<String, String> config = new Hashtable<String, String>();
			config.put("userauth.gssapi-with-mic", "no");// 不严格检查主机密钥
			config.put("StrictHostKeyChecking", "no");// 不严格检查主机密钥
			session.setConfig(config);
			LOG.info("{} 开始打开SSH Session连接...", getLoginSocketInfo());
			session.connect(connTimeout);
			LOG.info("{} 打开SSH Session连接完成", getLoginSocketInfo());
			loginStatus = session.isConnected();
			
			if(loginStatus){
				channelShell = (ChannelSubsystem) session.openChannel("subsystem");
				channelShell.setSubsystem("netconf");
				dataReader = channelShell.getInputStream();
				dataWriter = new PrintStream(channelShell.getOutputStream());
				
				LOG.info("{} 开始连接SSH通道...", getLoginSocketInfo());
				channelShell.connect(this.connTimeout);
				LOG.info("{} 连接SSH通道完成", getLoginSocketInfo());
				
				channelShell.setPtyType(terminalType.getName());
				
				if(resetPtySize){
					resetPtySize();
				}
			}
			
			//读取最后的结束符
			
			//校验登录状态
			
		} catch (Exception e) {
			loginStatus = false;
			
			//如果是认证问题，不是严重错误
			if(!e.getMessage().matches("(?i).*Auth.*")){
				loginError = true;
			}
			LOG.error("SSH连接" + getLoginInfo() + "发生异常", e);
		}finally{
			if(!loginStatus){
				close();
			}
			LOG.info("{} SSH登录结束", getLoginInfo());
		}
		return loginStatus;
	}
	
	@Override
	public void resetPtySize(){
		LOG.info("{} 开始设置SSH显示客户端大小...", getLoginSocketInfo());
		channelShell.setPtySize(800, 200, 1000, 500);
		LOG.info("{} 设置SSH显示客户端大小完成", getLoginSocketInfo());
	}
	
    @Override
    public boolean isNetconf() {
        return true;
    }
    
    @Override
    public boolean hasError(String info) {
        return hasNetconfError(info);
    }

    
    @Override
    public boolean hasOk(String info) {
        if (info != null && info.indexOf("<ok/>") >= 0){
            return true;
		}
        return false;
    }

	/**
	 * 有netconf错误
	 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
	 * @param data
	 * @return
	 */
	public static boolean hasNetconfError(String data) {
		if (data == null || !(data.indexOf("<rpc-error>") >= 0)) {
			return false;
		}
		if(data.contains(">error<")) {
			return true;
		}
		return false;
	}
}
