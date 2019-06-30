package com.latico.commons.common.envm;

/**
 * <PRE>
 *  枚举类：计算机存储单位
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2019-06-30 01:26:08
 * @Version: 1.0
 */
public enum StorageUnitEnum {

	BIT("bit", "cell: 1bit = 1b", "位"), 
	
	BYTE("byte", "byte: 1byte = 1B = 8bit", "字节"), 
	
	KB("KB", "kilobyte: 1KB = 1024byte", "千字节"), 
	
	MB("MB", "megabyte: 1MB = 1024KB", "兆字节"), 
	
	GB("GB", "gigabyte: 1GB = 1024MB", "吉字节"), 
	
	TB("TB", "trillionbyte: 1TB = 1024GB", "太字节"), 
	
	PB("PB", "petabyte: 1PG = 1024TB", "拍字节"), 
	
	EB("EB", "exabyte: 1EB = 1024PB", "艾字节"), 
	
	ZB("ZB", "zettabyte: 1ZB = 1024EB", "泽字节"), 
	
	YB("YB", "yottabyte: 1YB = 1024ZB", "尧字节"), 
	
	BB("BB", "brontobyte: 1BB = 1024YB", "亿字节"), 
	
	;
	
	public String VAL;
	
	public String DES_EN;
	
	public String DES_CH;
	
	private StorageUnitEnum(String val, String desEn, String desCh) {
		this.VAL = val;
		this.DES_EN = desEn;
		this.DES_CH = desCh;
	}
	
}
