package com.latico.commons.orm.mybatisplus.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.latico.commons.common.util.time.DateTimeUtils;

import java.util.Date;
import java.util.List;

/**
 * <PRE>
 * 代码生成器所需的配置信息
 * </PRE>
 *
 * @author: latico
 * @date: 2018-12-31 23:39
 * @version: 1.0
 */
public class GeneratorConfigInfo {

    /**
     * 输出根目录
     */
    private String outputDir = "./_mybatisplus_generator/" + DateTimeUtils.toStr(new Date(System.currentTimeMillis()), "yyyyMMddHHmmss");

    public String getMapperXmlFileRelativeDir() {
        return mapperXmlFileRelativeDir;
    }

    public void setMapperXmlFileRelativeDir(String mapperXmlFileRelativeDir) {
        this.mapperXmlFileRelativeDir = mapperXmlFileRelativeDir;
    }

    /**
     * mapper的xml文件存放的相对目录
     */
    private String mapperXmlFileRelativeDir = "resources/mapper";

    /**
     * 作者信息字符串
     */
    private String author = "";

    /**
     * 完成后是否打开目录
     */
    private boolean openDirWhenFinish = false;

    /**
     * 数据库类型
     */
    private DbType dbType;

    /**
     * 数据库URL
     */
    private String dbUrl;

    /**
     * 驱动名称
     */
    private String driverName;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    /**
     * 自定义类型转换，可选，如果是pdm由oracle转换mysql后的数据类型，需要使用自定义的字段转换器
     */
    private ITypeConvert typeConvert;

    /**
     * 下划线转驼峰
     */
    private boolean underlineToCamel = true;
    /**
     * 生成的java类的包目录
     */
    private String packageParent;

    public List<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(List<String> tableNames) {
        this.tableNames = tableNames;
    }

    /**
     * 表名
     */
    private List<String> tableNames;

    public String getPackageParent() {
        return packageParent;
    }

    public void setPackageParent(String packageParent) {
        this.packageParent = packageParent;
    }

    /**
     * 生成的各个种类的java类在packageParent下面的一个子包名
     */
    private String packageModuleName;

    public String getPackageModuleName() {
        return packageModuleName;
    }

    public void setPackageModuleName(String packageModuleName) {
        this.packageModuleName = packageModuleName;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isOpenDirWhenFinish() {
        return openDirWhenFinish;
    }

    public void setOpenDirWhenFinish(boolean openDirWhenFinish) {
        this.openDirWhenFinish = openDirWhenFinish;
    }

    public DbType getDbType() {
        return dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public ITypeConvert getTypeConvert() {
        return typeConvert;
    }

    public void setTypeConvert(ITypeConvert typeConvert) {
        this.typeConvert = typeConvert;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GeneratorConfigInfo{");
        sb.append("outputDir='").append(outputDir).append('\'');
        sb.append(", mapperXmlFileRelativeDir='").append(mapperXmlFileRelativeDir).append('\'');
        sb.append(", author='").append(author).append('\'');
        sb.append(", openDirWhenFinish=").append(openDirWhenFinish);
        sb.append(", dbType=").append(dbType);
        sb.append(", dbUrl='").append(dbUrl).append('\'');
        sb.append(", driverName='").append(driverName).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", typeConvert=").append(typeConvert);
        sb.append(", underlineToCamel=").append(underlineToCamel);
        sb.append(", packageParent='").append(packageParent).append('\'');
        sb.append(", tableNames=").append(tableNames);
        sb.append(", packageModuleName='").append(packageModuleName).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public boolean isUnderlineToCamel() {
        return underlineToCamel;
    }

    public void setUnderlineToCamel(boolean underlineToCamel) {
        this.underlineToCamel = underlineToCamel;
    }
}
