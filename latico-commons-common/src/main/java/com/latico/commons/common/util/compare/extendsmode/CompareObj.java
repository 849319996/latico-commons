package com.latico.commons.common.util.compare.extendsmode;

/**
 * <PRE>
 * 对象比较的接口对象
 * 已过时，请使用注解方式
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年4月8日</B>
 * @author    <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @since     <B>JDK1.6</B>
 */
@Deprecated
public interface CompareObj {
	
	/**
	 * 获取唯一的表的列，默认为空，主要用于像Mysql这种数据库有自增列的情况，这样比较的时候，假如是更新的数据，那就需要把自增的列的值传进来
	 * @return
	 */
	public Object getAutoIncrementFieldValue();
	
	/**
	 * 设置自增列的值
	 * @param fieldValue
	 */
	public void setAutoIncrementFieldValue(Object fieldValue);
	
	/**
	 * 比较时关联用的值，一般是一个列，但是要根据实际情况进行组拼
	 * @return
	 */
	public String getComapreRelatedKey();
	
	/**
	 * 比较的值，建议使用子类对象复写后的toString()方法并且计算MD5。
	 * @return
	 */
	public String getCompareRelatedValue();
}
