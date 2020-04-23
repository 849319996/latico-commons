/**
 * <PRE>
 bean对象差异比较工具
 共有三种方式。
 1、比较的对象需要实现ICompareObj.java类，或者继承AbstractCompareObj.java类，
 然后使用CompareUtils.java中的方法进行差异化比较，比较结果会存进入参的队列中。

 2、使用注解CompareAnnotation标注差异比较的字段。

 3、指定要比较的字段用数组传进来

 * </PRE>
 *
 * @author: latico
 * @date: 2019-02-06 22:49
 * @version: 1.0
 */
package com.latico.commons.common.util.compare;