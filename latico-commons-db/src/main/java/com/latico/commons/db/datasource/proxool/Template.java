package com.latico.commons.db.datasource.proxool;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.util.codec.CodecUtils;
import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.io.JarUtils;
import com.latico.commons.common.util.logging.Log;
import com.latico.commons.common.util.logging.LogUtils;
import com.latico.commons.common.util.string.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <PRE>
 * 模板文件加载类。
 * 提供加载模板、替换模板占位符、获取替换后文件内容的方法。
 * 
 * 注意：模板文件中的占位符为  @{占位符名称}@，而代码替换只需 set(占位符名称, 值)。
 * 
 * 使用示例:
 * 	Template tpl = new Template(FILE_PATH, CharsetType.UTF8);
 *  tpl.set("username", "exp");
 *  tpl.set("year", "2017-08-21");
 *  String content = tpl.getContent();
 * 
 * </PRE>
 * <B>PROJECT：</B> exp-libs
 * <B>SUPPORT：</B> EXP
 * @version   1.0 2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Template {
	
	/** 日志器 */
	private final static Log log = LogUtils.getLog(Template.class);
	
	/**
	 * 默认编码
	 */
	public final static String DEFAULT_CHARSET = CharsetType.UTF8;
	
	/**
	 * 占位符的左括号
	 */
	private final static String PLACEHOLDER_LEFT = "@{";
	
	/**
	 * 占位符的右括号
	 */
	private final static String PLACEHOLDER_RIGHT = "}@";
	
	/**
	 * 未被替换过占位符的模板内容
	 */
	private String tplContent;
	
	/**
	 * 替换过占位符的内容
	 */
	private String content;
	
	/**
	 * KV表：占位符名称-值
	 */
	private Map<String, String> kvMap;
	
	/**
	 * 模板文件的字符集编码
	 */
	private String charset;
	
	/**
	 * 是否需要刷新模板
	 */
	private boolean isFlash;
	
	/**
	 * 构造函数(默认文件内容编码为UTF-8)
	 * @param tplFilePath 模板文件路径, 支持：
	 * 					1. 磁盘文件路径, 如:	./src/main/resources/foo/bar/test.txt
	 * 					2. jar包内文件路径, 如: /foo/bar/test.txt
	 */
	public Template(String tplFilePath) throws IOException {
		this(tplFilePath, DEFAULT_CHARSET);
	}
	
	/**
	 * 构造函数
	 * @param tplFilePath 模板文件路径, 支持：
	 * 					1. 磁盘文件路径, 如:	./src/main/resources/foo/bar/test.txt
	 * 					2. jar包内文件路径, 如: /foo/bar/test.txt
	 * @param charset 文件内容编码
	 */
	public Template(String tplFilePath, String charset) throws IOException {
		this.charset = (CodecUtils.isVaild(charset) ? charset : DEFAULT_CHARSET);
		this.tplContent = read(tplFilePath, this.charset);
		this.content = tplContent;
		this.kvMap = new HashMap<String, String>();
		this.isFlash = true;
	}
	
	
	/**
	 * 使用指定编码读取模板文件。
	 * @param tplFilePath 模板文件路径
	 * @param charset 模板文件编码
	 */
	private String read(String tplFilePath, String charset) throws IOException {
		String content = FileUtils.exists(tplFilePath) ? 
				FileUtils.readFileToString(tplFilePath, charset) :
				JarUtils.read(tplFilePath, charset);
		
		if(StringUtils.isEmpty(content)) {
			log.warn("读取模板文件 [{}] 失败.", tplFilePath);
		}
		return content;
	}
	
	/**
	 * 设置各个占位符的值，同名占位符会被后设置的值覆盖
	 * @param placeholder 占位符
	 * @param value 替换值
	 */
	public void set(String placeholder, String value) {
		kvMap.put(placeholder, value);
		isFlash = true;
	}
	
	/**
	 * 获取未被替换任何占位符的模板内容
	 * @return 模板内容
	 */
	public String getTemplate() {
		return tplContent;
	}
	
	/**
	 * 获取最后一次设值后的模板内容
	 * @return 模板占位符被替换后的内容
	 */
	public String getContent() {
		String content = this.content;
		if(isFlash == true) {
			isFlash = false;
			
			content = tplContent;
			for(Iterator<String> keyIts = kvMap.keySet().iterator();
					keyIts.hasNext();) {
				String key = keyIts.next();
				String val = kvMap.get(key);
				
				key = StringUtils.join(PLACEHOLDER_LEFT, key, PLACEHOLDER_RIGHT);
				val = (val == null ? "" : val);
				content = content.replace(key, val);
			}
			this.content = content;
		}
		return content;
	}
	
	/**
	 * 获取当前的占位符表 
	 * @return 占位符表
	 */
	public Map<String, String> getPlaceHolders() {
		return new HashMap<String, String>(kvMap);
	}
	
}
