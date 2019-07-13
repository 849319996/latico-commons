package com.latico.commons.net.ftp.client;

import java.util.List;
import java.util.Map;

/**
 * <PRE>
 * FTP/SFTP连接操作接口
 * 全部路径字符串都转换成Linux模式
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2018年2月2日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public interface FtpClient {

	/**
	 * 上传本地文件到服务器远程目录功能 
	 * @param localFile 本地目录
	 * @param remoteDirectory 远程目录
	 */
	public boolean upload(String localFile, String remoteDirectory);

	/**
	 * 下载远程文件功能
	 * @param localDirectory 本地目录
	 * @param remoteFile 远程目录
	 */
	public boolean download(String localDirectory, String remoteFile);

	/**
	 * 查询文件列表功能
	 * @param remoteDirectory  远程目录
	 * @return 返回文件绝对路径名称的列表
	 */
	public List<String> listFiles(String remoteDirectory);

	/**
	 * 查询文件夹列表功能
	 * @param remoteDirectory  远程目录
	 * @return 返回文件夹绝对路径名称的列表
	 */
	public List<String> listDirs(String remoteDirectory);

	/**
	 * 关闭连接 <br>
	 */
	public void close();

	/**
	 * 获取文件大小
	 * @param remoteFileName
	 * @return
	 */
	public long getFileLength(String remoteFileName);

	/**
	 * 获取文件最后修改时间
	 *
	 * @param remoteFileName 远程文件名
	 * @return
	 */
	public long getLastModified(String remoteFileName);

	/**
	 * 列出目录下所有文件的大小
	 * @param remoteDirectory
	 * @return
	 */
	public Map<String, Long> listFilesSize(String remoteDirectory);

	/**
	 * 创建目录,父级目录必须存在才能创建成功
	 * @return 是否创建成功
	 */
	public boolean makeDir(String dirPath);
	
	/**
	 * 获取连接信息
	 * @return
	 */
	public String getConnInfo();

}