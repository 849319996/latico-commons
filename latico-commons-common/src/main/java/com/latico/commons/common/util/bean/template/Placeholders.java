package com.latico.commons.common.util.bean.template;

/**
 * <PRE>
 * 占位符名称定义。
 * 要求所有模板文件中用到的占位符都要先在此处定义，以便管理。
 * </PRE>
 * @Author: latico
 * @Date: 2019-07-16 17:40:02
 * @Version: 1.0
 */
public class Placeholders {

	/**
	 * 年份
	 */
	public final static String year = "year";
	
	/**
	 * 日期
	 */
	public final static String date = "date";
	
	/**
	 * 表名
	 */
	public final static String table_name = "table_name";

	/**
	 * insert语句列
	 */
	public final static String insert_column = "insert_column";
	
	/**
	 * insert语句占位符
	 */
	public final static String insert_column_placeholder = "insert_column_placeholder";
	
	/**
	 * update语句列
	 */
	public final static String update_column = "update_column";
	
	/**
	 * select语句列
	 */
	public final static String select_column = "select_column";
	
	/**
	 * 包路径
	 */
	public final static String package_path = "package_path";
	
	/**
	 * 类名
	 */
	public final static String class_name = "class_name";
	
	/**
	 * 类成员变量
	 */
	public final static String class_member = "class_member";
	
	/**
	 * 预编译insert sql替换占位符的参数
	 */
	public final static String insert_params = "insert_params";
	
	/**
	 * 预编译update sql替换占位符的参数
	 */
	public final static String update_params = "update_params";
	
	/**
	 * getter setter
	 */
	public final static String getter_and_setter = "getter_and_setter";
	
	/**
	 * 获取单个数据库字段域名称的方法
	 */
	public final static String get_column_name = "get_column_name";
	
	/**
	 * 获取单个类成员变量名称的方法
	 */
	public final static String get_java_name = "get_java_name";
	
	/**
	 * 所有数据库字段域名称
	 */
	public final static String all_column_names = "all_column_names";
	
	/**
	 * 所有类成员变量名称
	 */
	public final static String all_java_names = "all_java_names";
	
	/**
	 * toString
	 */
	public final static String to_string = "to_string";
	
	/**
	 * 禁止外部构造，避免误用
	 */
	private Placeholders() {}
	
}
