package com.latico.commons.net.smb;


import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <PRE>
 *  smb共享工具类
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-30 01:11:37
 * @Version: 1.0
 */
public class SMBImpl {

	// private SmbFile client = null;

	/**   */
	private NtlmPasswordAuthentication auth = null;
	
	/** 远程共享的根目录 */
	private static String dirName = null;

	/**
	 * 构造方法
	 */
	public SMBImpl() {
		init("", "", "", "");
	}

	/**
	 * 构造方法
	 * 
	 * @param domain
	 *            域，可以不填写，直接是NULL
	 * @param username
	 * @param password
	 * @throws IOException
	 */
	public SMBImpl(String domain, String username, String password,
                   String dirName) {
		init(domain, username, password, dirName);
	}
	
	/**
	 * 
	 *
	 * @param domain
	 * @param username
	 * @param password
	 * @param dirName
	 */
	public void init(String domain, String username, String password,
                     String dirName){
		if (!"".equals(username) && !"".equals(password)) {
			auth = new NtlmPasswordAuthentication(domain, username, password);
		}
		if("/".equals(dirName.charAt(dirName.length() - 1))){
			dirName = dirName.substring(0, dirName.length() -2);
		}
		SMBImpl.dirName = dirName;

		// client = new SmbFile(smbUrl, auth);
		// client.connect();
	}

	// public static void main(String[] args) throws Exception {
	// UploadDownloadUtil test = new UploadDownloadUtil();
	// smb:域名;用户名:密码@目的IP/文件夹/文件名.xxx
	// test.smbGet("smb://szpcg;jiang.t:xxx@192.168.193.13/Jake/test.txt",
	// "c://") ;

	// test.smbPut("smb://szpcg;jiang.t:xxx@192.168.193.13/Jake",
	// "c://test.txt");

	// 用户名密码不能有强字符，也就是不能有特殊字符，否则会被作为分断处理
	// test.smbGet(
	// "smb://CHINA;xieruilin:123456Xrl@10.70.36.121/project/report/网上问题智能分析助手使用文档.doc",
	// "c://Temp/");

	// List<String> list = null;
	// SMBImpl impl = null;
	//
	// impl = new SMBImpl();
	// list = impl.listFiles("smb://JHOME-PC/share/");
	// System.out.println(list);
	//
	// impl = new SMBImpl(null, "jhome", "816357492");
	// impl.upload("‪C:\\Users\\hj-54_000\\Documents\\预警确立事件.doc",
	// "smb://JHOME-PC/share12/");
	// list = impl.listFiles("smb://JHOME-PC/share12/");
	// System.out.println(list);
	// }

	/**
	 * 上传
	 * 
	 * @param localFileDir
	 *            本地文件路径
	 * @param localFileName
	 *            本地文件 文件名
	 * @param remoteDirectory
	 *            保存到远程 的 路径（忽略根目录）
	 */
	public void upload(String localFileDir, String localFileName,
                       String remoteDirectory) throws Exception {
		InputStream in = null;
		OutputStream out = null;
		SmbFile smbFile = null;
		if (remoteDirectory == null) {
			remoteDirectory = "";
		}
		localFileDir = checkDir(localFileDir, false, true);
		remoteDirectory = checkDir(remoteDirectory, true, false);
		localFileName = checkDir(localFileName, false, false);
		
		remoteDirectory = dirName + remoteDirectory;
		try {
			File file = new File(localFileDir + localFileName);

			// String fileName = file.getName();
			if (auth == null) {
				smbFile = new SmbFile(remoteDirectory + "/" + localFileName);
			} else {
				smbFile = new SmbFile(remoteDirectory + "/" + localFileName,
						auth);
			}
			in = new FileInputStream(file);
			in = new BufferedInputStream(in);
			out = new SmbFileOutputStream(smbFile);
			out = new BufferedOutputStream(out);
			byte[] buffer = new byte[1024];
			while (in.read(buffer) != -1) {
				out.write(buffer);
				buffer = new byte[1024];
			}
		} catch (Exception e) {
			throw e;
		} finally {
			closeStream(in);
			closeStream(out);
		}
	}

