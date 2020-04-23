package com.latico.commons.common.util.math;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * <PRE>
 * 数值处理工具
 * JDK自带的数学计算工具 {@link Math}
 * </PRE>
 * @author: latico
 * @date: 2019-06-27 11:45:31
 * @version: 1.0
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {
	private static final Logger LOG = LoggerFactory.getLogger(NumberUtils.class);
	/** 最小精度 */
	private final static double PRECISION = 1.0e-6D;
	
	/** 自然底数e */
	public final static double E = Math.E;
	
	/** 圆周率π */
	public final static double PI = Math.PI;
	
	/** 角度转弧度公式常量 */
	private final static double TO_RADIAN = PI / 180;
	
	/** 弧度转角度公式常量 */
	private final static double TO_ANGEL = 180 / PI;
	
	/** 私有化构造函数. */
	protected NumberUtils() {}
	
	/**
	 * 把[浮点数]转换为[百分比格式字符串]
	 * @param n 浮点数
	 * @return 百分比格式字符串
	 */
	public static String numToPrecent(final double n) {
		DecimalFormat df = new DecimalFormat("0.00%");
		return df.format(n);
	}
	
	/**
	 * 把[百分比格式字符串]转换为[浮点数]
	 * @param precent 百分比格式字符串
	 * @return 浮点数
	 */
	public static double precentToNum(String precent) {
		double n = 0;
		if(precent != null) {
			precent = StringUtils.deleteWhitespace(precent);
			precent = precent.replace("%", "");
			n = toDouble(precent);
			n /= 100.0D;
		}
		return n;
	}
	
	/**
	 * 设置浮点数的精度(四舍五入)
	 * @param num 浮点数
	 * @param decimal 最多保留的小数位
	 * @return 改变精度后的浮点数
	 */
	public static double setPrecision(double num, int decimal) {
		BigDecimal bd = new BigDecimal(num);
		return bd.setScale(decimal, BigDecimal.ROUND_HALF_UP).doubleValue();        
	}

	/**
	 * 数字字符串自增1
	 * @param sNum 数字字符串
	 * @return 自增1的数字字符串
	 */
	public static String increment(final String sNum) {
		long num = toLong(sNum) + 1;
		return String.valueOf(num);
	}
	
	/**
	 * 返回[int]的负数
	 * @param n int整数
	 * @return 负数
	 */
	public static int toNegative(int n) {
		return n > 0 ? -n : n;
	}
	
	/**
	 * 返回[long]的负数
	 * @param n long整数
	 * @return 负数
	 */
	public static long toNegative(long n) {
		return n > 0 ? -n : n;
	}
	
	/**
	 * 返回[int]的正数
	 * @param n int整数
	 * @return 正数
	 */
	public static int toPositive(int n) {
		return n < 0 ? -n : n;
	}
	
	/**
	 * 返回[long]的正数
	 * @param n long整数
	 * @return 正数
	 */
	public static long toPositive(long n) {
		return n < 0 ? -n : n;
	}
	
	/**
	 * 返回int最大值
	 * @param a 数字A
	 * @param b 数字B 
	 * @return 最大值
	 */
	public static int max(int a, int b) {
		return a > b ? a : b;
	}
	
	/**
	 * 返回long最大值
	 * @param a 数字A
	 * @param b 数字B 
	 * @return 最大值
	 */
	public static long max(long a, long b) {
		return a > b ? a : b;
	}
	

	/**
	 * 返回int最小值
	 * @param a 数字A
	 * @param b 数字B 
	 * @return 最大值
	 */
	public static int min(int a, int b) {
		return a < b ? a : b;
	}
	
	/**
	 * 返回long最小值
	 * @param a 数字A
	 * @param b 数字B 
	 * @return 最大值
	 */
	public static long min(long a, long b) {
		return a < b ? a : b;
	}
	
	/**
	 * 限制数值的最小最大值
	 * @param num 数值
	 * @param min 最小值
	 * @param max 最大值
	 * @return 若小于最小值则返回最小值; 若大于最大值则返回最大值; 否则返回原值
	 */
	public static int limitRange(int num, int min, int max) {
		if(min > max) {
			min = min ^ max;
			max = min ^ max;
			min = min ^ max;
		}
		return (num < min ? min : (num > max ? max : num));
	}
	
	/**
	 * 限制数值的最小最大值
	 * @param num 数值
	 * @param min 最小值
	 * @param max 最大值
	 * @return 若小于最小值则返回最小值; 若大于最大值则返回最大值; 否则返回原值
	 */
	public static long limitRange(long num, long min, long max) {
		if(min > max) {
			min = min ^ max;
			max = min ^ max;
			min = min ^ max;
		}
		return (num < min ? min : (num > max ? max : num));
	}
	
	/**
	 * <PRE>
	 * int递增序列压缩.
	 * 	例如把 { 1, 2, 3, 5, 6, 8, 10 }
	 *  压缩为 [1-3, 5-6, 8, 10]
	 * </PRE>
	 * @param ascSeries 递增序列(无需连续, 但必须递增)
	 * @return
	 */
	public static List<String> compress(int[] ascSeries) {
		return compress(ascSeries, '-');
	}
	
	/**
	 * <PRE>
	 * int递增序列压缩.
	 * 	例如把 { 1, 2, 3, 5, 6, 8, 10 }
	 *  压缩为 [1-3, 5-6, 8, 10]
	 * </PRE>
	 * @param ascSeries 递增序列(无需连续, 但必须递增)
	 * @param endash 连字符
	 * @return 压缩序列
	 */
	public static List<String> compress(int[] ascSeries, char endash) {
		List<String> cmpNums = new LinkedList<String>();
		if(ascSeries == null || ascSeries.length <= 0) {
			return cmpNums;
		}
		
		int len = ascSeries.length;
		int ps = 0;
		int pe = 0;
		while(ps < len) {
			while(pe + 1 < len && ascSeries[pe] + 1 == ascSeries[pe + 1]) {
				pe++;
			}
			
			int bgn = ascSeries[ps];
			int end = ascSeries[pe];
			if(bgn == end) {
				cmpNums.add(String.valueOf(bgn));
			} else {
				cmpNums.add(StringUtils.join(bgn, endash, end));
			}
			
			ps = ++pe;
		}
		return cmpNums;
	}
	
	/**
	 * <PRE>
	 * 判断双精度数是否为0或近似于0.
	 * 	(默认最小精度为1.0e-6D，绝对值 小于最小精度则判定为0)
	 * </PRE>
	 * @param num 双精度数
	 * @return true: 等于或近似于0; false:非0
	 */
	public static boolean isZero(double num) {
		return (Math.abs(num) < PRECISION)? true : false;
	}
	
	/**
	 * 角度转弧度
	 * @param angel 角度
	 * @return 弧度
	 */
	public static double toRadian(double angel) {
		return angel * TO_RADIAN;
	}
	
	/**
	 * 弧度转角度
	 * @param radian 弧度
	 * @return 角度
	 */
	public static double toAngel(double radian) {
		return radian * TO_ANGEL;
	}
	
	/**
	 * <PRE>
	 * 生成从bgn到end的范围数组.
	 * 	例如:
	 *    从 1...5 的范围数组为 [1, 2, 3, 4, 5]
	 *    从 2...-2 的的范围数组为 [2, 1, 0, -1, 2]
	 * </PRE>
	 * @param bgn 范围数组起始值（包括）
	 * @param end 范围数组终止值（包括）
	 * @return 范围数组
	 */
	public static int[] toRangeArray(int bgn, int end) {
		boolean isAsc = (bgn <= end);
		int[] array = new int[(isAsc ? (end - bgn) : (bgn - end)) + 1];
		for(int i = 0; i < array.length; i++) {
			array[i] = (isAsc ? (bgn + i) : bgn - i);
		}
		return array;
	}
	
	/**
	 * <PRE>
	 * 生成从bgn到end的范围数组.
	 * 	例如:
	 *    从 1...5 的范围数组为 [1, 2, 3, 4, 5]
	 *    从 2...-2 的的范围数组为 [2, 1, 0, -1, 2]
	 * </PRE>
	 * @param bgn 范围数组起始值（包括）
	 * @param end 范围数组终止值（包括）
	 * @return 范围数组
	 */
	public static long[] toRangeArray(long bgn, long end) {
		boolean isAsc = (bgn <= end);
		long[] array = new long[(int) (isAsc ? (end - bgn) : (bgn - end)) + 1];
		for(int i = 0; i < array.length; i++) {
			array[i] = (isAsc ? (bgn + i) : bgn - i);
		}
		return array;
	}
	
	/**
	 * 检查变量的值是否在指定的范围数组内
	 * @param variable 变量
	 * @param ranges 范围数组
	 * @return true:在范围内; false:在范围外
	 */
	public static boolean inIRange(int variable, int... ranges) {
		boolean inRange = false;
		if(ranges != null && ranges.length > 0) {
			for(int i = 0; !inRange && i < ranges.length; i++) {
				inRange = (variable == ranges[i]);
			}
		}
		return inRange;
	}
	
	/**
	 * 检查变量的值是否在指定的范围数组内
	 * @param variable 变量
	 * @param ranges 范围数组
	 * @return true:在范围内; false:在范围外
	 */
	public static boolean inLRange(long variable, long... ranges) {
		boolean inRange = false;
		if(ranges != null && ranges.length > 0) {
			for(int i = 0; !inRange && i < ranges.length; i++) {
				inRange = (variable == ranges[i]);
			}
		}
		return inRange;
	}

	/**
	 * 等于空或者不超过0
	 * @param i
	 * @return
	 */
	public static boolean isEmptyOrNotMoreThan0(Integer i){
		if(i == null || i <= 0){
			return true;
		}
		return false;
	}

	/**
	 * 等于空或者不超过0
	 * @param l
	 * @return
	 */
	public static boolean isEmptyOrNotMoreThan0(Long l){
		if(l == null || l <= 0){
			return true;
		}
		return false;
	}

	/**
	 * 等于空或者不超过0
	 * @param d
	 * @return
	 */
	public static boolean isEmptyOrNotMoreThan0(Double d){
		if(d == null || d <= 0){
			return true;
		}
		return false;
	}
	/**
	 * 等于空或者不超过0
	 * @param f
	 * @return
	 */
	public static boolean isEmptyOrNotMoreThan0(Float f){
		if(f == null || f <= 0){
			return true;
		}
		return false;
	}

	/**
	 * 不为空，且超过0
	 * @param i
	 * @return
	 */
	public static boolean isMoreThan0(Integer i){
		return !isEmptyOrNotMoreThan0(i);
	}

	/**
	 * 不为空，且超过0
	 * @param l
	 * @return
	 */
	public static boolean isMoreThan0(Long l){
		return !isEmptyOrNotMoreThan0(l);
	}

	/**
	 * 不为空且超过0
	 * @param d
	 * @return
	 */
	public static boolean isMoreThan0(Double d){
		return !isEmptyOrNotMoreThan0(d);
	}
	/**
	 * 不为空且超过0
	 * @param f
	 * @return
	 */
	public static boolean isMoreThan0(Float f){
		return !isEmptyOrNotMoreThan0(f);
	}

	/**
	 * 存在不为空，而且超过0
	 * @param ds
	 * @return
	 */
	public static boolean hasNotEmptyAndMoreThan0(Double... ds) {
		return !isAllEmptyOrNotMoreThan0(ds);
	}

	/**
	 * 所有都为空，或者所有都不超过0
	 * @param ds
	 * @return
	 */
	public static boolean isAllEmptyOrNotMoreThan0(Double... ds) {
		if(ds == null){
			return true;
		}

		for(Double d : ds){
			if(isMoreThan0(d)){
				return false;
			}
		}
		return true;
	}
	/**
	 * 所有都超过0
	 * @param ds
	 * @return
	 */
	public static boolean isAllMoreThan0(Double... ds) {
		if(ds == null){
			return false;
		}

		for(Double d : ds){
			if(isEmptyOrNotMoreThan0(d)){
				return false;
			}
		}
		return true;
	}
	/**
	 * 所有都超过0
	 * @param ds
	 * @return
	 */
	public static boolean isAllMoreThan0(Integer... ds) {
		if(ds == null){
			return false;
		}

		for(Integer d : ds){
			if(isEmptyOrNotMoreThan0(d)){
				return false;
			}
		}
		return true;
	}
	/**
	 * 所有都超过0
	 * @param ds
	 * @return
	 */
	public static boolean isAllMoreThan0(Long... ds) {
		if(ds == null){
			return false;
		}

		for(Long d : ds){
			if(isEmptyOrNotMoreThan0(d)){
				return false;
			}
		}
		return true;
	}

	public static BigDecimal toBigDecimal(String str){
		return new BigDecimal(toBigInteger(str));
	}

	public static BigDecimal toBigDecimal(String str, int defaultValue){
		return new BigDecimal(toBigInteger(str, defaultValue));
	}

	public static BigInteger toBigInteger(String str){
		return new BigInteger(str);
	}
	public static BigInteger toBigInteger(String str, int defaultValue){
		return new BigInteger(str, defaultValue);
	}

	/**
	 * 四舍五入
	 * @param d
	 * @return
	 */
	public static Long toLongRound(Double d) {
		if (d == null) {
			return null;
		}
		return Math.round(d);
	}

	/**
	 * 四舍五入
	 * @param f
	 * @return
	 */
	public static Integer toIntRound(Float f) {
		if (f == null) {
			return null;
		}
		return Math.round(f);
	}
	/**
	 * 四舍五入
	 * @param l
	 * @return
	 */
	public static Integer toInt(Long l) {
		if (l == null) {
			return null;
		}
		return Integer.parseInt(l.toString());
	}
}
