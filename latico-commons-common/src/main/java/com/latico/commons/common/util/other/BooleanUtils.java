package com.latico.commons.common.util.other;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

import java.util.Collection;

/**
 * <PRE>
 * 布尔数据处理工具
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2018/12/08 20:43:32
 * @Version: 1.0
 */
public class BooleanUtils extends org.apache.commons.lang3.BooleanUtils {
	private static final Logger LOG = LoggerFactory.getLogger(BooleanUtils.class);
	private static final String trueStr = "true";
	private static final String falseStr = "false";


	
	/** 私有化构造函数 */
	protected BooleanUtils() {}
	
	/**
	 * 把字符串转换成bool对象
	 * @param tof "true"或"false"字符串（忽略大小写）
	 * @param defavlt 默认值
	 * @return true或false, 转换失败则返回默认值
	 */
	public static boolean toBool(String tof, boolean defavlt) {
		boolean bool = defavlt;
		try {
			bool = Boolean.parseBoolean(tof.toLowerCase());
		} catch (Exception e) {
			LOG.error("转换 [{}] 为bool类型失败.", tof, e);
		}
		return bool;
	}
	/**
	 * 判断字符串类型是不是公共的true，如果是true或者1就是true
	 * @param str
	 * @return
	 */
	public static boolean isTrue(Object str){
		if(str == null){
			return false;
		}
		if(trueStr.equalsIgnoreCase(str.toString())){
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串类型是不是公共的true，如果是true或者1就是true
	 * @param str
	 * @return
	 */
	public static boolean isTrueOr1(Object str){
		if(str == null){
			return false;
		}
		if(trueStr.equalsIgnoreCase(str.toString()) || "1".equalsIgnoreCase(str.toString())){
			return true;
		}
		return false;
	}
	/**
	 * 判断字符串类型是不是公共的true，如果是true或者1就是true
	 * @param str
	 * @return
	 */
	public static boolean isFalse(Object str){
		if(str == null){
			return false;
		}
		if(falseStr.equalsIgnoreCase(str.toString())){
			return true;
		}
		return false;
	}
	/**
	 * 判断字符串类型是不是公共的true，如果是true或者1就是true
	 * @param str
	 * @return
	 */
	public static boolean isFalseOr0(Object str){
		if(str == null){
			return false;
		}
		if(falseStr.equalsIgnoreCase(str.toString()) || "0".equalsIgnoreCase(str.toString())){
			return true;
		}
		return false;
	}

	/**
	 * 所有都是true
	 * @param booleans
	 * @return
	 */
	public static boolean isAllTrue(Collection<Boolean> booleans){
		if (booleans == null) {
			return false;
		}
		for (Boolean bool : booleans) {
			if(!bool){
				return false;
			}
		}
		return true;
	}
	/**
	 * 所有都是false
	 * @param booleans
	 * @return
	 */
	public static boolean isAllfalse(Collection<Boolean> booleans){
		if (booleans == null) {
			return false;
		}
		for (Boolean bool : booleans) {
			if(bool){
				return false;
			}
		}
		return true;
	}

	/**
	 * 存在有true
	 * @param booleans
	 * @return
	 */
	public static boolean or(Collection<Boolean> booleans){
		if (booleans == null) {
			return false;
		}
		for (Boolean bool : booleans) {
			if(bool){
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>Performs an and on a set of booleans.</p>
	 *
	 * <pre>
	 *   BooleanUtils.and(true, true)         = true
	 *   BooleanUtils.and(false, false)       = false
	 *   BooleanUtils.and(true, false)        = false
	 *   BooleanUtils.and(true, true, false)  = false
	 *   BooleanUtils.and(true, true, true)   = true
	 * </pre>
	 *
	 * @param array  an array of {@code boolean}s
	 * @return {@code true} if the and is successful.
	 * @throws IllegalArgumentException if {@code array} is {@code null}
	 * @throws IllegalArgumentException if {@code array} is empty.
	 * @since 3.0.1
	 */
	public static boolean isAllTrue(final Boolean... array) {
		// Validates input
		if (array == null) {
			throw new IllegalArgumentException("The Array must not be null");
		}
		if (array.length == 0) {
			throw new IllegalArgumentException("Array is empty");
		}
		for (final Boolean element : array) {
			if (!element) {
				return false;
			}
		}
		return true;
	}

	public static boolean isAllFalse(final Boolean... array) {
		// Validates input
		if (array == null) {
			throw new IllegalArgumentException("The Array must not be null");
		}
		if (array.length == 0) {
			throw new IllegalArgumentException("Array is empty");
		}
		for (final Boolean element : array) {
			if (element) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>Performs an or on a set of booleans.</p>
	 *
	 * <pre>
	 *   BooleanUtils.or(true, true)          = true
	 *   BooleanUtils.or(false, false)        = false
	 *   BooleanUtils.or(true, false)         = true
	 *   BooleanUtils.or(true, true, false)   = true
	 *   BooleanUtils.or(true, true, true)    = true
	 *   BooleanUtils.or(false, false, false) = false
	 * </pre>
	 *
	 * @param array  an array of {@code boolean}s
	 * @return {@code true} if the or is successful.
	 * @throws IllegalArgumentException if {@code array} is {@code null}
	 * @throws IllegalArgumentException if {@code array} is empty.
	 * @since 3.0.1
	 */
	public static boolean hasTrue(final Boolean... array) {
		// Validates input
		if (array == null) {
			throw new IllegalArgumentException("The Array must not be null");
		}
		if (array.length == 0) {
			throw new IllegalArgumentException("Array is empty");
		}
		for (final Boolean element : array) {
			if (element) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>Performs an or on a set of booleans.</p>
	 *
	 * <pre>
	 *   BooleanUtils.or(true, true)          = true
	 *   BooleanUtils.or(false, false)        = false
	 *   BooleanUtils.or(true, false)         = true
	 *   BooleanUtils.or(true, true, false)   = true
	 *   BooleanUtils.or(true, true, true)    = true
	 *   BooleanUtils.or(false, false, false) = false
	 * </pre>
	 *
	 * @param array  an array of {@code boolean}s
	 * @return {@code true} if the or is successful.
	 * @throws IllegalArgumentException if {@code array} is {@code null}
	 * @throws IllegalArgumentException if {@code array} is empty.
	 * @since 3.0.1
	 */
	public static boolean hasFalse(final Boolean... array) {
		// Validates input
		if (array == null) {
			throw new IllegalArgumentException("The Array must not be null");
		}
		if (array.length == 0) {
			throw new IllegalArgumentException("Array is empty");
		}
		for (final Boolean element : array) {
			if (!element) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>Performs an xor on a set of booleans.</p>
	 *
	 * <pre>
	 *   BooleanUtils.xor(true, true, true)   = false
	 *   BooleanUtils.xor(false, false, false) = false
	 *   BooleanUtils.xor(true, false, false)  = true
	 * </pre>
	 *
	 * @param array  an array of {@code boolean}s
	 * @return {@code true} if the xor is successful.
	 * @throws IllegalArgumentException if {@code array} is {@code null}
	 * @throws IllegalArgumentException if {@code array} is empty.
	 */
	public static boolean bothTrueAndFalse(final Boolean... array) {
		// Validates input
		if (array == null) {
			throw new IllegalArgumentException("The Array must not be null");
		}
		if (array.length == 0) {
			throw new IllegalArgumentException("Array is empty");
		}

		// false if the neutral element of the xor operator
		boolean hasTrue = false;
		boolean hasFlase = false;
		for (final Boolean element : array) {
			if(element){
				hasTrue = true;
			}else{
				hasFlase = true;
			}
		}

		if(hasTrue && hasFlase){
			return true;
		}else{
			return false;
		}
	}
}
