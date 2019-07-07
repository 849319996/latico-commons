package com.latico.commons.net.cmdclient.impl;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.net.cmdclient.AbstractCmdClient;
import com.latico.commons.net.cmdclient.bean.LoginInfo;
import com.latico.commons.net.cmdclient.bean.ReadParam;
import com.latico.commons.net.cmdclient.enums.CmdClientTypeEnum;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.net.Ipv4Utils;
import com.latico.commons.common.util.string.StringUtils;
import org.apache.commons.exec.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * <PRE>
 * 执行命令行进程的预防超时类
 * </PRE>
 * @project   <B>项目：VPDN-Ping</B>
 * @copyright <B>技术支持：latico</B>
 * @version   <B>V1.0 2016年4月22日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class SysLocalCmdClient extends AbstractCmdClient {
	
	/** 日志 */
	private static final Logger LOG = LoggerFactory.getLogger(SysLocalCmdClient.class);
	
	/** timeoutMs 执行命令的超时时间 */
	private long timeoutMs = 60000;

	private ExecuteWatchdog watchDog;

	/** outputStream 系统输出标准信息流 */
	private ByteArrayOutputStream outputStream;

	/** errorStream 系统输出错误信息流 */
	private ByteArrayOutputStream errorStream;

	/** inputStream 输入数据到系统 */
	private ByteArrayInputStream inputStream;

	private DefaultExecutor exec;
	
	/** terminalType  */
	protected String dateCharset = CharsetType.DEFAULT;
	
	private String ip;
	
	/** execDetails  */
	protected List<String> execDetails = new ArrayList<String>();
	
	/**
	 * 构造函数
	 *  超时时间，单位毫秒。
	 */
	public SysLocalCmdClient() {
		this(60000);
	}
	
	/**
	 * 构造函数
	 * @param timeoutMs 超时时间，单位毫秒。
	 */
	public SysLocalCmdClient(long timeoutMs) {
		this.timeoutMs = timeoutMs;
		this.ip = Ipv4Utils.getLocalHostAddress();
		initExecute();
	}

	/**
	 * 初始化系统命令行工具
	 */
	private void initExecute() {

//		LOG.debug("初始化系统命令行工具");
//		System.out.println("初始化系统命令行工具");
		this.watchDog = new ExecuteWatchdog(timeoutMs);
		exec = new DefaultExecutor();
		exec.setWatchdog(watchDog);
		outputStream = new ByteArrayOutputStream();
		PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
		exec.setStreamHandler(streamHandler);
//		int[] exitValues = new int[]{35, 36, 58, 62, 93};
		exec.setExitValues(null);
	}

	/**
	 * 注意清空ByteArrayOutputStream输出流
	 * 该方法不帮助关闭
	 * @param command
	 * @return
	 * @throws IOException 
	 * @throws ExecuteException 
	 */
	private String executeCommandOutAndErr(String command) {

		String out = null;

		try {
			CommandLine commandLine = CommandLine.parse(command);// 命令行处理
			exec.execute(commandLine);
			out = outputStream.toString(dateCharset);
			outputStream.reset();// 重置字节计数，等于清空输出流
		} catch (Exception e) {
			LOG.error("", e);
		}

		return out;
	}

	/**
	 * 关闭流
	 * 手动关闭
	 */
	@Override
	public void close() {

		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (errorStream != null) {
			try {
				errorStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	public static void main(String[] args) {
		SysLocalCmdClient processExec = new SysLocalCmdClient(5000);
		String cmd4 = "ping 127.0.0.1 -n 4";
		System.out.println(processExec.executeCommandOutAndErr(cmd4));
		processExec.close();
		
//		for(int i=0; i<=256; i++){
//			System.out.println(i+":"+(char)i);
//		}
	}
	
	@Override
	public void setCharset(String charset){
		if(StringUtils.isNoneEmpty(charset)){
			this.dateCharset = charset;
		}
	}
	
	@Override
	public boolean execCommand(Object cmd) {
		boolean succ = false;
		try {
			CommandLine commandLine = CommandLine.parse(cmd.toString());// 命令行处理
			exec.execute(commandLine);
			succ = true;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return succ;
	}
	
	@Override
	public String readData() {
		String s = null;
		return readData(s);
	}
	
	@Override
	public String readData(String endStr) {
		return readData(endStr, 0);
	}

	@Override
	public String readData(long readTimeout) {
		return readData(null, readTimeout);
	}

	@Override
	public String readData(String endStr, long readTimeout) {
		return readData(endStr, null, 0);
	}

	@Override
	public String readData(String endStr, String notEndStrRegex,
	        long readTimeout) {
		return readData(endStr, notEndStrRegex, false, 0, 0);
	}

	@Override
	public String readData(String endStr, String notEndStrRegex,
	        boolean isLogSrcContent, int cutEndLineCount) {
		return readData(endStr, notEndStrRegex, isLogSrcContent, cutEndLineCount, 0);
	}
	
	@Override
	public String readData(String endStr, String notEndStrRegex,
	        boolean isLogSrcContent, int cutEndLineCount, long readTimeout) {
		String out = null;
		try {
			out = outputStream.toString(dateCharset);
			outputStream.reset();// 重置字节计数，等于清空输出流
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		execDetails.add(out);
		
		return out;
	}

	@Override
	public String execCmdAndReceiveData(Object cmd, String endStr,
	        long timeout) {
		return execCmdAndReceiveData(cmd, endStr);
	}
	@Override
	public String execCmdAndReceiveData(Object cmd, String endStr) {
		return execCmdAndReceiveData(cmd, endStr, null, 0);
	}

	@Override
	public String execCmdAndReceiveData(Object cmd, String endStr, String notEndStrRegex, long timeout) {
		
		if(execCommand(cmd)){
			return readData(endStr, notEndStrRegex, timeout);
		}else{
			return null;
		}
	}
	
	/**
	 * 添加执行细节
	 * @param str
	 */
	@Override
	protected void addExecDetail(String str){
		execDetails.add(str);
	}

	@Override
	public List<String> getExecDetail(){
		return new ArrayList<String>(execDetails);
	}

	@Override
	public void cleanExecDetail(){
		execDetails.clear();
	}
	
	@Override
	public boolean login(String ip, String username, String password) {
		return false;
		//TODO
	}

	@Override
	public boolean login(String ip, int port, String username,
	        String password) {
				return false;
		//TODO
	}

	@Override
	public boolean login(List<LoginInfo> loginInfos) {
		return false;
		//TODO
	}

	@Override
	public void logout() {
		close();
	}

	@Override
	public boolean login(List<LoginInfo> loginInfos, List<String> execDetails) {
		return false;
		//TODO
	}

	@Override
	public boolean isLogin() {
		return true;
	}

	@Override
	public String getIp() {
		return ip;
	}

	@Override
	public int getPort() {
		return -1;
	}

	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public boolean connect(String ip) {
		return false;
		//TODO
	}

	@Override
	public boolean connect(String ip, int port) {
		return false;
		//TODO
	}

	@Override
	public boolean login(LoginInfo loginInfo) {
		return false;
		//TODO
	}

	@Override
	public String read(ReadParam param) {
		return readData(param.getEndStrRegex(), param.getNotEndStrRegex(), param.getReadTimeout());
	}

	@Override
	public String execCmdAndReceive(Object cmd, ReadParam param) {
		return execCmdAndReceiveData(cmd, param.getEndStrRegex(), param.getNotEndStrRegex(), param.getReadTimeout());
	}


	@Override
	public String readRawAutoAnswerNo(String endStrRegex, String notEndStrRegex,
	        boolean isLogReturnData, long timeout) {
		return null;
	}

	@Override
	public String readRawAutoAnswerYes(String endStrRegex,
	        String notEndStrRegex, boolean isLogReturnData, long timeout) {
		return null;
	}

	@Override
	public String readRaw(String endStrRegex, String notEndStrRegex,
	        boolean isLogReturnData, boolean isAutoAnswer,
	        boolean isAutoAnswerYes, long timeout) {
		return null;
	}

	@Override
	public void setIp(String ip) {
		//TODO
	}

	@Override
	public void setPort(int port) {
		//TODO
	}

	@Override
	public void setUsername(String username) {
		//TODO
	}

	@Override
	public void setPassword(String password) {
		//TODO
	}

	@Override
	public void setWaitEndTimeWhenUnReadData(int waitTime) {
		//TODO
	}

	@Override
	public String getLoginInfo() {
		return "";
	}
	@Override
	public CmdClientTypeEnum getCmdClientType(){
		return CmdClientTypeEnum.SystemCmdLine;
	}

	@Override
	public boolean login(String ip, int port, String username, String password,
	        boolean isEnable, String enablePwd) {
				return false;
		//TODO
	}

	@Override
	public String getLoginSocketInfo() {
		return null;
		//TODO
	}

	@Override
	public String execCmdAndReceiveData(Object cmd) {
		return execCmdAndReceiveData(cmd, null);
	}

	@Override
	public boolean modifyCmdOutputAllDataImmediate(String manufacturer) {
		return false;
		//TODO
	}

	@Override
	public boolean enableHandle(String enablePwd) {
		return false;
		//TODO
	}

	@Override
	public String getEnablePwd() {
		return dateCharset;
		//TODO
	}

	@Override
	public boolean isLoginError() {
		return false;
		//TODO
	}

	@Override
	public void resetPtySize() {
		//TODO
	}

	@Override
	public void setCmdClientType(CmdClientTypeEnum cmdClientType) {
		//TODO
	}

	@Override
	public boolean isEnable() {
		return false;
		//TODO
	}

	@Override
	public void setEnable(boolean enable) {
		//TODO
	}

	@Override
	public void setEnablePwd(String enablePwd) {
		//TODO
	}

	@Override
	public boolean lastCmdReadTimeout() {
		return false;
		//TODO
	}

	@Override
	public int getLoginCount() {
		return 0;
		//TODO
	}

	@Override
	public void incrementLoginCount() {
		//TODO
	}

	@Override
	public void closeWithQuitCmd() {
		//TODO
	}

	@Override
	public void closeWithExitCmd() {
		//TODO
	}

	@Override
	public boolean login(String ip, int port, String username, String password,
	        String manufacturer, String model) {
				return false;
		//TODO
	}

	@Override
	public String getManufacturer() {
		return dateCharset;
		//TODO
	}

	@Override
	public void setManufacturer(String manufacturer) {
		//TODO
	}

	@Override
	public String getModel() {
		return dateCharset;
		//TODO
	}

	@Override
	public void setModel(String model) {
		//TODO
	}

	@Override
	protected void releaseSubResources() {
		//TODO
	}
}
