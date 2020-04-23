package com.latico.commons.common.util.reflect;

import com.latico.commons.common.envm.DateFormat;
import com.latico.commons.common.util.collections.ArrayUtils;
import com.latico.commons.common.util.collections.CollectionUtils;
import com.latico.commons.common.util.collections.MapUtils;
import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;
import com.latico.commons.common.util.time.DateTimeUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;
import java.util.zip.DataFormatException;

/**
 * <PRE>
 *  对象工具
 * </PRE>
 * @author: latico
 * @date: 2018/12/16 03:00:33
 * @version: 1.0
 */
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

	/** LOG 日志工具 */
	private final static Logger LOG = LoggerFactory.getLogger(ObjectUtils.class);
	
	/** 私有化构造函数 */
	protected ObjectUtils() {}

	/**
	 * 实例化对象
	 * @param clazzPath 类路径, 如: foo.bar.Test （该类必须支持无参构造函数）
	 * @return 实例化对象（若失败则返回null）
	 */
	public static Object instanceClass(String clazzPath) {
		Object inst = null;
		try {
			Class<?> clazz = Class.forName(clazzPath);
			inst = clazz.newInstance();
		} catch(Exception e) {
			LOG.error("实例化类 [{}] 失败", clazzPath, e);
		}
		return inst;
	}
	/**
	 * 实例化对象
	 * @param clazz 类（该类必须支持无参构造函数）
	 * @return 实例化对象（若失败则返回null）
	 */
	public static <T> T instanceClass(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch(Exception e) {
			LOG.error("实例化类 [{}] 失败", clazz, e);
		}
		return null;
	}

	/**
	 * 实例化类，指定字段和值
	 * @param clazz
	 * @param fieldNames
	 * @param fieldValues
	 * @return
	 * @param <T>
	 * @throws Exception
	 */
	public static <T> T instanceClass(Class<T> clazz, String[] fieldNames, Object[] fieldValues) throws Exception {
		if(clazz == null || fieldNames == null || fieldValues == null){
			throw new NullPointerException("存入数据有null");
		}

		if (fieldNames.length != fieldValues.length) {
			throw new DataFormatException("字段名的数组跟字段值的数组长度不一样," + fieldNames.length + ":" + fieldValues.length);
		}

		Map<String, Field> allFields = FieldUtils.getAllFieldNameMap(clazz, true);
		T obj = clazz.newInstance();
		for (int i = 0; i < fieldNames.length; i++) {
			Field field = allFields.get(fieldNames[i]);
			if (field != null) {
				field.set(obj, fieldValues[i]);
			}
		}

		return obj;
	}

	/**
	 * 实例化类，指定字段和值，List装着<code>fieldValuesList</code>长度个要生成对象的字段值
	 * @param clazz 被实例化的类
	 * @param fieldNames 类的字段名
	 * @param fieldValuesList <code>fieldValuesList</code>长度个对象的字段值
	 * @return <code>fieldValuesList</code>长度个对象
	 * @param <T>
	 * @throws Exception
	 */
	public static <T> List<T> instanceClass(Class<T> clazz, String[] fieldNames, List<Object[]> fieldValuesList) throws Exception {
		if(clazz == null){
			throw new NullPointerException("存入数据有null");
		}

		boolean isSetValue = false;
		if(ArrayUtils.isNotEmpty(fieldNames) && CollectionUtils.isNotEmpty(fieldValuesList) && fieldNames.length == fieldValuesList.get(0).length){
			isSetValue = true;
		}
		List<T> objs = new ArrayList<T>();
		Map<String, Field> allFields = FieldUtils.getAllFieldNameMap(clazz, true);
		for (Object[] fieldValues : fieldValuesList) {
			T obj = clazz.newInstance();
			objs.add(obj);

			if(isSetValue){
				for (int i = 0; i < fieldNames.length; i++) {
					Field field = allFields.get(fieldNames[i]);
					if (field != null) {
						field.set(obj, fieldValues[i]);
					}
				}
			}

		}

		return objs;
	}

	/**
	 * 实例化类，可以传入字段名和值的Map
	 * @param clazz 要实例化的类
	 * @param fieldNameValueMap 字段名和值的Map，可以为空
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	public static <T> T instanceClass(Class<T> clazz, Map<String, Object> fieldNameValueMap) throws Exception {
		if(clazz == null){
			throw new NullPointerException("存入数据有null");
		}
		T obj = clazz.newInstance();
		if (MapUtils.isEmpty(fieldNameValueMap)) {
			return obj;
		}

		Map<String, Field> allFields = FieldUtils.getAllFieldNameMap(clazz, true);
		for (Map.Entry<String, Object> entry : fieldNameValueMap.entrySet()) {
			Field field = allFields.get(entry.getKey());
			if (field != null) {
				field.setAccessible(true);
				field.set(obj, entry.getValue());
			}
		}

		return obj;
	}

	/**
	 * 实例化类，可以传入字段名和值的Map,
	 * @param clazz 要实例化的类
	 * @param fieldNameValueMapList 字段名和值的Map，可以为空，List的大小说明生成对象的数量
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> instanceClass(Class<T> clazz, List<Map<String, Object>> fieldNameValueMapList) throws Exception {
		if(clazz == null){
			throw new NullPointerException("存入数据有null");
		}

		boolean isSetValue = false;
		if(CollectionUtils.isNotEmpty(fieldNameValueMapList)){
			isSetValue = true;
		}
		List<T> objs = new ArrayList<T>();

		Map<String, Field> allFields = FieldUtils.getAllFieldNameMap(clazz, true);
		for (Field field : allFields.values()) {
			field.setAccessible(true);
		}

		for (Map<String, Object> fieldNameValueMap : fieldNameValueMapList) {
			T obj = clazz.newInstance();
			objs.add(obj);

			if (isSetValue) {
				for (Map.Entry<String, Object> entry : fieldNameValueMap.entrySet()) {
					Field field = allFields.get(entry.getKey());
					if (field != null) {
						field.set(obj, entry.getValue());
					}
				}
			}

		}

		return objs;
	}

	/**
	 * <PRE>
	 * 把String对象转换成其他实体对象.
	 * 	允许转换的对象包括：Integer、Long、BigInteger、Float、Double、Date、Timestamp
	 * </PRE>
	 * @param s String对象
	 * @param clazz 期望转换的对象类
	 * @return 允许转换的对象(不支持转换则返回String)
	 */
	public static Object toObj(String s, Class<?> clazz) throws Exception {
		if(StringUtils.isEmpty(s)) {
			return (String.class == clazz ? "" : null);
		}
		
		Object o = s;
		String str = s.trim();
		if (Integer.class == clazz) {
			o = Integer.valueOf(str);
			
		} else if (Long.class == clazz) {
			o = Long.valueOf(str);
			
		} else if (BigInteger.class == clazz) {
			o = new BigInteger(str);
			
		} else if (Float.class == clazz) {
			o = Float.valueOf(str);
			
		} else if (Double.class == clazz) {
			o = Double.valueOf(str);
			
		} else if (Date.class == clazz) {
			o = DateTimeUtils.toDateBy_ymdhms(str);
			
		} else if (Timestamp.class == clazz) {
			o = DateTimeUtils.toTimestamp(DateTimeUtils.toDateBy_ymdhms(str));
		}
		return o;
	}
	
	/**
	 * <PRE>
	 * 把其他实体对象转换成String.
	 * 	(对于Date和Timestamp对象会返回 yyyy-MM-dd HH:mm:ss.SSS格式字符串)
	 * </PRE>
	 * @param o 被转换的实体对象
	 * @param clazz 被转换的实体对象类型
	 * @return String对象(若转换失败返回"")
	 */
	public static String toStr(Object o, Class<?> clazz) {
		String s = "";
		if(o == null) {
			return s;
		}
		
		if(clazz == null) {
			return toStr(o);
		}
		
		if (ClassUtils.isSubclass(clazz, Number.class)) {
			s = String.valueOf(o);
			
		} else if (Date.class == clazz) {
			s = DateTimeUtils.toStr((Date) o, DateFormat.YYYY_MM_DD_HH_MM_SS_SSS_HORIZONTAL_BAR);
			
		} else if (Timestamp.class == clazz) {
			Date date = DateTimeUtils.toDate((Timestamp) o);
			s = DateTimeUtils.toStr(date, DateFormat.YYYY_MM_DD_HH_MM_SS_SSS_HORIZONTAL_BAR);
			
		} else {
			s = o.toString();
		}
		return s;
	}
	
	/**
	 * <PRE>
	 * 把其他实体对象转换成String.
	 * 	(对于Date和Timestamp对象会返回 yyyy-MM-dd HH:mm:ss.SSS格式字符串)
	 * </PRE>
	 * @param o 被转换的实体对象
	 * @return String对象(若转换失败返回"")
	 */
	public static String toStr(Object o) {
		String s = "";
		if(o == null) {
			return s;
		}
		
		if (o instanceof Number) {
			s = String.valueOf(o);
			
		} else if (o instanceof Date) {
			s = DateTimeUtils.toStr((Date) o, DateFormat.YYYY_MM_DD_HH_MM_SS_SSS_HORIZONTAL_BAR);
			
		} else if (o instanceof Timestamp) {
			Date date = DateTimeUtils.toDate((Timestamp) o);
			s = DateTimeUtils.toStr(date, DateFormat.YYYY_MM_DD_HH_MM_SS_SSS_HORIZONTAL_BAR);
			
		} else {
			s = o.toString();
		}
		return s;
	}
	
	/**
	 * <pre>
	 * 通过Serializable序列化方式深度克隆对象，
	 * 要求所克隆的对象及其下所有成员都要实现Serializable接口。
	 * 
	 * 因为java的[基本数据类型]是值传递，可以直接复制，
	 * 而其[包装类]（如String, Integer等）也都已经实现了Serializable接口，
	 * 因此对于一般的待克隆对象，实现Serializable接口后，直接使用即可。
	 * 
	 * 若待克隆对象下存在[引用数据类型]（如自定义的class），则要求它必须实现Serializable接口。
	 * </pre>
	 * @param obj 被克隆的对象(必须实现Serializable接口)
	 * @return 克隆的对象
	 */
	public static Object cloneBySerial(Object obj) {
		if(obj == null) {
			return null;
		}

		Object newObj = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(obj);
			out.close();
			
			ByteArrayInputStream bin = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream in = new ObjectInputStream(bin);
			newObj = in.readObject();
			in.close();
			
		} catch (Exception e) {
			LOG.debug("克隆对象 [{}] 失败.", obj, e);
		}
		return newObj;
	}

	/**
	 * <PRE>
	 * 通过反射调用对象内部方法.
	 * 	(私有方法也可调用, 可用于单元测试)
	 * </PRE>
	 * @param instnOrClazz
	 *            如果是调用实例方法，该参数为实例对象，
	 *            如果调用静态方法，该参数为实例对象或对应类***.class
	 * @param methodName 调用的方法名
	 * @param paramVals 调用方法的参数
	 * @param valClazzs 调用方法的参数对应的类型类
	 * @return 调用结果
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object invokeMethod(Object instnOrClazz, String methodName,
			Object[] paramVals, Class[] valClazzs) {
		if(instnOrClazz == null || StringUtils.isEmpty(methodName)) {
			LOG.debug("反射调用方法失败: [{}.{}()], 无效的类或方法.",
					instnOrClazz, methodName);
			return null;
		}
		
		Class clazz = (instnOrClazz instanceof Class ? 
				(Class) instnOrClazz : instnOrClazz.getClass());
		paramVals = (paramVals == null ? new Object[0] : paramVals);
		Class[] valTypes = (valClazzs == null ? new Class[0] : valClazzs);
		
		if(paramVals.length > valTypes.length) {
			if(valTypes.length <= 0) {
				valTypes = new Class[paramVals.length];
				for (int i = 0; i < paramVals.length; i++) {
					valTypes[i] = (paramVals[i] != null ? 
							paramVals[i].getClass() : Object.class);
				}
			} else {
				LOG.debug("反射调用方法失败: [{}.{}()], 入参与类型的个数不一致.",
						clazz, methodName);
				return null;
			}
		}
		
		Object result = null;
		try {
			Method method = clazz.getDeclaredMethod(methodName, valTypes);
			method.setAccessible(true);	// 临时开放调用权限(针对private方法)
			result = method.invoke(instnOrClazz, paramVals);
			
		} catch (Exception e) {
			LOG.debug("反射调用方法失败: [{}.{}()]", clazz, methodName, e);
		}
		return result;
	}
	
	/**
	 * 生成Bean中的所有成员域的KV对信息（使用MULTI_LINE_STYLE风格）
	 * @param bean bean对象
	 * @return 所有成员域的KV对信息
	 */
	public static String toBeanInfo(Object bean) {
		return toBeanInfo(bean, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	/**
	 * 生成Bean中的所有成员域的KV对信息
	 * @param bean bean对象
	 * @param style 打印风格, 建议值 MULTI_LINE_STYLE
	 * @return 所有成员域的KV对信息
	 */
	public static String toBeanInfo(Object bean, ToStringStyle style) {
		String info = "";
		if(bean != null) {
			info = new ReflectionToStringBuilder(bean, style).toString();
		}
		return info;
	}
	
	/**
	 * 把内存对象序列化并保存到外存文件
	 * @param o 内存对象（需实现Serializable接口）
	 * @param outFilePath 外存文件位置
	 * @return true:序列化成功; false:序列化失败
	 */
	public static boolean toSerializable(Serializable o, String outFilePath) {
		boolean isOk = false;
		if(o == null) {
			return isOk;
		}
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(outFilePath));
			oos.writeObject(o);
			oos.flush();
			oos.close();
			isOk = true;
			
		} catch (Exception e) {
			LOG.debug("序列化对象到外存文件失败: [{}]", outFilePath, e);
		}
		return isOk;
	}
	
	/**
	 * 反序列化外存文件，还原为内存对象
	 * @param inFilePath 外存序列化文件
	 * @return 内存对象(失败返回null)
	 */
	public static Object unSerializable(String inFilePath) {
		Object o = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(
					new FileInputStream(inFilePath));
			o = ois.readObject();
			ois.close();
			
		} catch (Exception e) {
			LOG.debug("从外存文件反序列化对象失败: [{}]", inFilePath, e);
		}
		return o;
	}
	
	/**
	 * 把内存对象序列化为byte[]字节数组
	 * @param object 内存对象（需实现Serializable接口）
	 * @return byte[]字节数组 （失败返回null）
	 */
	public static byte[] toSerializable(Serializable object) {
		byte[] bytes = null;
		if(object == null) {
			return bytes;
		}
		
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			bytes = baos.toByteArray();
			
		} catch (Exception e) {
			LOG.debug("序列化对象为字节数组失败", e);
			
		} finally {
			IOUtils.close(oos);
			IOUtils.close(baos);
		}
		return bytes;

	}

	/**
	 * 反序列化byte[]字节数组，还原为内存对象
	 * @param bytes 序列化的字节数组
	 * @return 内存对象(失败返回null)
	 */
	public static Object unSerializable(byte[] bytes) {
		Object object = null;
		if(bytes == null) {
			return object;
		}
		
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			object = ois.readObject();
			
		} catch (Exception e) {
			LOG.debug("从字节数组反序列化对象失败", e);
			
		} finally {
			IOUtils.close(ois);
			IOUtils.close(bais);
		}
		return object;
	}

	/**
	 * 获取对象的所有字段名字和值的Map
	 * <pre>
	 *     TestObjectUitlsBean2 testObjectUitlsBean2 = new TestObjectUitlsBean2();
	 *
	 *         testObjectUitlsBean2.setAge(12);
	 *         testObjectUitlsBean2.setName("xiaoming");
	 *         testObjectUitlsBean2.setDate(new Timestamp(System.currentTimeMillis()));
	 *         testObjectUitlsBean2.setNumber(23356);
	 *
	 *         System.out.println(ObjectUtils.getAllFieldNameValueMap(testObjectUitlsBean2));
	 * </pre>
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getAllFieldNameValueMap(Object obj) throws Exception {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(obj == null){
			return map;
		}
		Map<String, Field> allFields = FieldUtils.getAllFieldNameMap(obj.getClass(), true);
		for (Map.Entry<String, Field> entry : allFields.entrySet()) {
			Field field = entry.getValue();
			field.setAccessible(true);
			map.put(field.getName(), field.get(obj));
		}
		map.remove("serialVersionUID");
		return map;
	}
	/**
	 * 根据Map，把跟Map的key同名的字段写进对象里面，只支持简单类型的字段
	 * @param instance 对象实例
	 * @param fieldNameValueMap 字段名和值的Map
	 * @throws Exception
	 */
	public static void writeValueToSimpleField(Object instance, Map<String, ?> fieldNameValueMap) throws Exception {
		Map<String, Field> allFieldNameMap = FieldUtils.getAllFieldNameMap(instance.getClass(), true);
		writeValueToSimpleField(instance, allFieldNameMap, fieldNameValueMap);
	}

	/**
	 * 根据Map，把跟Map的key同名的字段写进对象里面，只支持简单类型的字段
	 * @param instance 对象实例
	 * @param fieldNameValueMap 字段名和值的Map
	 * @throws Exception
	 */
	public static void writeValueToSimpleField(Object instance, Map<String, Field> fieldNameFieldMap, Map<String, ?> fieldNameValueMap) throws Exception {

		if (fieldNameFieldMap == null || fieldNameFieldMap.isEmpty()) {
			return;
		}
		if (fieldNameValueMap == null || fieldNameValueMap.isEmpty()) {
			return;
		}

		for (Map.Entry<String, ?> entry : fieldNameValueMap.entrySet()) {
			Field field = fieldNameFieldMap.get(entry.getKey());
			if (field == null) {
				LOG.warn("类{}实例指定字段名称:[{}], 找不到字段", instance.getClass().getName(), entry.getKey());
				continue;
			}

			//如果为空，不注入
			if(StringUtils.isBlank(entry.getValue())){
				continue;
			}
			//检测字段的类型，注入数据
			FieldUtils.writeFieldWithCheckType(field, instance, entry.getValue(), true);
		}
	}

	/**
	 * 把对象转换成字节
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static byte[] toBytes(Object object) throws IOException {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			return baos.toByteArray();
		} catch(Exception e) {
			throw e;
		} finally {
			IOUtils.close(oos);
			IOUtils.close(baos);
		}
	}

	/**
	 * 把字节转换成对象
	 * @param bytes 对象的字节数组形式
	 * @return
	 */
	public static Object toObject(byte[] bytes) throws Exception {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch(Exception e) {
			throw e;
		} finally {
			IOUtils.close(ois);
			IOUtils.close(bais);
		}
	}
}
