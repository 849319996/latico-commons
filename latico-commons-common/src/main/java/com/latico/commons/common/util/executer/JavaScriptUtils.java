package com.latico.commons.common.util.executer;

import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

/**
 * javascript工具
 * @Author: latico
 * @Date: 2018/12/08 20:50:25
 * @Version: 1.0
 */
public class JavaScriptUtils {

	/** LOG 日志工具 */
	private static final Logger LOG = LoggerFactory.getLogger(JavaScriptUtils.class);

	public static final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

	/** 私有化构造函数. */
	protected JavaScriptUtils() {}

	/**
	 *
	 * 执行和通过key获取返回结果，只传了一个入参
	 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
	 * @param script JavaScript脚本
	 * @param inParamKey 入参的key
	 * @param inParamValue 入参的value
	 * @throws Exception
	 */
	public static void exec(String script, String inParamKey, Object inParamValue) throws Exception {
		ScriptEngine engine = scriptEngineManager.getEngineByName("javascript");
		engine.put(inParamKey, inParamValue);
		engine.eval(script);
	}

	/**
	 * 执行和通过key获取返回结果，只传了一个入参
	 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
	 * @param script JavaScript脚本
	 * @param inParamKey 入参的key
	 * @param inParamValue 入参的value
	 * @param resultKey 获取返回结果的时候的key
	 * @param clazz 返回结果的数据格式
	 * @return
	 * @throws Exception
	 */
	public static <T> T execAndGetKeyResult(String script, String inParamKey, Object inParamValue, String resultKey, Class<T> clazz) throws Exception {
		ScriptEngine engine = scriptEngineManager.getEngineByName("javascript");
		engine.put(inParamKey, inParamValue);
		engine.eval(script);
		Object obj = engine.get(resultKey);
		if(obj != null){
			return (T)obj;
		}
		return null;
	}


	/**
	 * 执行和通过key获取返回结果
	 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
	 * @param script JavaScript脚本
	 * @param paramMap 全局变量
	 * @param resultKey 获取返回结果的时候的key
	 * @param clazz 返回结果的数据格式
	 * @return
	 * @throws Exception
	 */
	public static <T> T execAndGetKeyResult(String script, Map<String, Object> paramMap, String resultKey, Class<T> clazz) throws Exception {
		ScriptEngine engine = scriptEngineManager.getEngineByName("javascript");
		if(paramMap != null){
			for(Map.Entry<String, Object> entry : paramMap.entrySet()){
				engine.put(entry.getKey(), entry.getValue());
			}
		}
		engine.eval(script);
		Object obj = engine.get(resultKey);
		if(obj != null){
			return (T)obj;
		}
		return null;
	}

	/**
	 * 执行JavaScript脚本
	 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
	 * @param script JavaScript脚本
	 * @param paramMap 全局变量
	 * @throws Exception
	 */
	public static void exec(String script, Map<String, Object> paramMap) throws Exception {
		ScriptEngine engine = scriptEngineManager.getEngineByName("javascript");
		if(paramMap != null){
			for(Map.Entry<String, Object> entry : paramMap.entrySet()){
				engine.put(entry.getKey(), entry.getValue());
			}
		}
		engine.eval(script);
	}


	/**
	 * 执行一个JavaScript方法
	 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
	 * @param script JavaScript脚本
	 * @param methodName 执行的JavaScript方法名称
	 * @param clazz 返回结果的数据类型
	 * @param args JavaScript方法的参数
	 * @return
	 * @throws Exception
	 */
	public static <T> T execMethod(String script, String methodName, Class<T> clazz, Object... args) throws Exception {
		ScriptEngine engine = scriptEngineManager.getEngineByName("javascript");
		engine.eval(script);
		if(engine instanceof Invocable) {
			Invocable invoke = (Invocable)engine;

			// 调用方法，并传入参数
			Object result = invoke.invokeFunction(methodName, args);
			if(result != null){
				return (T)result;
			}
		}
		return null;
	}

	/**
	 * 执行一个JavaScript方法
	 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
	 * @param script JavaScript脚本
	 * @param methodName 执行的JavaScript方法名称
	 * @param clazz 返回结果的数据类型
	 * @param paramMap 全局变量
	 * @param args JavaScript方法的参数
	 * @return
	 * @throws Exception
	 */
	public static <T> T execMethod(String script, String methodName, Class<T> clazz, Map<String, Object> paramMap, Object... args) throws Exception {
		ScriptEngine engine = scriptEngineManager.getEngineByName("javascript");
		if(paramMap != null){
			for(Map.Entry<String, Object> entry : paramMap.entrySet()){
				engine.put(entry.getKey(), entry.getValue());
			}
		}
		engine.eval(script);
		if(engine instanceof Invocable) {
			Invocable invoke = (Invocable)engine;

			// 调用方法，并传入参数
			Object result = invoke.invokeFunction(methodName, args);
			if(result != null){
				return (T)result;
			}
		}
		return null;
	}

	/**
	 * 执行一个JavaScript方法
	 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
	 * @param script JavaScript脚本
	 * @param methodName 执行的JavaScript方法名称
	 * @param clazz 返回结果的数据类型
	 * @param paramMap 全局变量,同时也是方法的入参
	 * @return
	 * @throws Exception
	 */
	public static <T> T execMethod(String script, String methodName, Class<T> clazz, Map<String, Object> paramMap) throws Exception {
		ScriptEngine engine = scriptEngineManager.getEngineByName("javascript");
		if(paramMap != null){
			for(Map.Entry<String, Object> entry : paramMap.entrySet()){
				engine.put(entry.getKey(), entry.getValue());
			}
		}
		engine.eval(script);
		if(engine instanceof Invocable) {
			Invocable invoke = (Invocable)engine;

			// 调用方法，并传入参数
			Object result = invoke.invokeFunction(methodName, paramMap);
			if(result != null){
				return (T)result;
			}
		}
		return null;
	}
	/**
	 * 执行JS脚本中的方法
	 * @param jsFilePath JS文件路径
	 * @param jsMethod JS方法
	 * @param args 方法参数
	 * @return Object 执行结果
	 */
	public static Object executeFromFile(String jsFilePath, String jsMethod, Object... args) throws FileNotFoundException, ScriptException, NoSuchMethodException {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");

		Object result = null;
		FileReader reader = null;
		try {
			reader = new FileReader(jsFilePath);
			engine.eval(reader);
			Invocable inv = (Invocable) engine;
			result = inv.invokeFunction(jsMethod, args);
			
		} finally {
			IOUtils.close(reader);
		}
		return result;
	}

}
