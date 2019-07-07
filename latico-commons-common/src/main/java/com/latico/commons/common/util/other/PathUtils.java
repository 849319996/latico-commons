package com.latico.commons.common.util.other;

import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.string.StringUtils;
import com.latico.commons.common.util.system.SystemUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <PRE>
 * 路径处理工具.
 * </PRE>
 * @Author: latico
 * @Date: 2019-07-07 19:42:33
 * @Version: 1.0
 */
public class PathUtils {

	/** 私有化构造函数 */
	protected PathUtils() {}
	
	/**
	 * 判断是否为全路径（即以根开始的路径）
	 * 
	 * @param path 路径
	 * @return true:全路径; false:非全路径
	 */
	public static boolean isFullPath(String path) {
		return isWinFullPath(path) || isUnixFullPath(path);
	}
	
	/**
	 * 判断是否为Windows的全路径（即以根开始的路径）
	 * 
	 * @param path 路径
	 * @return true:全路径; false:非全路径
	 */
	public static boolean isWinFullPath(String path) {
		return path.matches("^[A-Za-z]:[/|\\\\].*$");
	}
	
	/**
	 * 判断是否为Unix的全路径（即以根开始的路径）
	 * 
	 * @param path 路径
	 * @return true:全路径; false:非全路径
	 */
	public static boolean isUnixFullPath(String path) {
		return path.matches("^/.*$");
	}
	
	/**
	 * <PRE>
	 * 路径合并。
	 * 同时会把/./这种优化掉
	 * <PRE>
	 * @param prefixPath 路径前缀，全路径或相对路径
	 * @param suffixPath 路径后缀，必须是相对路径
	 * @return 路径前缀 + 路径分隔符 + 路径后缀 
	 */
	public static String combine(String prefixPath, String suffixPath) {
		String combPath = "";
		if(StringUtils.isNoneBlank(prefixPath, suffixPath)) {
			prefixPath = prefixPath.trim().replace('\\', '/');
			suffixPath = suffixPath.trim().replace('\\', '/');
			
			if(isWinFullPath(suffixPath)) {
				combPath = prefixPath;
				
			} else {
				if(!prefixPath.endsWith("/")) {
					prefixPath = prefixPath.concat("/");
				}
				combPath = StringUtils.join(prefixPath, suffixPath);
				combPath = combPath.replace("/./", "/").replace("//", "/");
			}
			
		} else if(StringUtils.isNotBlank(prefixPath)) {
			combPath = suffixPath.trim();
			
		} else if(StringUtils.isNotBlank(suffixPath)) {
			combPath = prefixPath.trim();
		}
		
		combPath = combPath.replace('\\', '/');
		return combPath;
	}
	
	/**
	 * 获取文件的父目录
	 * @param filePath 文件路径
	 * @return 文件的父目录
	 */
	public static String getParentDir(String filePath) {
		String parentDir = "";
		try {
			parentDir = new File(filePath).getParent();
		} catch(Exception e) {
			Pattern pat = Pattern.compile("(.*)[/\\\\]");
			Matcher mat = pat.matcher(filePath);
			if (mat.find()) {
				parentDir = mat.group(1);
			}
		}
		return parentDir;
	}
	/**
	 * 获取文件的父目录
	 * @param filePath 文件路径
	 * @return 文件的父目录
	 */
	public static File getParentFile(String filePath) {
		return new File(filePath).getParentFile();
	}

	/**
	 * 获取文件的名称
	 * @param filePath 文件路径
	 * @return 文件的名称
	 */
	public static String getFileName(String filePath) {
		return new File(filePath).getName();
	}
	
	/**
	 * 取得项目的绝对路径, 如: X:\foo\project
	 * @return 项目的绝对路径
	 */
	public static String getProjectPath() throws IOException {
		String path = "";
		path = new File(".").getCanonicalPath();
		return path;
	}
	
