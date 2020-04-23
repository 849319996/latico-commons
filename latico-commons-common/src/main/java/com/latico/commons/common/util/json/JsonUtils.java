package com.latico.commons.common.util.json;

/**
 * JSON工具,默认使用FastJson，其他的jar依赖选择性使用
 * 建议使用：FastJson、Jackson、Gson；
 * 建议针对性使用，比如
 * 1、Jackson：{@link JacksonUtils}
 * 2、Gson：{@link GsonUtils}
 * 3、FastJson：{@link FastJsonUtils}
 * 4、Jettison：{@link JettisonUtils}
 * 5、JsonLib：{@link JsonLibUtils}
 *
 * JSON工具对于泛型有个特点，就是要实现一个泛型抽象类，目的是为了能让JSON工具能拿到父类真正的泛型，
 * Type superClass = getClass().getGenericSuperclass();//拿到父类指定泛型
 *
 java对于处理JSON数据的序列化与反序列化目前常用的类库有Gson、FastJSON、Jackson、jettison以及json-lib。在这里我们将对这些类库在json序列化与反序列化方面的性能进行测试对比
 1、从测试结果可以看出gson在小于10w的数据量处理上，耗时相对较少，但是在数据越来越大的情况下耗时会明显的增长。

 2、无论那种情况下，json-lib的耗时都是最多的，引用时还需要额外几个依赖包，且目前已经停止了更新，所以不推荐使用。

 3、jackson在各阶段数据量都有很不错的性能，而fastjson在数据量较多的情况下也有很好性能。

 4、jettison性能不错，但只提供json和其JSONObject对象相互转化的方法，转为自定义bean时需要再手动将JSONObject对象转为需要的bean。
 * @author: latico
 * @date: 2018/12/07 18:43
 * @version: 1.0
 */
public class JsonUtils {

}
