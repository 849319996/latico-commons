package com.latico.commons.common.util.system;


import com.latico.commons.common.util.codec.CodecUtils;
import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.io.flowreader.FileFlowReader;
import com.latico.commons.common.util.regex.RegexUtils;
import com.latico.commons.common.util.string.StringUtils;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * <PRE>
 *  // 实时打印命令行执行结果
 * </PRE>
 * @author: latico
 * @date: 2019-07-07 18:50:46
 * @version: 1.0
 */
public class JavaUtils {

	/** Java关键字数组 */
	private final static String[] JAVA_KEY_WORDS = {
			"abstract", "assert", "boolean", "break", "byte",
			"case", "catch", "char", "class", "const",
			"continue", "default", "do", "double", "else",
			"enum", "extends", "final", "finally", "float",
			"for", "goto", "if", "implements", "import",
			"instanceof", "int", "interface", "long", "native",
			"new", "package", "private", "protected", "public",
			"return", "short", "static", "strictfp", "super",
			"switch", "synchronized", "this", "throw", "throws",
			"transient", "try", "void", "volatile", "while",	
	};
	
	/** java关键字列表 */
	private final static List<String> JAVA_KEY_WORD_LIST = 
			Arrays.asList(JAVA_KEY_WORDS);
	
	/** 私有化构造函数 */
	protected JavaUtils() {}
	
	/**
	 * 检查单词是否为java关键字
	 * 
	 * @param word 待检查字符串
	 * @return true:是 ; false:不是
	 */
	public static boolean isJavaKeyWord(String word) {
		boolean isKeyWord = false;
		if(word != null && !"".equals(word.trim())) {
			isKeyWord = JAVA_KEY_WORD_LIST.contains(word.trim());
		}
		return isKeyWord;
	}
	
	/**
	 * <PRE>
	 * 修正Java源文件的package路径.
	 * 
	 * 	用于解决Eclipse迁移包代码时，不能自动修改package路径的问题。
	 * 	当要迁移整个包代码时，先在系统文件夹直接移动，再使用此方法调整所有源码文件的package路径。
	 * 	<B>使用要求：在迁移包代码之前，代码无任何语法错误。</B>
	 * </PRE>
	 * @param srcDirPath 源码根目录的绝对路径，如 D:foo/bar/project/src/main/java
	 * @param encoding 源码文件的内容编码
	 */
	public static void modifyPackagePath(String srcDirPath, String encoding) throws IOException {
		if(StringUtils.isEmpty(srcDirPath) || CodecUtils.isInvalid(encoding)) {
			return;
		}
		
		File srcDir = new File(srcDirPath);
		if(!srcDir.exists()) {
			return;
		}
		
		Map<String, String> packagePaths = new HashMap<String, String>();
		searchPackagePath(null, srcDir, encoding, packagePaths);
		modifyPackagePath(srcDir, encoding, packagePaths);
	}
	
	private static void searchPackagePath(String dir, File file, 
			String encoding, Map<String, String> packagePaths) {
		if(file.isDirectory()) {
			dir = (dir == null ? "" : StringUtils.join(dir, file.getName(), "."));
			File[] files = file.listFiles();
			for(File f : files) {
				searchPackagePath(dir, f, encoding, packagePaths);
			}
			
		} else {
			if(file.getName().endsWith(".java") && StringUtils.isNoneEmpty(dir)) {
				String newPackagePath = dir.replaceFirst("\\.$", "");
				String oldPackagePath = "";
				
				FileFlowReader ffr = new FileFlowReader(file, encoding);
				while(ffr.hasNextLine()) {
					String line = ffr.readLine(';');
					line = line.trim();
					if(line.startsWith("package")) {
						oldPackagePath = RegexUtils.findFirst(line, "package\\s([^;]+);");
						break;
					}
				}
				ffr.close();
				
				if(StringUtils.isNoneEmpty(oldPackagePath) &&
						!oldPackagePath.equals(newPackagePath)) {
					packagePaths.put(oldPackagePath, newPackagePath);
				}
			}
		}
	}
	
	private static void modifyPackagePath(File file, 
			String encoding, Map<String, String> packagePaths) throws IOException {
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(File f : files) {
				modifyPackagePath(f, encoding, packagePaths);
			}
			
		} else {
			if(file.getName().endsWith(".java")) {
				String content = FileUtils.readFileToString(file, encoding);
				Iterator<String> keyIts = packagePaths.keySet().iterator();
				while(keyIts.hasNext()) {
					String oldPackagePath = keyIts.next();
					String newPackagePath = packagePaths.get(oldPackagePath);
					content = content.replace(oldPackagePath, newPackagePath);
					if("".equals(newPackagePath)) {
						content = content.replaceAll("import\\s+\\w+;", "");	// 根目录下的类不能import
					}
				}
				FileUtils.write(file, content, encoding, false);
			}
		}
	}

	/**
	 * 编译java文件，编译后的文件在跟java文件同目录下
	 * @param charset       字符集
	 * @param javaFilePaths 文件列表
	 * @return
	 */
	public static boolean compileJavaFiles(String charset, String... javaFilePaths) throws Exception {

		//文件管理器
		StandardJavaFileManager fileManager = null;

		try {
			//获取系统编译器
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

			fileManager = compiler.getStandardFileManager(null, null, Charset.forName(charset));
			//通过传入java文件列表，获取java文件对象
			Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjects(javaFilePaths);

			//创建编译任务
			JavaCompiler.CompilationTask compilerTask = compiler.getTask(null, fileManager, null, null, null, javaFileObjects);

			//启动编译
			compilerTask.call();
		} catch (Exception e) {
			throw e;
		} finally {
			if (fileManager != null) {
				try {
					fileManager.close();
				} catch (IOException e) {
				}
			}
		}

		return true;
	}
}
