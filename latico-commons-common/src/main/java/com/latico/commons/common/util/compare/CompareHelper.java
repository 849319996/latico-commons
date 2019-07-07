package com.latico.commons.common.util.compare;


import com.latico.commons.common.util.collections.CollectionUtils;
import com.latico.commons.common.util.compare.annotationmode.ComapreAutoIncrementFieldAnnotation;
import com.latico.commons.common.util.compare.annotationmode.ComapreKeyFieldAnnotation;
import com.latico.commons.common.util.compare.annotationmode.ComapreValueFieldAnnotation;
import com.latico.commons.common.util.compare.annotationmode.CompareAnnotation;
import com.latico.commons.common.util.compare.extendsmode.CompareObj;
import com.latico.commons.common.util.codec.MD5Utils;
import com.latico.commons.common.util.reflect.ClassUtils;
import com.latico.commons.common.util.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <PRE>
 * 队列差异比较工具，支持对于像mysql的的自增序列ID的传递
 *
 * 共有两种方式。
 * 1、比较的对象需要实现ICompareObj.java类，或者继承AbstractCompareObj.java类，
 * 然后使用CompareHelper中的方法进行差异化比较，比较结果会存进入参的队列中。
 *
 * 2、使用注解CompareAnnotation标注差异比较的字段。
 * 3、差异比较工具支持注解方式标志字段，建议使用{@link ComapreAutoIncrementFieldAnnotation} {@link ComapreKeyFieldAnnotation} {@link ComapreValueFieldAnnotation}，
 * 比使用{@link CompareAnnotation}里面写死字段名称指定更方便
 *
 * examples:
 *
 List<CompareBean> newObjs = new ArrayList<>();
 List<CompareBean> oldObjs = new ArrayList<>();
 CompareBean obj = null;
 int id = 0;

 obj = new CompareBean();
 newObjs.add(obj);
 id = 11;
 obj.setName("name" + id);
 obj.setNickName("nickName" + id);
 obj.setSex("男");
 obj.setAge(id);

 obj = new CompareBean();
 newObjs.add(obj);
 id = 12;
 obj.setName("name" + id);
 obj.setNickName("nickName" + id);
 obj.setSex("男");
 obj.setAge(id);

 obj = new CompareBean();
 oldObjs.add(obj);
 id = 11;
 obj.setId(id);
 obj.setName("name" + id);
 obj.setNickName("nickName" + id);
 obj.setSex("男");
 obj.setAge(id);

 obj = new CompareBean();
 oldObjs.add(obj);
 id = 12;
 obj.setId(id);
 obj.setName("name" + id);
 obj.setNickName("nickName" + id);
 obj.setSex("女");
 obj.setAge(id);

 System.out.println(CompareHelper.diffCompareDataByAnnotion(newObjs, oldObjs));
 *
 * </PRE>
 * @Author: latico
 * @Date: 2019-05-28 23:34:07
 * @Version: 1.0
 */
public class CompareHelper {

	/**
	 * 通过注解的方式，对象差异比较
	 * 无论是新适配对象还是旧的适配对象，生成比较结果的时候，会进行去重
	 * @param newObjs 新适配对象列表
	 * @param oldObjs 旧对象列表
	 * @return 比较结果，包括增加、更新、删除的对象
	 * @param <T>
	 * @throws Exception
	 */
	public static <T extends Object> CompareResult<T> diffCompareDataByAnnotion(Collection<T> newObjs, Collection<T> oldObjs) throws Exception {
		//比较结果
		CompareResult<T> compareResult = new CompareResult<T>();
		if (CollectionUtils.isEmpty(newObjs) && CollectionUtils.isEmpty(oldObjs)) {
			return compareResult;
		}

		Class clazz = getCompareClass(newObjs, oldObjs);
		CompareAnnotation annotation = ClassUtils.getAnnotationPresentOnClass(clazz, CompareAnnotation.class);
		if (annotation == null) {
			throw new NullPointerException("要比较的对象类型需要指定CompareAnnotation注解");
		}

		//自动递增字段名称
		String autoIncrementFieldName = annotation.autoIncrementFieldName();

		//比较的关联key的字段名称
		String[] comapreKeyRelatedFieldNames = annotation.comapreKeyRelatedFieldNames();

		//比较的比较值字段名称
		String[] compareValueRelatedFieldNames = annotation.compareValueRelatedFieldNames();

		return diffCompareData(newObjs, oldObjs, autoIncrementFieldName, comapreKeyRelatedFieldNames, compareValueRelatedFieldNames);

	}

