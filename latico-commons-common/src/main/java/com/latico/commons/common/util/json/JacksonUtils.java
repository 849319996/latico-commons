package com.latico.commons.common.util.json;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Jackson的Json数据提取工具
 *
 * Jackson提供了一系列注解，方便对JSON序列化和反序列化进行控制，下面介绍一些常用的注解。
 * @JsonIgnore 此注解用于属性上，作用是进行JSON操作时忽略该属性。
 * @JsonFormat 此注解用于属性上，作用是把Date类型直接转化为想要的格式，如@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")。
 * @JsonProperty 此注解用于属性上，作用是把该属性的名称序列化为另外一个名称，如把trueName属性序列化为name，@JsonProperty("name")。
 *
 * @Author: LanDingDong
 * @Date: 2018/12/16 02:53:26
 * @Version: 1.0
 */
public class JacksonUtils {

	private static final Logger LOG = LoggerFactory.getLogger(JacksonUtils.class);

	/**
	 * 对象映射
	 */
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	/** 私有化构造函数 */
	protected JacksonUtils() {}

	/**
	 * 对象类型转json字符串
	 *
	 * @param obj 对象
	 * @return json字符串
	 */
	public static String objToJson(Object obj) {
		if (obj == null) {
			return "";
		}
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			LOG.error("JSON转换失败", e);
		}
		return null;
	}

	/**
	 * json字符串转泛型对象，不支持非泛型
	 *
	 *
	 List<List<CompareBean>> lists = new ArrayList<>();
	 List<CompareBean> list = new ArrayList<>();
	 CompareBean obj = new CompareBean();
	 obj.setAge(10);
	 obj.setName("name1");
	 obj.setNickName("nickName1");

	 obj = new CompareBean();
	 obj.setAge(101);
	 obj.setName("name2");
	 obj.setNickName("nickName2");
	 list.add(obj);

	 list.add(obj);
	 lists.add(list);


	 String json = JacksonUtils.objToJson(lists);
	 System.out.println(json);

	 lists = JacksonUtils.jsonToObj(json);

	 System.out.println(lists);

	 * @param json          json字符串
	 * @param valueType     new TypeReference<T>(){}
	 * @param <T>           泛型类型
	 * @return 泛型对象
	 */
	public static <T> T jsonToObj(String json, TypeReference<T> valueType) {

		if (StringUtils.isBlank(json)) {
			return null;
		}
		try {
			return objectMapper.readValue(json, valueType);
		} catch (Exception e) {
			LOG.error("JSON转换失败", e);
		}
		return null;
	}

	/**
	 * json字符串转对象类型，不支持泛型类型
	 *
	 * @param json  json字符串
	 * @param clazz 对象类型
	 * @param <T>   泛型
	 * @return 对象
	 */
	public static <T> T jsonToObj(String json, Class<T> clazz) {
		if (StringUtils.isBlank(json) || clazz == null) {
			return null;
		}
		try {
			return clazz.equals(String.class) ? (T) json : objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			LOG.error("JSON转换失败", e);
		}
		return null;
	}

	/**
	 * 拷贝对象
	 * @param obj
	 * @return
	 */
	public static Object copyObj(Object obj) {
		if (obj == null) {
			return null;
		}
		String json = objToJson(obj);
		return jsonToObj(json, obj.getClass());
	}

	/**
	 * 拷贝对象
	 * @param obj
	 * @param targetClass 目标类
	 * @return
	 */
	public static <T> T copyObj(Object obj, Class<T> targetClass) {
		if (obj == null) {
			return null;
		}
		String json = objToJson(obj);
		return jsonToObj(json, targetClass);
	}
}
