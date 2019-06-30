package com.latico.commons.common.envm;

/**
 * <PRE>
 *  枚举类：日期格式
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2019-06-30 01:24:40
 * @Version: 1.0
 */
public class DateFormat {

	/** 横杠日期格式： yyyy-MM-dd HH:mm:ss */
	public final static String YYYY_MM_DD_HH_MM_SS_HORIZONTAL_BAR = "yyyy-MM-dd HH:mm:ss";
	
	/** 横杠日期格式： yyyy-MM-dd HH:mm:ss.SSS */
	public final static String YYYY_MM_DD_HH_MM_SS_SSS_HORIZONTAL_BAR = "yyyy-MM-dd HH:mm:ss.SSS";

	/** 斜杠日期格式： yyyy/MM/dd HH:mm:ss */
	public final static String YYYY_MM_DD_HH_MM_SS_SLASH_BAR = "yyyy/MM/dd HH:mm:ss";

	/** 斜杠日期格式： yyyy/MM/dd HH:mm:ss.SSS */
	public final static String YYYY_MM_DD_HH_MM_SS_SSS_SLASH_BAR = "yyyy/MM/dd HH:mm:ss.SSS";
	
	/** GMT日期格式(多用于cookie的有效时间)： EEE, dd MMM yyyy HH:mm:ss z */
	public final static String GMT = "EEE, dd MMM yyyy HH:mm:ss z";

	/**
	 * yyyy-MM-dd
	 */
	public static final String YYYY_MM_DD_HORIZONTAL_BAR = "yyyy-MM-dd";

	/**
	 * 时间：HH:mm:ss
	 */
	public static final String TIME = "HH:mm:ss";
	
}