	/**
	 * 对象差异比较
	 * 无论是新适配对象还是旧的适配对象，生成比较结果的时候，会进行去重
	 *
	 * 过时，请参考建议使用
	 * {@link CompareHelper#diffCompareDataByAnnotion(Collection, Collection)}
	 * {@link CompareHelper#diffCompareData(Collection, Collection, String, String[], String[])}
	 *
	 * @param newObjs 新适配对象列表
	 * @param oldObjs 旧对象列表
	 * @return 比较结果，包括增加、更新、删除的对象
	 * @param <T>
	 */
	@Deprecated
	public static <T extends CompareObj> CompareResult<T> diffCompareDataByExtends(Collection<T> newObjs, Collection<T> oldObjs){
		//比较结果
		CompareResult<T> compareResult = new CompareResult<T>();
		if (CollectionUtils.isEmpty(newObjs) && CollectionUtils.isEmpty(oldObjs)) {
			return compareResult;
		}

		if(newObjs != null){
			compareResult.setNewCount(newObjs.size());
		}
		if(oldObjs != null){
			compareResult.setOldCount(oldObjs.size());
		}
		
		//没有新数据时那全部旧数据应该添加到删除队列中
		if(newObjs == null || newObjs.size() == 0){
			compareResult.addDeleteObjs(oldObjs);
			return compareResult;
		}
		
		// addKeys 添加的key，用于添加到增加队列的的时候去重 */
		Set<String> addKeys = new HashSet<String>();
		// updateKeys 更新的Key，用于添加到更新队列的时候去重 */
		Set<String> updateKeys = new HashSet<String>();
		// sameKeys 相同对象的Key，用于添加到相同队列的时候去重 */
		Set<String> sameKeys = new HashSet<String>();
		
		//如果老数据为空，也就是全新
		if(oldObjs == null || oldObjs.size() == 0){
			for(T obj : newObjs){
				//不包含的时候才添加
				if(!addKeys.contains(obj.getComapreRelatedKey())){
					compareResult.addNewObj(obj);
					addKeys.add(obj.getComapreRelatedKey());
				}else{
					compareResult.addRepeatedObj(obj);
				}
			}
			return compareResult;
		}
		
		//计算新老数据比例，同时判断是否超过门限
		compareResult.setNewOldCountRatio(newObjs.size()/(double)oldObjs.size());

		//组装old数据成Map形式
		Map<String, T> oldMap = new HashMap<String, T>();
		for(T obj : oldObjs){
			
			//如果包含了的话就添加进删除队列，用于删除现有库中重复数据
			if(oldMap.containsKey(obj.getComapreRelatedKey())){
				compareResult.addDeleteObj(obj);
				
			}else{
				oldMap.put(obj.getComapreRelatedKey(), obj);
			}
		}
		
		//遍历新数据，获取到新数据和需要更新的数据，从oldMap中移除出关联得上的数据
		for(T newObj : newObjs){
			String newComapreRelatedKey = newObj.getComapreRelatedKey();
			//如果在新增或者更新中出现，那么就是重复对象
			if (addKeys.contains(newComapreRelatedKey) || updateKeys.contains(newComapreRelatedKey) || sameKeys.contains(newComapreRelatedKey)) {
				compareResult.addRepeatedObj(newObj);
				continue;
			}

			T oldObj = oldMap.remove(newComapreRelatedKey);
			
			//如果关联不上，就是新数据
			if(oldObj == null){
				//不包含的时候才添加
				if(!addKeys.contains(newObj.getComapreRelatedKey())){
					compareResult.addNewObj(newObj);
					addKeys.add(newObj.getComapreRelatedKey());
				}else{
					compareResult.addRepeatedObj(newObj);
				}
				
			//关联的上，进行差异比较，如果比较结果不一样，就添加到更新队列中
			}else {
				//把需要传递的唯一列给新对象，比如Mysql数据库的自动递增列的值，这个值在新对象中是没有的
				newObj.setAutoIncrementFieldValue(oldObj.getAutoIncrementFieldValue());

				if(!newObj.getCompareRelatedValue().equals(oldObj.getCompareRelatedValue())){

					//不包含的时候才添加
					if(!updateKeys.contains(newObj.getComapreRelatedKey())){
						compareResult.addUpdateObj(newObj);
						compareResult.addUpdateNewOldObjMap(newObj, oldObj);
						updateKeys.add(newObj.getComapreRelatedKey());
					}else{
						compareResult.addRepeatedObj(newObj);
					}

				}else{

					//添加到相同对象队列
					if(!sameKeys.contains(newObj)){
						compareResult.addSameObj(newObj);
						sameKeys.add(newObj.getComapreRelatedKey());
					}else{
						compareResult.addRepeatedObj(newObj);
					}
				}
			}
		}
		
		//oldMap中剩余的数据就是在新数据中关联不上的，需要删除的对象
		if(oldMap.size() >= 1){
			for(Map.Entry<String, T> entry : oldMap.entrySet()){
				compareResult.addDeleteObj(entry.getValue());
			}
		}
		
		//清理临时队列
		oldMap = null;
		
		return compareResult;
	}

