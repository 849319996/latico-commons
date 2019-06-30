package com.latico.commons.common.util.math;

import java.text.DecimalFormat;

import com.latico.commons.common.envm.StorageUnitEnum;
import com.latico.commons.common.envm.TimeUnitEnum;

/**
 * <PRE>
 * 单位转换工具.
 * </PRE>
 * <B>PROJECT：</B> exp-libs
 * <B>SUPPORT：</B> EXP
 * @version   1.0 2015-12-27
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class UnitUtils {

	/** 获取 [1byte] 的数值表示 (单位:byte) */
	public final static int _1_BYTE = 1;
	
	/** 获取 [1KB] 的数值表示 (单位:byte) */
	public final static int _1_KB = 1024 * _1_BYTE;
	
	/** 获取 [1MB] 的数值表示 (单位:byte) */
	public final static int _1_MB = 1024 * _1_KB;
	
	/** 获取 [1GB] 的数值表示 (单位:byte) */
	public final static int _1_GB = 1024 * _1_MB;
	
	/** 获取 [1TB] 的数值表示 (单位:byte) */
	public final static long _1_TB = 1024L * _1_GB;
	
	/** 私有化构造函数 */
	protected UnitUtils() {}
	
	/**
	 * 字节单位转换
	 * @param bytes 字节大小
	 * @return 根据字节大小自动调整为byte、KB、MB等单位字符串
	 */
	public String convertBytes(long bytes) {
		double size = (double) bytes;
		String unit = StorageUnitEnum.BYTE.VAL;
		
		if(size >= 1024 && StorageUnitEnum.BYTE.VAL.equals(unit)) {
			size = size / 1024.0;
			unit = StorageUnitEnum.KB.VAL;
		}
		
		if(size >= 1024 && StorageUnitEnum.KB.VAL.equals(unit)) {
			size = size / 1024.0;
			unit = StorageUnitEnum.MB.VAL;
		}
		
		if(size >= 1024 && StorageUnitEnum.MB.VAL.equals(unit)) {
			size = size / 1024.0;
			unit = StorageUnitEnum.GB.VAL;
		}
		
		if(size >= 1024 && StorageUnitEnum.GB.VAL.equals(unit)) {
			size = size / 1024.0;
			unit = StorageUnitEnum.TB.VAL;
		}
		
		if(size >= 1024 && StorageUnitEnum.TB.VAL.equals(unit)) {
			size = size / 1024.0;
			unit = StorageUnitEnum.PB.VAL;
		}
		
		if(size >= 1024 && StorageUnitEnum.PB.VAL.equals(unit)) {
			size = size / 1024.0;
			unit = StorageUnitEnum.EB.VAL;
		}
		
		if(size >= 1024 && StorageUnitEnum.EB.VAL.equals(unit)) {
			size = size / 1024.0;
			unit = StorageUnitEnum.ZB.VAL;
		}
		
		if(size >= 1024 && StorageUnitEnum.ZB.VAL.equals(unit)) {
			size = size / 1024.0;
			unit = StorageUnitEnum.YB.VAL;
		}
		
		if(size >= 1024 && StorageUnitEnum.YB.VAL.equals(unit)) {
			size = size / 1024.0;
			unit = StorageUnitEnum.BB.VAL;
		}
		return new DecimalFormat("0.00 " + unit).format(size);
	}
	
	/**
	 * byte -> KB
	 * @param bytes 字节大小
	 * @return KB大小
	 */
	public static double toKB(long bytes) {
		return bytes / 1024.0;
	}
	
	/**
	 * byte -> MB
	 * @param bytes 字节大小
	 * @return MB大小
	 */
	public static double toMB(long bytes) {
		return toKB(bytes) / 1024.0;
	}
	
	/**
	 * byte -> GB
	 * @param bytes 字节大小
	 * @return GB大小
	 */
	public static double toGB(long bytes) {
		return toMB(bytes) / 1024.0;
	}
	
	/**
	 * byte -> TB
	 * @param bytes 字节大小
	 * @return TB大小
	 */
	public static double toTB(long bytes) {
		return toGB(bytes) / 1024.0;
	}
	
	/**
	 * 毫秒单位转换
	 * @param millis 毫秒值
	 * @return 根据毫秒值大小自动调整为ms、s、minute、hour、day等单位字符串
	 */
	public static String convertMills(long millis) {
		double time = millis;
		String unit = TimeUnitEnum.MS.VAL;
		
		if(time >= 1000 && TimeUnitEnum.MS.VAL.equals(unit)) {
			time = time / 1000.0;
			unit = TimeUnitEnum.SECOND.VAL;
		}
		
		if(time >= 60 && TimeUnitEnum.SECOND.VAL.equals(unit)) {
			time = time / 60.0;
			unit = TimeUnitEnum.MINUTE.VAL;
		}
		
		if(time >= 60 && TimeUnitEnum.MINUTE.VAL.equals(unit)) {
			time = time / 60.0;
			unit = TimeUnitEnum.HOUR.VAL;
		}
		
		if(time >= 24 && TimeUnitEnum.HOUR.VAL.equals(unit)) {
			time = time / 24.0;
			unit = TimeUnitEnum.DAY.VAL;
		}
		return new DecimalFormat("0.00 " + unit).format(time);
	}
	
	/**
	 * millis -> second
	 * @param millis 毫秒值
	 * @return 秒值
	 */
	public static double toSecond(long millis) {
		return millis / 1000.0;
	}
	
	/**
	 * millis -> minute
	 * @param millis 毫秒值
	 * @return 分钟
	 */
	public static double toMinute(long millis) {
		return millis / 60000.0;
	}
	
	/**
	 * millis -> hour
	 * @param millis 毫秒值
	 * @return 小时
	 */
	public static double toHour(long millis) {
		return millis / 3600000.0;
	}
	
	/**
	 * millis -> day
	 * @param millis 毫秒值
	 * @return 天
	 */
	public static double toDay(long millis) {
		return millis / 86400000.0;
	}
	
}
