package com.latico.commons.common.util.compare.extendsmode;


import com.latico.commons.common.util.codec.MD5Utils;

/**
 * <PRE>
 * 抽象的比较对象
 * 1、某些对象不需要利用getUniqueColumnValue()和setUniqueColumnValue()方法；
 * 2、比较的值默认使用子类的toString()方法获取；
 *
 * 已过时，请使用注解方式
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年3月30日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
@Deprecated
public abstract class AbstractCompareObj implements CompareObj {

	/**
	 * 默认为空
	 */
	@Override
	public Object getAutoIncrementFieldValue() {
		return null;
	}

	/**
	 * 默认不操作
	 */
	@Override
	public void setAutoIncrementFieldValue(Object fieldValue) {
	}

	/**
	 * 默认使用toString()方法后计算MD5
	 */
	@Override
	public String getCompareRelatedValue() {
		return MD5Utils.toUpperCaseMd5(toString());
	}

}
