
package com.latico.commons.db.bdb;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.*;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 本地文件数据库，支持高并发访问，建议存储不常变化的数据，存储一次，访问多次的场景，redis需要部署服务端，
 * 该工具是嵌入式，直接生成文件存储
 * <PRE>
 * 注意,存储自定义对象类型,要实现序列化.
 * 文件数据库,存储一个Map到本地文件.使用方式跟Map类似只不过把信息存到本地硬盘了.
 * 只会把小部分常用数据缓存在内存中.
 * 使用一个类来封装对Berkeley DB 的配置
 * 可以通过继承或者新建来操作数据库和类目录对象
 * 下面是使用BDB数据库的主要流程：
(1)	使用一个文件夹来搭建数据库环境；
(2)	通过环境对象创建一个存储类信息的数据库；并创建一个用于类存储到类信息数据库的对象，该对象能把类信息结构存储到类信息数据库中；
(3)	通过环境对象创建一个或多个存储数据的数据库；
(4)	使用结束，关闭所有数据库对象，再关闭环境对象。

 example:
 BerkeleyDatabase bdb = new BerkeleyDatabase("./src/test/resources/com/latico/commons/db/bdb/db_data_dir", "BDB");

 bdb.put("abc", "123");
 bdb.put("abc2", "1232");
 bdb.put("abc3", "1233");
 bdb.put("abc4", "1234");

 System.out.println(bdb.get("abc"));
 System.out.println(bdb.get("abc2"));
 System.out.println(bdb.get("abc3"));
 System.out.println(bdb.get("abc4"));
 bdb.closes();

 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2016年10月9日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class BerkeleyDatabase {

	/** LOG 日志对象 */
	private static final Logger LOG = LoggerFactory.getLogger(BerkeleyDatabase.class);

	/** BdbEnvDir BDB数据库根目录，搭建环境的目录 */
	protected File bdbEnvDir;

	/** envConfig 数据库环境配置对象 */
	protected EnvironmentConfig envConfig;

	/** env BDB环境对象 */
	protected Environment env;

	/** CLASS_CATALOG_DB_NAME 类目录数据库名 */
	protected static final String CLASS_CATALOG_DB_NAME = "class_catalog";

	/** classCatalogDBConfig 存储Java类信息的数据库的配置对象 */
	protected DatabaseConfig classCatalogDBConfig;

	/** classCatalogDB 存储Java类信息的数据库 */
	protected Database classCatalogDB;

	/** classCatalog 存储的类信息数据库的映射对象，是类信息存进BDB数据库中的中介 */
	protected StoredClassCatalog classCatalog;

	/** databaseName 存储数据的数据库名 */
	protected String databaseName;

	/** databaseConfig 存储数据的数据库的配置对象 */
	protected DatabaseConfig databaseConfig;

	/** database 存储数据的数据库 */
	protected Database database;

	/** storedMapDB 数据库存储的一个映射 */
	protected StoredMap<Object, Object> storedMap = null;
	
	/**
	 * 数据库配置
	 * @param databaseDirectory  BDB数据库根目录，搭建环境的目录
	 * @param databaseName 数据库名称
	 * @throws DatabaseException BDB数据库异常
	 */
	public BerkeleyDatabase(String databaseDirectory, String databaseName) {
		this.bdbEnvDir = new File(databaseDirectory);
		// 数据库名字
		this.databaseName = databaseName;

		if (!bdbEnvDir.exists()) {
			LOG.warn("BDB环境文件夹[{}]不存在，开始创建并加载...", bdbEnvDir.getPath());
			bdbEnvDir.mkdirs();
		} else {
			LOG.info("BDB环境文件夹[{}]已存在，开始加载...", bdbEnvDir.getPath());
		}
		LOG.info("初始化数据库环境: " + databaseDirectory);
		initEnvironment();
		LOG.info("初始化类信息数据库：" + CLASS_CATALOG_DB_NAME + "，并获取类信息对象");
		initClassCatalogDb();
		LOG.info("初始化BDB数据库：" + databaseName);
		
		initDb();
		
		initStoredMap();
	}

	/**
	 * 初始化的数据库映射使用对象,
	 * 初始化该对象后,才方便使用数据库
	 */
	private void initStoredMap() {

		EntryBinding<Object> keyBinding = null;
		EntryBinding<Object> valueBinding = null;
		keyBinding = new SerialBinding<Object>(classCatalog, Object.class);
		valueBinding = new SerialBinding<Object>(classCatalog, Object.class);
		storedMap = new StoredMap<Object, Object>(database, keyBinding,
		        valueBinding, true);
	}

	/**
	 * 初始化存储数据的数据库，通过数据库环境根据数据库名称和数据库配置信息来创建
	 */
	private void initDb() {

		databaseConfig = new DatabaseConfig();
		databaseConfig.setTransactional(true);
		databaseConfig.setAllowCreate(true);
		database = env.openDatabase(null, this.databaseName, databaseConfig);
	}

	/**
	 * 初始化类目录信息对象，该对象记录了所有的类信息，类信息+数据=对象；
	 * 通过数据库环境根据数据库名称和类目录数据库配置信息来创建类目录数据库，
	 * 然后再根据类目录数据库创建具体的类目录对象。
	 */
	private void initClassCatalogDb() {

		classCatalogDBConfig = new DatabaseConfig();
		classCatalogDBConfig.setTransactional(true);
		classCatalogDBConfig.setAllowCreate(true);
		classCatalogDB = env.openDatabase(null, CLASS_CATALOG_DB_NAME,
		        classCatalogDBConfig);
		classCatalog = new StoredClassCatalog(classCatalogDB);
	}

	/**
	 * 初始化数据库环境，搭建环境
	 * 配置数据库环境后打开BDB数据库环境，所有在同一个环境下配置的数据库都是在该目录下
	 */
	private void initEnvironment() {

		LOG.info("开始初始化数据库环境...");
		envConfig = new EnvironmentConfig();
		envConfig.setTransactional(true); // 允许事务处理
		envConfig.setAllowCreate(true); // 允许创建

		env = new Environment(bdbEnvDir, envConfig);
	}

	/**
	 * 关闭映射Map,释放数据,同时关闭库
	 */
	public void free() {

		storedMap = null;
		closes();
	}
	
	/**
	 * 直接关闭数据库关闭环境,不事先释放对象
	 */
	public void closes() {

		if (database != null) {
			database.close();
		}
		if (classCatalog != null) {
			classCatalog.close();
		}
		if (classCatalogDB != null) {
			classCatalogDB.close();
		}
		if (env != null) {
			// env.cleanLog(); // 在关闭环境前清理下日志
			env.close();
		}
		LOG.info("关闭BDB数据库和环境成功");
	}

	/**
	 * 删除当前环境中指定的数据库。
	 * 如果传入"all",则删除所有数据库
	 * @param dbName 数据库名称
	 * @return true表示有数据库被删除
	 */
	public boolean removeDatabase(String dbName) {

		boolean flag = false;// 判断是否成功删除的标志
		closes();// 删除数据库前要关闭数据库对象，相当于断开连接
		env = new Environment(bdbEnvDir, envConfig); // 删除数据库要用到环境对象
		List<String> dbNames = env.getDatabaseNames();

		int dbCount = dbNames.size();
		if (dbName.equalsIgnoreCase("all")) {
			for (int i = 0; i < dbCount; i++) {
				LOG.info("Remove Berkeley Database: " + dbNames.get(i));
				env.removeDatabase(null, dbNames.get(i));
			}
			flag = true;
		} else {
			for (int i = 0; i < dbCount; i++) {
				if (dbNames.get(i).equals(dbName)) {
					LOG.info("Remove Berkeley Database: " + dbNames.get(i));
					env.removeDatabase(null, dbNames.get(i));
				}
			}
			flag = true;
		}
		// 把环境关闭掉
		if (env != null) {
			env.close();
		}
		return flag;
	}
	
	/**
	 * 判断是否包含Map的value
	 * @param value
	 * @return
	 */
	public boolean containsValue(Object value) {

		return storedMap.containsValue(value);
	}

	/**
	 * 判断是否包含一个key
	 * @param key
	 * @return
	 */
	public boolean containsKey(Object key) {

		return storedMap.containsKey(key);
	}

	/**
	 * 通过key获取一个记录
	 * @param key
	 * @return
	 */
	public Object get(Object key) {

		return storedMap.get(key);
	}

	/**
	 * 通过键删除该条记录
	 * @param key 键值
	 * @return 被删除的记录
	 */
	public Object delete(Object key) {

		return storedMap.remove(key);
	}

	/**
	 * 添加一个记录
	 * @param key 键
	 * @param value 值
	 */
	public void put(Object key, Object value) {

		storedMap.put(key, value);
	}

	/**
	 * 判断该库是否为空库
	 * @return true是空库，false不是空库
	 */
	public boolean isEmpty() {

		return storedMap.isEmpty();
	}

	/**
	 * 清空库
	 */
	public void clear() {

		storedMap.clear();
	}

	/**
	 * 添加多条记录到库中
	 * @param storedDatas 多条记录的键值对
	 */
	public void putAll(Map<Object, Object> storedDatas) {

		storedMap.putAll(storedDatas);
	}

	/**
	 * 通过键获取并移除一条记录，没有时返回null
	 * @param key 键
	 * @return 被移除的对象值，如果没有则返回null
	 */
	public Object getAndRemoveByKey(Object key) {

		return storedMap.remove(key);
	}

	/**
	 * 获取库的Map映射对象
	 * @return 库的Map映射对象
	 */
	public Map<Object, Object> getDataBaseMap() {
		
		return storedMap;
	}
	
	/**
	 * 获取大小
	 * @return
	 */
	public int getSize(){
		return storedMap.size();
	}
	
	/**
	 * 添加的时候,如果存在就不替换添加
	 * @param key
	 * @param value
	 */
	public void putIfNotAbsent(Object key, Object value){
		storedMap.putIfAbsent(key, value);
	}
	
	/**
	 * 通过替换一个值
	 * @param key
	 * @param newValue
	 */
	public void replace(Object key, Object newValue){
		storedMap.replace(key, newValue);
	}
	
	/**
	 * 获取Key集合,因为不可能重复,所以是Set集合
	 * @return
	 */
	public Set<Object> getKeySet(){
		return storedMap.keySet();
	}
	
	/**
	 * 获取value集合
	 * 因为value不像key那样唯一,所以用Collection装起来
	 * @return
	 */
	public Collection<Object> getValues(){
		return storedMap.values();
	}
}