	/**
	 * 获取 项目的根路径，如： X:\foo\project
	 * @return 项目的根路径
	 */
	public static String getProjectRootPath() {
		return System.getProperty("user.dir").concat(File.separator);
	}
	
	/**
	 * <PRE>
	 * 获取项目的编译目录的根路径。
	 *   非Tomcat项目形, 如： X:/foo/bar/project/target/classes
	 *   Tomcat项目形, 如:  %tomcat%/%wepapp%/%project%/classes
	 * <PRE>
	 * @return 项目的编译根路径
	 */
	public static String getProjectCompilePath() {

//		也可以使用类加载器的方式，效果一样
//		URL resource = Thread.currentThread().getContextClassLoader().getResource("");
		URL resource = PathUtils.class.getResource("/");
		if (resource != null) {
			String path = resource.getPath();
			if(path != null){
				File file = new File(path);
				String absolutePath = file.getAbsolutePath();
				if (absolutePath != null) {
					return absolutePath.concat(File.separator);
				}

			}
		}
		return null;
	}
	
	/**
	 * 获取 类的编译路径，如：X:/workspace/project/target/classes/foo/bar
	 * @param clazz 类
	 * @return 类的编译路径
	 */
	public static String getClassCompilePath(Class<?> clazz) {
		return new File(clazz.getResource("").getPath()).
				getAbsolutePath();
	}
	
	/**
	 * <PRE>
	 * 获取项目自身引用的所有jar包的路径。
	 * 只能获取运行main方法的项目所需要的jar包路径，而不能获取其他项目的jar包类路径。
	 * (如果是外部jdk调用，则返回的是 -cp 的参数表)
	 * </PRE>
	 * @return 项目引用的所有包的路径
	 */
	public static String[] getAllClassPaths() {
		return System.getProperty("java.class.path").split(";");
	}
	
	/**
	 * 把路径转换为运行平台的标准路径。
	 * @param paths 路径集
	 * @return 标准路径集
	 */
	public static List<String> toStandard(List<String> paths) {
		return (SystemUtils.IS_OS_WINDOWS ? toWin(paths) : toLinux(paths));
	}
	
	/**
	 * 把路径转换为运行平台的标准路径。
	 * @param path 路径
	 * @return 标准路径
	 */
	public static String toStandard(String path) {
		return (SystemUtils.IS_OS_WINDOWS ? toWin(path) : toLinux(path));
	}
	
	/**
	 * 把linux路径转换为win路径
	 * @param linuxPaths linux路径集
	 * @return win路径集
	 */
	public static List<String> toWin(List<String> linuxPaths) {
		List<String> winPaths = new LinkedList<String>();
		for(String linuxPath : linuxPaths) {
			winPaths.add(toWin(linuxPath));
		}
		return winPaths;
	}
	
	/**
	 * 把linux路径转换为win路径（仅对相对路径有效）
	 * @param linuxPath linux路径
	 * @return win路径
	 */
	public static String toWin(String linuxPath) {
		String winPath = "";
		if(linuxPath != null) {
			winPath = linuxPath.replace('/', '\\');
		}
		return winPath;
	}
	
	/**
	 * 把win路径转换为linux路径
	 * @param winPaths win路径集
	 * @return linux路径集
	 */
	public static List<String> toLinux(List<String> winPaths) {
		List<String> linuxPaths = new LinkedList<String>();
		for(String winPath : winPaths) {
			linuxPaths.add(toLinux(winPath));
		}
		return linuxPaths;
	}
	
	/**
	 * 把win路径转换为linux路径（仅对相对路径有效）
	 * @param winPath win路径
	 * @return linux路径
	 */
	public static String toLinux(String winPath) {
		String linuxPath = "";
		if(winPath != null) {
			linuxPath = winPath.replace('\\', '/');
		}
		return linuxPath;
	}
	
