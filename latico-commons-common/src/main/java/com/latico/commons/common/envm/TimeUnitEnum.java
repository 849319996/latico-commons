package com.latico.commons.common.envm;

/**
 * <PRE>
 *  枚举类：计算机存储单位
 * </PRE>
 * @author: latico
 * @date: 2019-06-30 01:26:17
 * @version: 1.0
 */
public enum TimeUnitEnum {

	NS("ns", "cell: nanosecond", "纳秒"), 
	
	MS("ms", "microsecond: 1ms = 1000ns", "微秒"), 
	
	SECOND("s", "second: 1s = 1000ms", "秒"), 
	
	MINUTE("min", "minute: 1min = 60s", "分"), 
	
	HOUR("H", "hour: 1H = 60min", "时"), 
	
	DAY("d", "day: 1d = 24H", "天"), 
	
	MONTH("M", "month: 1M = 28/29/30/31d", "月"), 
	
	YEAR("y", "year: 1y = 12M = 365/366d", "年"), 
	
	;
	
	public String VAL;
	
	public String DES_EN;
	
	public String DES_CH;
	
	private TimeUnitEnum(String val, String desEn, String desCh) {
		this.VAL = val;
		this.DES_EN = desEn;
		this.DES_CH = desCh;
	}
	
}