	/**
	 * 上传
	 * 
	 * @param localFile
	 *            本地文件 目录+文件名
	 * @param remoteDirectory
	 *            保存到远程 的 路径（忽略根目录）
	 */
	public void upload(String localFile, String remoteDirectory)
			throws Exception {
		InputStream in = null;
		OutputStream out = null;
		SmbFile smbFile = null;
		if (remoteDirectory == null) {
			remoteDirectory = "";
		}
		remoteDirectory = checkDir(remoteDirectory, true, false);
		localFile = checkDir(localFile, false, false);
		
		remoteDirectory = dirName + remoteDirectory;
		try {
			File file = new File(localFile);
			String fileName = file.getName();
			if (auth == null) {
				smbFile = new SmbFile(remoteDirectory + "/" + fileName);
			} else {
				smbFile = new SmbFile(remoteDirectory + "/" + fileName,
						auth);
			}
			in = new FileInputStream(file);
			in = new BufferedInputStream(in);
			out = new SmbFileOutputStream(smbFile);
			out = new BufferedOutputStream(out);
			byte[] buffer = new byte[1024];
			while (in.read(buffer) != -1) {
				out.write(buffer);
				buffer = new byte[1024];
			}
		} catch (Exception e) {
			throw e;
		} finally {
			closeStream(in);
			closeStream(out);
		}
	}

	/**
	 * 关闭流
	 * 
	 * @param stream
	 */
	private static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 下载
	 * 
	 * @param localDirectory
	 *            下载保存到本地 的 本地目录地址
	 * @param remoteFileDir
	 *            远程文件的目录（忽略根目录）
	 * @param remoteFileName
	 *            要下载的远程文件名
	 */
	public void download(String localDirectory, String remoteFileDir,
                         String remoteFileName) throws Exception {
		InputStream in = null;
		OutputStream out = null;
		SmbFile smbFile = null;
		if (remoteFileDir == null) {
			remoteFileDir = "";
		}
		localDirectory = checkDir(localDirectory, false, false);
		remoteFileDir = checkDir(localDirectory, true, true);
		remoteFileName = checkDir(remoteFileName, false, false);
		
		remoteFileName = dirName + remoteFileDir + remoteFileName;
		try {

			if (auth == null) {
				smbFile = new SmbFile(remoteFileName);
			} else {
				smbFile = new SmbFile(remoteFileName, auth);
			}
			// 这一句很重要
			smbFile.connect();
			if (!smbFile.exists()) {
				throw new Exception("smbFile not exist");
			}
			String fileName = smbFile.getName();
			File localFile = new File(localDirectory + "/" + fileName);
			in = new BufferedInputStream(new SmbFileInputStream(smbFile));
			out = new BufferedOutputStream(new FileOutputStream(localFile));
			byte[] buffer = new byte[1024];
			while (in.read(buffer) != -1) {
				out.write(buffer);
				buffer = new byte[1024];
			}
		} catch (Exception e) {
			throw e;
		} finally {
			closeStream(in);
			closeStream(out);
		}
	}
	
	/**
	 * 下载
	 * 
	 * @param localDirectory
	 *            下载保存到本地 的 本地目录地址
	 * @param remoteFile
	 *            要下载的远程文件     远程文件的目录（忽略根目录）+名
	 */
	public void download(String localDirectory, String remoteFile)
			throws Exception {
		InputStream in = null;
		OutputStream out = null;
		SmbFile smbFile = null;
		localDirectory = checkDir(localDirectory, false, false);
		remoteFile = checkDir(remoteFile, true, false);
		String remoteFileName = dirName + remoteFile;
		try {
			
			if (auth == null) {
				smbFile = new SmbFile(remoteFileName);
			} else {
				smbFile = new SmbFile(remoteFileName, auth);
			}
			// 这一句很重要
			smbFile.connect();
			if (!smbFile.exists()) {
				throw new Exception("smbFile not exist");
			}
			String fileName = smbFile.getName();
			File localFile = new File(localDirectory + "/" + fileName);
			in = new BufferedInputStream(new SmbFileInputStream(smbFile));
			out = new BufferedOutputStream(new FileOutputStream(localFile));
			byte[] buffer = new byte[1024];
			while (in.read(buffer) != -1) {
				out.write(buffer);
				buffer = new byte[1024];
			}
		} catch (Exception e) {
			throw e;
		} finally {
			closeStream(in);
			closeStream(out);
		}
	}

	/**
	 * 获取文件列表
	 * 
	 * @param remoteDirectory
	 *            获取文件列表的目录（忽略根目录）
	 */
	public List<String> listFiles(String remoteDirectory) throws Exception {
		SmbFile smbFile = null;
		if (remoteDirectory == null) {
			remoteDirectory = "";
		}
		remoteDirectory = checkDir(remoteDirectory, true, false);
		remoteDirectory = dirName + remoteDirectory;
		if (auth == null) {
			smbFile = new SmbFile(remoteDirectory);
		} else {
			smbFile = new SmbFile(remoteDirectory, auth);
		}
		SmbFile[] files = smbFile.listFiles();
		List<String> fileList = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {
			SmbFile fileFileTemp = files[i];
			if (fileFileTemp.isFile()) {
				fileList.add(remoteDirectory + fileFileTemp.getName());
			}
		}
		return fileList;
	}

