package com.latico.commons.common.envm;

/**
 * 枚举类：编码
 * @author: latico
 * @date: 2018/12/16 02:50:41
 * @version: 1.0
 */
public class CharsetType {

	/** 虚拟机系统编码 */
	public final static String DEFAULT = System.getProperty("sun.jnu.encoding");
	
	/** UNICODE编码 */
	public final static String UNICODE = "UNICODE";
	
	/** UTF-8编码 */
	public final static String UTF8 = "UTF-8";
	
	/** GBK编码（繁简） */
	public final static String GBK = "GBK";
	
	/** GB2312编码（简） */
	public final static String GB2312 = "GB2312";
	
	/** ASCII编码（ISO-8859-1） */
	public final static String ASCII = "ISO-8859-1";

	/**
	 * 字节类型
	 */
	public final static String BYTE_ENCODING = "ISO-8859-1";

	/** ISO-8859-1编码  */
	public final static String ISO = "ISO-8859-1";
	
}