	/**
	 * 通过动态传入要比较的key字段和value字段的方式，对象差异比较
	 * 无论是新适配对象还是旧的适配对象，生成比较结果的时候，会进行去重
	 * @param newObjs 新适配对象列表，必填
	 * @param oldObjs 旧对象列表，必填
	 * @param autoIncrementFieldName       指定自增字段，可选，一般为空即可
	 * @param comapreKeyRelatedFieldNames 指定比较的key 字段集合，必填
	 * @param compareValueRelatedFieldNames 指定比较的value字段集合，必填
	 * @return 比较结果，包括增加、更新、删除的对象
	 * @param <T>
	 * @throws Exception
	 */
	public static <T extends Object> CompareResult<T> diffCompareData(Collection<T> newObjs, Collection<T> oldObjs, String autoIncrementFieldName, String[] comapreKeyRelatedFieldNames, String[] compareValueRelatedFieldNames) throws Exception {
		//比较结果
		CompareResult<T> compareResult = new CompareResult<T>();
		if (CollectionUtils.isEmpty(newObjs) && CollectionUtils.isEmpty(oldObjs)) {
			return compareResult;
		}
		Class clazz = getCompareClass(newObjs, oldObjs);

		//拿到对象的所有字段，包括父类的字段
		Map<String, Field> classFields = FieldUtils.getAllFieldNameMap(clazz, true);

		//自增字段处理，可以为空
		Field autoIncrementField = null;
		if (autoIncrementFieldName != null && !"".equals(autoIncrementFieldName)) {
			autoIncrementField = classFields.get(autoIncrementFieldName);
		} else {
//			利用注解中读取
			for (Field field : classFields.values()) {
				if (FieldUtils.isAnnotationPresentOnField(field, ComapreAutoIncrementFieldAnnotation.class)) {
					autoIncrementField = field;
					break;
				}
			}
		}

		//key相关的字段获取
		Field[] comapreKeyRelatedFields = null;
		if (comapreKeyRelatedFieldNames != null && comapreKeyRelatedFieldNames.length >= 1) {
			comapreKeyRelatedFields = new Field[comapreKeyRelatedFieldNames.length];
			for (int i = 0; i < comapreKeyRelatedFieldNames.length; i++) {
				comapreKeyRelatedFields[i] = classFields.get(comapreKeyRelatedFieldNames[i]);
				if (comapreKeyRelatedFields[i] == null) {
					throw new NullPointerException("要比较的对象类型注解指定的比较key字段不存在:" + comapreKeyRelatedFieldNames[i]);
				}
			}
		} else {
//			从注解中读取
			List<Field> fields = new ArrayList<>();
			for (Field field : classFields.values()) {
				if (FieldUtils.isAnnotationPresentOnField(field, ComapreKeyFieldAnnotation.class)) {
					fields.add(field);
				}
			}
			comapreKeyRelatedFields = fields.toArray(new Field[fields.size()]);
		}

		if (comapreKeyRelatedFields.length == 0) {
			throw new IllegalArgumentException("要比较的比较key字段不存在");
		}


//value相关的字段获取
		Field[] compareValueRelatedFields = null;
		if (compareValueRelatedFieldNames != null && compareValueRelatedFieldNames.length >= 1) {
			compareValueRelatedFields = new Field[compareValueRelatedFieldNames.length];
			for (int i = 0; i < compareValueRelatedFieldNames.length; i++) {
				compareValueRelatedFields[i] = classFields.get(compareValueRelatedFieldNames[i]);
				if (compareValueRelatedFields[i] == null) {
					throw new NullPointerException("要比较的对象类型注解指定的比较值字段不存在:" + compareValueRelatedFieldNames[i]);
				}
			}

		} else {
//			从注解中读取
			List<Field> fields = new ArrayList<>();
			for (Field field : classFields.values()) {
				if (FieldUtils.isAnnotationPresentOnField(field, ComapreValueFieldAnnotation.class)) {
					fields.add(field);
				}
			}
			compareValueRelatedFields = fields.toArray(new Field[fields.size()]);
		}
		if (compareValueRelatedFields.length == 0) {
			throw new IllegalArgumentException("要比较的比较value字段不存在");
		}


		//统计新旧数据的数量
		if(newObjs != null){
			compareResult.setNewCount(newObjs.size());
		}
		if(oldObjs != null){
			compareResult.setOldCount(oldObjs.size());
		}

		//没有新数据时那全部旧数据应该添加到删除队列中
		if(newObjs == null || newObjs.size() == 0){
			compareResult.addDeleteObjs(oldObjs);
			return compareResult;
		}

		// addKeys 添加的key，用于添加到增加队列的的时候去重 */
		Set<String> addKeys = new HashSet<String>();
		// updateKeys 更新的Key，用于添加到更新队列的时候去重 */
		Set<String> updateKeys = new HashSet<String>();
		// sameKeys 相同对象的Key，用于添加到相同队列的时候去重 */
		Set<String> sameKeys = new HashSet<String>();

		//如果老数据为空，也就是全新
		if(oldObjs == null || oldObjs.size() == 0){
			for(T obj : newObjs){
				String keys = getCombineFieldValues(comapreKeyRelatedFields, obj);
				//不包含的时候才添加
				if(!addKeys.contains(keys)){
					compareResult.addNewObj(obj);
					addKeys.add(keys);
				}else{
					//添加进重复队列
					compareResult.addRepeatedObj(obj);
				}
			}
			//直接返回
			return compareResult;
		}

		//计算新老数据比例，同时判断是否超过门限
		compareResult.setNewOldCountRatio(newObjs.size()/(double)oldObjs.size());

		//组装old数据成Map形式
		Map<String, T> oldMap = new HashMap<String, T>();
		for(T obj : oldObjs){
			String keys = getCombineFieldValues(comapreKeyRelatedFields, obj);
			//如果包含了的话就添加进删除队列，用于删除现有库中重复数据
			if(oldMap.containsKey(keys)){
				compareResult.addDeleteObj(obj);

			}else{
				oldMap.put(keys, obj);
			}
		}

		//遍历新数据，获取到新数据和需要更新的数据，从oldMap中移除出关联得上的数据
		for(T newObj : newObjs){
			String newObjKeys = getCombineFieldValues(comapreKeyRelatedFields, newObj);
			//如果在新增或者更新中出现，那么就是重复对象
			if (addKeys.contains(newObjKeys) || updateKeys.contains(newObjKeys) || sameKeys.contains(newObjKeys)) {
				compareResult.addRepeatedObj(newObj);
				continue;
			}

			T oldObj = oldMap.remove(newObjKeys);

			//如果关联不上，就是新数据，有重复添加进重复队列
			if(oldObj == null){
				//不包含的时候才添加
				if(!addKeys.contains(newObjKeys)){
					compareResult.addNewObj(newObj);
					addKeys.add(newObjKeys);
				}else{
					compareResult.addRepeatedObj(newObj);
				}

				continue;
			}

			//关联的上，把自增字段自动赋值，同时进行差异比较，如果比较结果一样，那就添加进相同队列，如果比较结果不一样，就添加到更新队列中

			//把需要传递的唯一列给新对象，比如Mysql数据库的自动递增列的值，这个值在新对象中是没有的
			if(autoIncrementField != null){
				autoIncrementField.set(newObj, autoIncrementField.get(oldObj));
			}

			String newObjValues = getCombineFieldValues(compareValueRelatedFields, newObj);
			String oldObjValues = getCombineFieldValues(compareValueRelatedFields, oldObj);
			//数据比较字段相同，添加到相同对象队列，有添加过就添加进重复队列
			if(newObjValues.equals(oldObjValues)){

				if(!sameKeys.contains(newObj)){
					compareResult.addSameObj(newObj);
					sameKeys.add(newObjKeys);
				}else{
					compareResult.addRepeatedObj(newObj);
				}

				continue;
			}

			//数据比较字段有差异，相当于是需要更新的数据,不包含的时候才添加,包含了就添加进重复队列
			if(!updateKeys.contains(newObjKeys)){
				compareResult.addUpdateObj(newObj);
				compareResult.addUpdateNewOldObjMap(newObj, oldObj);
				updateKeys.add(newObjKeys);
			}else{
				compareResult.addRepeatedObj(newObj);
			}

		}

		//oldMap中剩余的数据就是在新数据中关联不上的，需要删除的对象
		if(oldMap.size() >= 1){
			for(Map.Entry<String, T> entry : oldMap.entrySet()){
				compareResult.addDeleteObj(entry.getValue());
			}
		}

		//清理临时队列
		oldMap = null;

		return compareResult;
	}