	/**
	 * 目录列表
	 * 
	 * @param remoteDirectory
	 *            获取目录列表的父目录（忽略根目录）
	 */
	public List<String> listDirs(String remoteDirectory) throws IOException {
		SmbFile smbFile = null;
		if (remoteDirectory == null) {
			remoteDirectory = "";
		}
		remoteDirectory = checkDir(remoteDirectory, true, false);
		remoteDirectory = dirName + remoteDirectory;
		if (auth == null) {
			smbFile = new SmbFile(remoteDirectory);
		} else {
			smbFile = new SmbFile(remoteDirectory, auth);
		}
		SmbFile[] files = smbFile.listFiles();
		List<String> dirList = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {
			SmbFile fileFileTemp = files[i];
			if (fileFileTemp.isDirectory()) {
				dirList.add(remoteDirectory + fileFileTemp.getName());
			}
		}
		return dirList;
	}

	
	public void close() {
		// NOT TO DO
	}

	/**
	 * 文件长度
	 * 
	 * @param remoteFileDir
	 *            远程的文件所在的目录（忽略根目录，直接在根目录写的置空或为null）
	 * @param remoteFileName
	 *            远程的文件名
	 */
	public long getFileLength(String remoteFileDir, String remoteFileName)
			throws IOException {
		// remoteFileName 包含文件路径+文件名？
		SmbFile smbFile = null;
		if (remoteFileDir == null) {
			remoteFileDir = "";
		}
		remoteFileDir = checkDir(remoteFileDir, true, true);
		remoteFileName = checkDir(remoteFileName, false, false);
		remoteFileName = dirName + remoteFileDir + remoteFileName;
		if (auth == null) {
			smbFile = new SmbFile(remoteFileName);
		} else {
			smbFile = new SmbFile(remoteFileName, auth);
		}

		return smbFile.length();
	}
	
	/**
	 * 文件长度
	 * 
	 * @param remoteFile
	 *            远程的文件   远程的文件所在的目录（忽略根目录）+名
	 */
	public long getFileLength(String remoteFile)
			throws IOException {
		// remoteFileName 包含文件路径+文件名？
		SmbFile smbFile = null;
		remoteFile = checkDir(remoteFile, true, false);
		String remoteFileName = dirName + remoteFile;
		if (auth == null) {
			smbFile = new SmbFile(remoteFileName);
		} else {
			smbFile = new SmbFile(remoteFileName, auth);
		}
		
		return smbFile.length();
	}

	/**
	 * 文件最后修改时间
	 * 
	 * @param remoteFileDir
	 *            远程的文件所在的目录（忽略根目录，直接在根目录写的置空或为null）
	 * @param remoteFileName
	 *            远程的文件名
	 */
	public long getLastModified(String remoteFileDir, String remoteFileName)
			throws IOException {
		SmbFile smbFile = null;
		if (remoteFileDir == null) {
			remoteFileDir = "";
		}
		remoteFileDir = checkDir(remoteFileDir, true, true);
		remoteFileName = checkDir(remoteFileName, false, false);
		remoteFileName = dirName + remoteFileDir + remoteFileName;
		if (auth == null) {
			smbFile = new SmbFile(remoteFileName);
		} else {
			smbFile = new SmbFile(remoteFileName, auth);
		}

		return smbFile.lastModified();
	}
	
	/**
	 * 文件最后修改时间
	 * 
	 * @param remoteFile
	 *            远程的文件    远程的文件所在的目录（忽略根目录)+名
	 */
	public long getLastModified(String remoteFile)
			throws IOException {
		SmbFile smbFile = null;
		remoteFile = checkDir(remoteFile, true, false);
		String remoteFileName = dirName + remoteFile;
		if (auth == null) {
			smbFile = new SmbFile(remoteFileName);
		} else {
			smbFile = new SmbFile(remoteFileName, auth);
		}
		
		return smbFile.lastModified();
	}

	/**
	 * 对路径进行检查（主要检查路径前后的斜杠是否正确）
	 * @param dir 路径
	 * @param head 路径前是否加斜杠
	 * @param tail  路径后是否加斜杠
	 * @return
	 */
	public String checkDir(String dir, boolean head, boolean tail){
		if(head){
			if(!"/".equals(dir.charAt(0))){
				dir = "/" + dir;
			}
		}else{
			if("/".equals(dir.charAt(0))){
				dir = dir.substring(1, dir.length() - 1);
			}
		}
		if(tail){
			if(!"/".equals(dir.charAt(dir.length() - 1))){
				dir += "/";
			}
		}else{
			if("/".equals(dir.charAt(dir.length() - 1))){
				dir = dir.substring(0, dir.length() - 2);
			}
		}
		return dir;
	}
	
}
