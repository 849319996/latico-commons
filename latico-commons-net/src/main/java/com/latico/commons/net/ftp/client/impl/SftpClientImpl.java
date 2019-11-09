package com.latico.commons.net.ftp.client.impl;

import com.latico.commons.net.ftp.client.FtpClient;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.regex.RegexUtils;
import com.latico.commons.common.util.string.StringUtils;
import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.LsEntry;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <PRE>
 * SFTP协议类型的实现
 * 全部路径字符串都转换成Linux模式
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2016年12月1日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class SftpClientImpl implements FtpClient {
	/** LOG 日志工具 */
	private static final Logger LOG = LoggerFactory.getLogger(SftpClientImpl.class);
	/**
	 * ftp通道对象
	 */
	public ChannelSftp sftp = null;

	/**
	 * 会话
	 */
	public Session sshSession = null;
	
	private String ip;
	private int port;
	private String username;
	private String password;
	/**
	 * 构造方法
	 * 
	 * @param ftpIp ip地址
	 * @param ftpPort 端口号
	 * @param ftpUsername 账号
	 * @param ftpPassword 密码
	 * @param timeOut 会话超时
	 * @throws JSchException 异常
	 */
	public SftpClientImpl(String ftpIp, int ftpPort, String ftpUsername,
						  String ftpPassword, int timeOut) throws JSchException {
		this.ip = ftpIp;
		this.username = ftpUsername;
		this.password = ftpPassword;
		JSch jsch = new JSch();
		if (ftpPort <= 0) {
			this.port = 22;
		} else {
			this.port = ftpPort;
		}
		sshSession = jsch.getSession(ftpUsername, ftpIp, port);
		sshSession.setPassword(ftpPassword);
		Hashtable<String, String> sshConfig = new Hashtable<String, String>();
		sshConfig.put("userauth.gssapi-with-mic", "no");// 不严格检查主机密钥
		sshConfig.put("StrictHostKeyChecking", "no");// 不严格检查主机密钥
		sshSession.setConfig(sshConfig);
		sshSession.connect(15000);
		sshSession.setTimeout(timeOut);
		ChannelSftp channel = (ChannelSftp) sshSession.openChannel("sftp");
		channel.connect(15000);
		sftp = channel;
		LOG.info("SFTP连接成功：{}", getConnInfo());
	}

	@Override
	public boolean download(String localDirectory, String remoteFile) {
		if(localDirectory == null || remoteFile == null){
			return false;
		}
		File file = new File(remoteFile);
		OutputStream is = null;
		boolean succ = false;
		try {
			remoteFile = remoteFile.replace("\\", "/");
			if(!localDirectory.matches(".*[/\\\\]")){
				localDirectory += "/";
			}
			is = new FileOutputStream(localDirectory + file.getName());
			sftp.get(remoteFile, is);
			succ = true;
		} catch (FileNotFoundException e) {
			LOG.error("", e);
		} catch (SftpException e) {
			LOG.error("", e);
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return succ;
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
			is = new FileInputStream(file);
			sftp.cd(remoteDirectory);
			sftp.put(is, file.getName());
			succ = true;
		} catch (Exception e) {
			LOG.error("", e);
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return succ;
	}

	@Override
	public void close() {
		try {
			if (sftp != null) {
				sftp.disconnect();
			}
			
		} catch (Exception e) {
			LOG.error("", e);
		} finally {
			try {
				if (sshSession != null) {
					sshSession.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 释放掉下载数
			// relFtpIpConn(this, null);
			sshSession = null;
		}
		LOG.info("关闭SFTP连接：{}", getConnInfo());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> listFiles(String remoteDirectory) {
		List<String> filelistTemp = null;
		try {
			remoteDirectory = remoteDirectory.replace("\\", "/");
			if(!remoteDirectory.matches(".*[/\\\\]")){
				remoteDirectory += "/";
			}
			Vector<LsEntry> v = (Vector<LsEntry>) sftp.ls(remoteDirectory);
			filelistTemp = new ArrayList<String>();
			for (Iterator<LsEntry> iterator = v.iterator(); iterator.hasNext();) {
				LsEntry lsEntry = (LsEntry) iterator.next();
				String fileName = remoteDirectory + lsEntry.getFilename();
				SftpATTRS file = sftp.lstat(fileName);
				if (file.isDir() == false) {
					filelistTemp.add(fileName);
				}
			}
		} catch (Exception e) {
			LOG.error("", e);
		}
		return filelistTemp;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> listDirs(String remoteDirectory) {
		List<String> filelistTemp = new ArrayList<String>();
		if(remoteDirectory == null){
			return filelistTemp;
		}
		try {
			remoteDirectory = remoteDirectory.replace("\\", "/");
			if(!remoteDirectory.matches(".*[/\\\\]")){
				remoteDirectory += "/";
			}
			Vector<LsEntry> v = (Vector<LsEntry>) sftp.ls(remoteDirectory);
			for (Iterator<LsEntry> iterator = v.iterator(); iterator.hasNext();) {
				LsEntry lsEntry = (LsEntry) iterator.next();
				String fileName = remoteDirectory + lsEntry.getFilename();
				SftpATTRS file = sftp.lstat(fileName);
				if (file.isDir()) {
					filelistTemp.add(fileName);
				}
			}
		} catch (Exception e) {
			LOG.error("", e);
		}
		return filelistTemp;
	}

	@Override
	public long getFileLength(String remoteFileName){
		long size = -1;
		if(remoteFileName == null){
			return size;
		}
		try {
			remoteFileName = remoteFileName.replace("\\", "/");
			SftpATTRS file = sftp.lstat(remoteFileName);
			size = file.getSize();
		} catch (Exception e) {
			LOG.error("", e);
		}
		return size;
	}

	@Override
	public long getLastModified(String remoteFileName) {
		long time = -1;
		if(remoteFileName == null){
			return time;
		}
		try {
			remoteFileName = remoteFileName.replace("\\", "/");
			SftpATTRS file = sftp.lstat(remoteFileName);
			if (file != null) {
//			-rw-r--r-- 0 0 872093 Fri Mar 21 15:34:30 CST 2014
				String strDate = file.toString();
				strDate = RegexUtils.findFirst(".*\\s\\d+\\s\\d+\\s\\d+\\s(.*)", strDate);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"EEE MMM DD HH:mm:ss z yyyy", Locale.ENGLISH);
				time = sdf.parse(strDate).getTime();
			}
			
		} catch (Exception e) {
			LOG.error("", e);
		}
		return time;
	}

	/** 
	 * 该方法，异常时返回null
	 */
	@Override
	public Map<String, Long> listFilesSize(String remoteDirectory) {
		if(remoteDirectory == null){
			return null;
		}
		Map<String, Long> filePathFileSizeMap = new TreeMap<String, Long>();
		
		try {
			remoteDirectory = remoteDirectory.replace("\\", "/");
			if(!remoteDirectory.matches(".*[/\\\\]")){
				remoteDirectory += "/";
			}
			@SuppressWarnings("unchecked")
            Vector<LsEntry> v = (Vector<LsEntry>) sftp.ls(remoteDirectory);
			if(v == null){
				return null;
			}
			for (LsEntry lsEntry : v) {
				String fileName = remoteDirectory + lsEntry.getFilename();
				SftpATTRS file = sftp.lstat(fileName);
				if (file.isDir() == false) {
					filePathFileSizeMap.put(fileName, file.getSize());
				}
			}
		} catch (Exception e) {
			LOG.error("", e);
		}
		return filePathFileSizeMap;
	}

	/** 
	 * 需要判断文件夹是否存在，如果存在，就深入下一层，发现不存在的就创建
	 */
	@Override
	public boolean makeDir(String remoteDir) {
		if(remoteDir == null){
			return false;
		}
		boolean succ = false;
		try {
			remoteDir = remoteDir.replace("\\", "/");
			String[] dirNames = remoteDir.split("/");
			StringBuilder curDir = new StringBuilder("./");

			for(String dirName : dirNames){
				if(StringUtils.isEmpty(dirName)){
					continue;
				}
				List<String> dirs = listDirs(curDir.toString());
				curDir.append(dirName).append("/");

//				不存在的时候创建
				boolean existsDir = false;
				for(String director : dirs){
					if(curDir.toString().equals(director)){
						existsDir = true;
						break;
					}
				}

				if(!existsDir){
					sftp.mkdir(curDir.toString());
				}
			}
			succ = true;
		} catch (Exception e) {
			LOG.error("", e);
		}
		
		return succ;
	}
	@Override
	public String getConnInfo() {
		return StringUtils.join("[SFTP连接-", ip, ":", port, " 账密:", username, "/", password, "]");
	}
	
}
