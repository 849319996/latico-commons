package com.latico.commons.net.ftp.impl;

import com.latico.commons.net.ftp.FtpClient;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <PRE>
 * FTP协议类型的实现
 * 全部路径字符串都转换成Linux模式
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2016年12月1日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class FtpClientImpl implements FtpClient {

    /** LOG 日志工具 */
    private static final Logger LOG = LoggerFactory.getLogger(FtpClientImpl.class);

	private String ip;
	private int port;
	private String username;
	private String password;
	/**
	 * 	ftp对象
	 */
	public FTPClient ftpClient = null;

	/**
	 * 构造方法
	 * @param ftpIp ip地址
	 * @param ftpPort 端口号
	 * @param ftpUsername 账号
	 * @param ftpPassword 密码
	 * @param timeOut 超时 单位秒
	 * @throws Exception 异常
	 */
	public FtpClientImpl(String ftpIp, int ftpPort, String ftpUsername,
						 String ftpPassword, int timeOut) throws Exception {
		this.ip = ftpIp;
		this.username = ftpUsername;
		this.password = ftpPassword;
		ftpClient = new FTPClient();// 由于重连时不能定位到远程的中文目录，故这里重新赋值一个对象
		ftpClient.setConnectTimeout(timeOut);// 连接超时60秒
		ftpClient.setDataTimeout(timeOut);// 访问超时60秒
		ftpClient.setControlKeepAliveTimeout(timeOut);
		ftpClient.setControlKeepAliveReplyTimeout(timeOut);
		ftpClient.setControlEncoding("UTF-8");
		if (ftpPort <= 0) {
			this.port = 22;
			ftpClient.connect(ftpIp);
		} else {
			ftpClient.connect(ftpIp, ftpPort);
			this.port = ftpPort;
		}
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			if (ftpClient.login(ftpUsername, ftpPassword)) {
				ftpClient.enterLocalPassiveMode();
			} else {
				throw new Exception("登陆失败");
			}
		} else {
			throw new Exception("连接失败");
		}
		LOG.info("FTP连接成功：{}", getConnInfo());
	}

	@Override
	public void close() {
		// Add By WuZhongtian 2014-11-2上午10:23:10
		try {
			if (ftpClient != null && ftpClient.isConnected()) {
				ftpClient.disconnect();
			} else {
			}
		} catch (Exception e) {
			LOG.error("", e);
		} finally {
			ftpClient = null;
		}
		LOG.info("关闭FTP连接：{}", getConnInfo());
	}

	@Override
	public List<String> listFiles(String remoteDirectory) {
		List<String> fileList = new ArrayList<String>();
		if(remoteDirectory == null){
			return fileList;
		}
		try {
			remoteDirectory = remoteDirectory.replace("\\", "/");
			if(!remoteDirectory.matches(".*[/\\\\]")){
				remoteDirectory += "/";
			}
			FTPFile[] ftpFiles = ftpClient.listFiles(remoteDirectory);
			
			for (int i = 0; i < ftpFiles.length; i++) {
				FTPFile fileFileTemp = ftpFiles[i];
				if (fileFileTemp.isFile()) {
					fileList.add(remoteDirectory + fileFileTemp.getName());
				}
			}
		} catch (Exception e) {
			LOG.error("", e);
		}
		return fileList;
	}

	@Override
	public boolean upload(String localFile, String remoteDirectory){
		if(localFile == null || remoteDirectory == null){
			return false;
		}
		
		File file = new File(localFile);
		InputStream is = null;
		boolean succ = false;
		try {
			remoteDirectory = remoteDirectory.replace("\\", "/");
			if (file.exists()) {
				if (!file.isFile()) {
					throw new FileNotFoundException();
				}
				ftpClient.makeDirectory(remoteDirectory);
				ftpClient.changeWorkingDirectory(remoteDirectory);
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);// 设置传输为2进制
				is = new FileInputStream(file);
				if (!ftpClient.storeFile(file.getName(), is)) {
					throw new Exception("upload error");
				}
			} else {
				throw new FileNotFoundException();
			}
			succ = true;
		} catch (Exception e) {
			LOG.error("", e);
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					LOG.error("", e);
				}
			}
		}
		return succ;
	}

	@Override
	public boolean download(String localDirectory, String remoteFile){
		if(localDirectory == null || remoteFile == null){
			return false;
		}
		OutputStream is = null;
		boolean succ = false;
		try {
			localDirectory = localDirectory.replace("\\", "/");
			if(!localDirectory.matches(".*[/\\\\]")){
				localDirectory += "/";
			}
			File localDir = new File(localDirectory);
			File reFile = new File(remoteFile);
			if (!(localDir.exists() && localDir.isDirectory())) {
				localDir.mkdirs();
			}
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);// 设置传输为2进制
			is = new FileOutputStream(localDirectory + reFile.getName());
			if (!ftpClient.retrieveFile(remoteFile, is)) {
				throw new Exception("download error");
			}
			succ = true;
		} catch (Exception e) {
			LOG.error("", e);
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					LOG.error("", e);
				}
			}
		}
		return succ;

	}

	@Override
	public long getFileLength(String remoteFileName) {
		long fileSize = -1;
		if(remoteFileName == null){
			return fileSize;
		}
		try {
			remoteFileName = remoteFileName.replace("\\", "/");
			FTPFile file = ftpClient.mlistFile(remoteFileName);
			if (file != null) {
				fileSize = file.getSize();
			}
		} catch (Exception e) {
			LOG.error("", e);
		}
		return fileSize;
	}

	@Override
	public long getLastModified(String remoteFileName) {
		long time = 0;
		if(remoteFileName == null){
			return time;
		}
		try {
			remoteFileName = remoteFileName.replace("\\", "/");
			FTPFile file = ftpClient.mlistFile(remoteFileName);
			if (file != null) {
				time = file.getTimestamp().getTimeInMillis();
			}
		} catch (Exception e) {
			LOG.error("", e);
		}
		return time;
	}

	@Override
	public List<String> listDirs(String remoteDirectory) {
		List<String> fileList = new ArrayList<String>();
		if(remoteDirectory == null){
			return fileList;
		}
		try {
			remoteDirectory = remoteDirectory.replace("\\", "/");
			if(!remoteDirectory.matches(".*[/\\\\]")){
				remoteDirectory += "/";
			}
			FTPFile[] ftpFiles = ftpClient.listFiles(remoteDirectory);
			
			for (int i = 0; i < ftpFiles.length; i++) {
				FTPFile fileFileTemp = ftpFiles[i];
				if (fileFileTemp.isDirectory()) {
					fileList.add(remoteDirectory + fileFileTemp.getName());
				}
			}
		} catch (Exception e) {
			LOG.error("", e);
		}
		return fileList;
	}

	/** 
	 * 该方法，异常时返回null
	 */
	@Override
	public Map<String, Long> listFilesSize(String remoteDirectory) {
		
		if(remoteDirectory == null){
			return null;
		}
		Map<String, Long> fileNameSize = new TreeMap<String, Long>();
		try {
			remoteDirectory = remoteDirectory.replace("\\", "/");
			if(!remoteDirectory.matches(".*[/\\\\]")){
				remoteDirectory += "/";
			}
			FTPFile[] ftpFiles = ftpClient.listFiles(remoteDirectory);
			if(ftpFiles == null){
				return null;
			}
			for (FTPFile fileFileTemp : ftpFiles) {
				if (fileFileTemp.isFile()) {
					fileNameSize.put(remoteDirectory + fileFileTemp.getName(), fileFileTemp.getSize());
				}
			}
		} catch (Exception e) {
			LOG.error("", e);
			return null;
		}
		return fileNameSize;
	}

	@Override
	public boolean makeDir(String remoteDir) {
		if(remoteDir == null){
			return false;
		}
		
		remoteDir = remoteDir.replace("\\", "/");
		boolean succ = false;
		try {
			String[] arr = remoteDir.split("[/\\\\]");
			StringBuffer curDir = new StringBuffer();
			for(String s : arr){
				if(s == null | "".equals(s.trim())){
					continue;
				}
				curDir.append(s).append("/");
				ftpClient.makeDirectory(curDir.toString());
			}
			succ = true;
		} catch (Exception e) {
			LOG.error("", e);
		}
		
		return succ;
	}

	@Override
	public String getConnInfo() {
		return StringUtils.join("[FTP连接-", ip, ":", port, " 账密:", username, "/", password, "]");
	}

}
