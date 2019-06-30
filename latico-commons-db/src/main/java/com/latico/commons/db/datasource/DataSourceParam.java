package com.latico.commons.db.datasource;

import com.latico.commons.common.envm.CharsetType;

/**
 * <PRE>
 * 数据源参数
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-02 14:09
 * @Version: 1.0
 */
public class DataSourceParam {

    /**
     * 数据源ID，多个数据源的时候，可用于区分数据源唯一，外界不传时使用默认
     */
    private String id = "default-datasource";

    /**
     * 数据源类型，Druid/dbcp/c3p0/proxool
     */
    private String type;

    /**
     * 数据库字符集,默认UTF-8
     */
    private String charset = CharsetType.UTF8;
    /**
     * 数据库类型，某些连接池的某些数据库可能会用到
     */
    private String dbType;
    /**
     * 驱动类全名称
     */
    private String driverClassName;

    /**
     * url 连接目标机器的URL,必须，不能为空
     */
    private String url;

    /**
     * 目标机器主机名，不建议用来组拼URL
     */
    private String host;
    /**
     * 目标机器端口，不建议用来组拼URL
     */
    private int port;
    /**
     * 目标机器数据库名称，不建议用来组拼URL
     */
    private String databaseName;

    /**
     * 数据库用户名
     */
    private String username;
    /**
     * 数据库密码
     */
    private String password;
    /**
     * 连接池的最大数据库连接数。设为0表示无限制。默认20
     */
    private int maxActive = 20;
    /**
     * 最大空闲数，数据库连接的最大空闲时间。超过空闲时间，数据库连
     * 接将被标记为不可用，然后被释放。设为0表示无限制
     */
    private int maxIdle = 5;
    /**
     * 最大激活时间，一般开源框架定义成maxWait，一个连接从数据库获取连接开始计算，直到真正关闭的时间，
     * 每个数据库最大建立连接等待时间，线程用完会放到空闲连接中，
     * 空闲连接会等待超过这个时间后就真正回收。如果超过此时间将接到异常。
     * 设为-1表示无限制，默认一个小时超时。单位毫秒
     */
    private int maxActiveTime = 3600000;

    /**
     * 最大寿命，当一个调用者从连接池获取一个连接开始计算，这个时间是限定调用者最大持有连接时间,单位毫秒
     * 默认跟maxActiveTime一样
     * 例如对于proxool来说，一般需要结合配置连接存活检测机制才能生效。
     */
    private int maxLifeTime = 3600000;

    /**
     * 初始化时连接池中连接数量，默认一个连接
     */
    private int initialSize = 1;
    /**
     * 最小空闲连接数数量
     */
    private int minIdle = 1;

    /**
     * 是否保持连接
     */
    private boolean keepAlive;

    /**
     * 使用前是否测试是否可用
     */
    private boolean testBeforeUse;

    /**
     * 默认构造函数，需要手动set参数
     */
    public DataSourceParam() {
    }

    /**
     * 最小参数方式
     * @param driverClassName
     * @param url
     * @param username
     * @param password
     */
    public DataSourceParam(String id, String driverClassName, String url, String username, String password) {
        this.id = id;
        this.driverClassName = driverClassName;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxActiveTime() {
        return maxActiveTime;
    }

    public void setMaxActiveTime(int maxActiveTime) {
        this.maxActiveTime = maxActiveTime;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DataSourceParam{");
        sb.append("id='").append(id).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", charset='").append(charset).append('\'');
        sb.append(", dbType='").append(dbType).append('\'');
        sb.append(", driverClassName='").append(driverClassName).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", host='").append(host).append('\'');
        sb.append(", port=").append(port);
        sb.append(", databaseName='").append(databaseName).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", maxActive=").append(maxActive);
        sb.append(", maxIdle=").append(maxIdle);
        sb.append(", maxActiveTime=").append(maxActiveTime);
        sb.append(", maxLifeTime=").append(maxLifeTime);
        sb.append(", initialSize=").append(initialSize);
        sb.append(", minIdle=").append(minIdle);
        sb.append(", keepAlive=").append(keepAlive);
        sb.append(", testBeforeUse=").append(testBeforeUse);
        sb.append('}');
        return sb.toString();
    }

    public boolean isTestBeforeUse() {
        return testBeforeUse;
    }

    public void setTestBeforeUse(boolean testBeforeUse) {
        this.testBeforeUse = testBeforeUse;
    }

    public int getMaxLifeTime() {
        return maxLifeTime;
    }

    public void setMaxLifeTime(int maxLifeTime) {
        this.maxLifeTime = maxLifeTime;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}
