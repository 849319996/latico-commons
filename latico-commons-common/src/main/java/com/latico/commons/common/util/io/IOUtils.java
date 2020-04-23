package com.latico.commons.common.util.io;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.math.UnitUtils;
import com.latico.commons.common.util.other.PathUtils;
import com.latico.commons.common.util.system.classloader.ClassLoaderUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * IO工具.处理IO流相关
 * @author: latico
 * @date: 2018/12/16 02:52:37
 * @version: 1.0
 */
public class IOUtils extends org.apache.commons.io.IOUtils {
	/** LOG 日志工具 */
	private static final Logger LOG = LoggerFactory.getLogger(IOUtils.class);
	
	/** 私有化构造函数 */
	protected IOUtils() {}

	/**
	 * 关闭IO流
	 * @param closeable IO流接口
	 * @return true:关闭成功; false:关闭失败
	 */
	public static boolean close(Closeable closeable) {
		boolean isOk = true;
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				LOG.error("IO流关闭失败.", e);
				isOk = false;
			}
		}
		return isOk;
	}

	/**
	 * 关闭数据库sql接口
	 * @param statement 数据库sql接口
	 * @return true:关闭成功; false:关闭失败
	 */
	public static boolean close(Statement statement) {
		boolean isOk = true;
		if (statement != null) {
			try {
				statement.close();
	        } catch (Exception e) {
	        	LOG.error("IO流关闭失败.", e);
	        	isOk = false;
	        }
		}
		return isOk;
    }

	/**
	 * 关闭数据库结果集接口
	 * @param resultSet 数据库结果集接口
	 * @return true:关闭成功; false:关闭失败
	 */
	public static boolean close(ResultSet resultSet) {
		boolean isOk = true;
		if (resultSet != null) {
			try {
				resultSet.close();
	        } catch (Exception e) {
	        	LOG.error("IO流关闭失败.", e);
	        	isOk = false;
	        }
		}
		return isOk;
    }

	/**
	 * 关闭数据库连接
	 * @param conn 数据库连接
	 */
	public static boolean close(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				LOG.error("关闭数据库连接失败.", e);
				return false;
			}
		}
		return true;
	}

	/**
	 * 关闭数据库相关IO资源
	 * @param conn 数据库连接
	 * @param statement 数据库连接声明
	 * @param resultSet 数据库连接结果集
	 */
	public static boolean close(Connection conn, Statement statement, ResultSet resultSet) {
		boolean bool = close(resultSet);
		bool &= close(statement);
		bool &= close(conn);
		return bool;
	}

	/**
	 * 保存流式数据到文件
	 * @param is 流式数据通道
	 * @param savePath 保存文件位置
	 * @return true:保存成功; false:保存失败
	 */
	public static void toFile(InputStream is, String savePath) throws IOException {
		File saveFile = new File(savePath);
		toFile(is, saveFile);
	}

	/**
	 * 保存流式数据到文件
	 * @param is 流式数据通道
	 * @param saveFile 保存文件对象
	 * @return true:保存成功; false:保存失败
	 */
	public static void toFile(InputStream is, File saveFile) throws IOException {
		if(is != null && saveFile != null) {
			FileUtils.createDir(saveFile.getParent());
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(saveFile);
				int len = 0;
				byte[] buffer = new byte[UnitUtils._1_MB];
				while((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				fos.flush();

			} catch (Exception e) {
				throw new IOException("保存流式数据到文件 [{}] 失败."+saveFile.getAbsolutePath(), e);

			} finally {
				closeQuietly(fos);
			}
		}
	}

	/**
	 * Gets the contents of a classpath resource as a String using the
	 * specified character encoding.
	 *
	 * <p>
	 * It is expected the given <code>name</code> to be absolute. The
	 * behavior is not well-defined otherwise.
	 * </p>
	 *
	 * @param name     name of the desired resource
	 * @return the requested String
	 * @throws IOException if an I/O error occurs
	 *
	 * @since 2.6
	 */
	public static String resourceToString(String name) throws IOException {
		name = PathUtils.formatResourcesPathToRootPathForClass(name);
		return resourceToString(name, Charset.forName(CharsetType.UTF8), null);
	}

	/**
	 * 指定类加载资源，会使用Class中的类加载器，假如name不是/开头，Class会自动添加包名在前缀
	 * @param clazz 指定类，一般是用于获取当前类下的路径使用
	 * @param name 如果不是/开头，Class内部会自动添加前缀
	 * @return
	 * @throws IOException
	 */
	public static String resourceToStringByClass(Class clazz, String name) {
		InputStream resourceAsStream = null;
		String str = null;
		try {
			resourceAsStream = clazz.getResourceAsStream(name);
			str = toString(resourceAsStream, CharsetType.UTF8);
		} catch (Exception e) {
			LOG.error(e);
		}finally {
			close(resourceAsStream);
		}
		return str;
	}

	/**
	 * 通过类加载器方式加载
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static String resourceToStringByClassLoader(String name) throws IOException {
		return resourceToStringByClassLoader(name, CharsetType.UTF8);
	}

	/**
	 * 通过类加载器方式加载
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static String resourceToStringByClassLoader(String name, String encoding) throws IOException {
		name = PathUtils.formatResourcesPathForClassLoader(name);
		return resourceToString(name, Charset.forName(encoding), ClassLoaderUtils.getDefaultClassLoader());
	}

	/**
	 * @param name
	 * @param classLoader
	 * @return
	 * @throws IOException
	 */
	public static String resourceToString(String name, ClassLoader classLoader) throws IOException {
		if (classLoader == null) {
			name = PathUtils.formatResourcesPathToRootPathForClass(name);
		}else{
			name = PathUtils.formatResourcesPathForClassLoader(name);
		}
		return resourceToString(name, Charset.forName(CharsetType.UTF8), classLoader);
	}

	/**
	 * 读取资源转化成字符串
	 * @param name 资源文件路径
	 * @param encoding 字符集
	 * @return
	 * @throws IOException
	 */
	public static String resourceToString(String name, final String encoding) throws IOException {
		name = PathUtils.formatResourcesPathToRootPathForClass(name);
		return resourceToString(name, Charset.forName(encoding));
	}

	/**
	 * URL到字节数组
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static byte[] readUrlToByteArray(URL url) throws Exception {
		InputStream is = null;
		try {
			is = url.openStream();
			return IOUtils.toByteArray(is);
		} catch (Exception e) {
			throw e;
		}finally {
			IOUtils.close(is);
		}
	}
}
