package com.latico.commons.common.envm;

/**
 * <PRE>
 *  元信息中表的类型
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2019-02-07 00:01:59
 * @Version: 1.0
 */
public enum TableType {
	TABLE("TABLE"),
	VIEW("VIEW"),
	SYSTEM_TABLE ("SYSTEM TABLE"),
	GLOBAL_TEMPORARY("GLOBAL TEMPORARY"),
	LOCAL_TEMPORARY("LOCAL TEMPORARY"),
	ALIAS("ALIAS"),
	SYNONYM("SYNONYM");
	
	private String value;
	
	/**
	 * 构造
	 * @param value 值
	 */
	TableType(String value){
		this.value = value;
	}
	/**
	 * 获取值
	 * @return 值
	 */
	public String value(){
		return this.value;
	}
	
	@Override
	public String toString() {
		return this.value();
	}
}