	/**
	 * 获取 [当前代码运行处] 的调用堆栈路径, 并用“|”将堆栈路径拼接起来。
	 * @return 用“|”连接的堆栈路径
	 */
	public static String getCallStackPath() {
		StringBuilder sb = new StringBuilder();
		try {
			throw new Exception();
			
		} catch (Exception e) {
			StackTraceElement[] stes = e.getStackTrace();

			for (StackTraceElement ste : stes) {
				sb.append(ste.getClassName());
				sb.append("(").append(ste.getFileName());
				sb.append(":").append(ste.getLineNumber());
				sb.append(")|");
			}
			
			if(stes.length > 0) {
				sb.setLength(sb.length() - 1);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 获取桌面路径
	 * @return 桌面路径
	 */
	public static String getDesktopPath() {
		FileSystemView fsv = FileSystemView.getFileSystemView();
		return fsv.getHomeDirectory().getPath();
	}
	
	/**
	 * 获取系统临时目录
	 * @return 系统临时目录
	 */
	public static String getSysTmpDir() {
		return System.getProperty("java.io.tmpdir");
	}

	/**
	 * 通过编译目录判断是不是Web容器方式启动的
	 * 适应web项目的配置文件
	 * 如果一个配置文件需要支持普通java项目启动也要支持web容器启动，因为web容器启动的时候，项目的当前目录是在web容器的bin目录，
	 * 所以要让一个文件的读取路径同时支持，那就需要使用该方法，
	 * 示例：
	 * 普通java项目下有个文件: ./config/config.xml
	 * web项目的时候配置文件放在: WebContent/WEB-INFO/config/config.xml或者/WEB-INFO/config/classes/config.xml
	 * 此时调用该方法，假如是在web容器启动的话，传入相对路径./config/config.xml，会得到config.xml在web容器中的绝对路径
	 * @param relativePath 要适配的相对路径
	 * @return 适应web路径后的文件目录
	 */
	public static String adapterFilePathSupportWebContainer(String relativePath) {
		String projectCompilePath = PathUtils.getProjectCompilePath();
		if (projectCompilePath == null) {
			return relativePath;
		}
		if(isWebContainerPath(projectCompilePath)) {
			File dir = new File(projectCompilePath);
			// classes目录优先
			String path = PathUtils.combine(dir.getAbsolutePath(), relativePath);

			// 若classes目录下不存在该文件, 则找上一层目录
			if(!FileUtils.exists(path)) {
				path = PathUtils.combine(dir.getParentFile().getAbsolutePath(), relativePath);
			}
			relativePath = path;
		}
		return relativePath;
	}

	/**
	 * 路径是不是Web启动的目录，比如有：/WEB-INF/
	 * @param projectCompilePath
	 * @return
	 */
	public static boolean isWebContainerPath(String projectCompilePath) {
		return toLinux(projectCompilePath).matches(".+?/WEB-INF/.+");
	}

	/**
	 * 因为类加载器加载资源路径的时候，实际上是不能以/开头的，/开头会使用Bootstrap类加载器
	 * 把资源路径适配成类加载器资源路径格式
	 * 1、如果没有以/开头，那就去掉；
	 * @param resourcesPath
	 * @return
	 */
	public static String formatResourcesPathForClassLoader(String resourcesPath){
		if(resourcesPath != null){
			if(resourcesPath.startsWith("/")){
				resourcesPath = resourcesPath.substring(1);
			}
		}
		return resourcesPath;
	}

	/**
	 * 因为假如是用了Class.getResource()等获取资源的方法，Class类里面会对路径进行优化，
	 * 所以该方法是把路径格式化成适应Class加载方式的根路径方式
	 * 1、如果没有以/开头，那就补上；
	 * @param resourcesPath
	 * @return
	 */
	public static String formatResourcesPathToRootPathForClass(String resourcesPath){
		if(resourcesPath != null){
			if(!resourcesPath.startsWith("/")){
				resourcesPath = "/" + resourcesPath;
			}
		}
		return resourcesPath;
	}

}