	/**
	 * @param newObjs
	 * @param oldObjs
	 * @param <T>
	 * @return
	 */
	private static <T extends Object> Class getCompareClass(Collection<T> newObjs, Collection<T> oldObjs) {
		Class clazz = null;
		if(newObjs != null && !newObjs.isEmpty()){
			clazz = newObjs.iterator().next().getClass();
		}else if(oldObjs != null && !oldObjs.isEmpty()){
			clazz = oldObjs.iterator().next().getClass();
		}else{
			throw new IllegalArgumentException("拿不到比较的Class");
		}
		return clazz;
	}

	/**
	 * 获取字段值组拼的字符串,最后计算MD5
	 * @param comapreKeyRelatedFields
	 * @param obj
	 * @param <T>
	 * @return
	 * @throws IllegalAccessException
	 */
	private static <T extends Object> String getCombineFieldValues(Field[] comapreKeyRelatedFields, T obj) throws Exception {
		if(obj == null){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (Field comapreKeyRelatedField : comapreKeyRelatedFields) {
			Object value = comapreKeyRelatedField.get(obj);
			if(value == null){
				value = "";
			}
			sb.append(value.toString());
		}
		return MD5Utils.toLowerCaseMd5(sb.toString());
	}

	/**
	 * 对象差异比较，目前使用hashCode()
	 * 无论是新适配对象还是旧的适配对象，生成比较结果的时候，会进行去重
	 * @param newObjs 新适配对象列表
	 * @param oldObjs 旧对象列表
	 * @return 比较结果，包括增加、更新、删除的对象
	 * @param <T>
	 * @throws Exception
	 */
	public static <T extends Object> CompareResult<T> diffCompareByHashCode(Collection<T> newObjs, Collection<T> oldObjs) {
		//比较结果
		CompareResult<T> compareResult = new CompareResult<T>();
		if (CollectionUtils.isEmpty(newObjs) && CollectionUtils.isEmpty(oldObjs)) {
			return compareResult;
		}

		if(newObjs != null){
			compareResult.setNewCount(newObjs.size());
		}
		if(oldObjs != null){
			compareResult.setOldCount(oldObjs.size());
		}

		//没有新数据时那全部旧数据应该添加到删除队列中
		if(newObjs == null || newObjs.size() == 0){
			compareResult.addDeleteObjs(oldObjs);
			return compareResult;
		}

		// addKeys 添加的key，用于添加到增加队列的的时候去重 */
		Set<Integer> addKeys = new HashSet<>();
		// updateKeys 更新的Key，用于添加到更新队列的时候去重 */
		Set<Integer> updateKeys = new HashSet<>();
		// sameKeys 相同对象的Key，用于添加到相同队列的时候去重 */
		Set<Integer> sameKeys = new HashSet<>();

		//如果老数据为空，也就是全新
		if(oldObjs == null || oldObjs.size() == 0){
			for(T obj : newObjs){
				int hashCode = obj.hashCode();
				//不包含的时候才添加
				if(!addKeys.contains(hashCode)){
					compareResult.addNewObj(obj);
					addKeys.add(hashCode);
				}else{
					compareResult.addRepeatedObj(obj);
				}
			}
			return compareResult;
		}

		//计算新老数据比例，同时判断是否超过门限
		compareResult.setNewOldCountRatio(newObjs.size()/(double)oldObjs.size());

		//组装old数据成Map形式
		Map<Integer, T> oldMap = new HashMap<Integer, T>();
		for(T obj : oldObjs){
			int hashCode = obj.hashCode();
			//如果包含了的话就添加进删除队列，用于删除现有库中重复数据
			if(oldMap.containsKey(hashCode)){
				compareResult.addDeleteObj(obj);

			}else{
				oldMap.put(hashCode, obj);
			}
		}

		//遍历新数据，获取到新数据和需要更新的数据，从oldMap中移除出关联得上的数据
		for(T newObj : newObjs){
			int newObjHashCode = newObj.hashCode();
			//如果在新增或者更新中出现，那么就是重复对象
			if (addKeys.contains(newObjHashCode) || updateKeys.contains(newObjHashCode) || sameKeys.contains(newObjHashCode)) {
				compareResult.addRepeatedObj(newObj);
				continue;
			}
			T oldObj = oldMap.remove(newObjHashCode);

			//如果关联不上，就是新数据
			if(oldObj == null){
				//不包含的时候才添加
				if(!addKeys.contains(newObjHashCode)){
					System.out.println("新增"+newObjHashCode);
					compareResult.addNewObj(newObj);
					addKeys.add(newObjHashCode);
				}else{
					compareResult.addRepeatedObj(newObj);
				}

				//关联的上，进行差异比较，如果比较结果不一样，就添加到更新队列中
			}else {

				//不包含的时候才添加
				if(!sameKeys.contains(newObjHashCode)){
					compareResult.addSameObj(newObj);
					sameKeys.add(newObjHashCode);
				}else{
					compareResult.addRepeatedObj(newObj);
				}

			}
		}

		//oldMap中剩余的数据就是在新数据中关联不上的，需要删除的对象
		if(oldMap.size() >= 1){
			for(Map.Entry<Integer, T> entry : oldMap.entrySet()){
				compareResult.addDeleteObj(entry.getValue());
			}
		}

		//清理临时队列
		oldMap = null;

		return compareResult;
	}
}
