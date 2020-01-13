package com.latico.commons.common.util.version;

import java.util.List;

/**
 * <PRE>
 *  版本信息
 * </PRE>
 * @Author: latico
 * @Date: 2020-01-13 11:10:37
 * @Version: 1.0
 */
public class VersionInfo {

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 版本号
     */
    private String version;

    /**
     * 作者
     */
    private String author;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 更新内容信息
     */
    private String updateInfo;

    public VersionInfo() {
    }

    /**
     * @param projectName 项目名称
     * @param version 版本
     * @param author 作者
     * @param updateTime 更新时间
     * @param updateInfo 更新内容
     */
    public VersionInfo(String projectName, String version, String author, String updateTime, String updateInfo) {
        this.projectName = projectName;
        this.version = version;
        this.author = author;
        this.updateTime = updateTime;
        this.updateInfo = updateInfo;
    }

    /**
     * 构建一个对象
     *
     * @return
     */
    public static VersionInfo build() {
        return new VersionInfo();
    }

    /**
     * 构建一个对象
     *
     * @param version 版本号
     * @param author 作者
     * @param updateTime 更新时间
     * @param updateInfo 更新信息
     * @return
     */
    public static VersionInfo build(String projectName, String version, String author, String updateTime, String updateInfo) {
        VersionInfo versionInfo = new VersionInfo();
        versionInfo.projectName = projectName;
        versionInfo.version = version;
        versionInfo.author = author;
        versionInfo.updateTime = updateTime;
        versionInfo.updateInfo = updateInfo;
        return versionInfo;
    }

    public String getProjectName() {
        return projectName;
    }

    public VersionInfo buildProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAuthor() {
        return author;
    }


    public String getVersion() {
        return version;
    }

    /**
     * 增加版本信息
     *
     * @param version
     * @return
     */
    public VersionInfo buildVersion(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 增加更新作者
     *
     * @param author
     * @return
     */
    public VersionInfo buildAuthor(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * 增加更新时间
     *
     * @param updateTime
     * @return
     */
    public VersionInfo buildUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    /**
     * 增加更新信息
     *
     * @param updateInfo
     * @return
     */
    public VersionInfo buildUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
        return this;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VersionInfo{");
        sb.append("projectName='").append(projectName).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", author='").append(author).append('\'');
        sb.append(", updateTime='").append(updateTime).append('\'');
        sb.append(", updateInfo='").append(updateInfo).append('\'');
        sb.append('}');
        return sb.toString();
    }
}