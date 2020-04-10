/**
 * <PRE>
 目标：使用libcephfs库，实现对cephfs文件系统的挂载、目录、文件上传与下载等操作

 环境：CentOS 7 / IntelliJ IDEA (因为libcephfs仅支持linux，所以在centos7中安装IDE开发程序)

 步骤：libcephfs简介->Linux环境配置->cephfs挂载与目录基本方法实现->文件上传与下载功能开发

 1.libcephfs简介

 CephFS通过在RADOS基础上增加了MDS(Metadata Server)来提供文件存储，并提供libcephfs库供开发者调用其接口。

 libcephfs javadoc：http://docs.ceph.org.cn/api/libcephfs-java/javadoc/

 上述网址中列出了库中包含的类及其方法，其中主要的类为CephMount。

 maven依赖：

 <!-- https://mvnrepository.com/artifact/com.ceph/libcephfs -->
 <dependency>
 <groupId>com.ceph</groupId>
 <artifactId>libcephfs</artifactId>
 <version>0.80.5</version>
 </dependency>
 2.Linux环境配置

 除了添加maven依赖之外，还需在linux开发机上安装libcephfs相关开发包：

 libcephfs1-0.94.9-0.el7.x86_64.rpm

 libcephfs_jni1-0.94.9-0.el7.x86_64.rpm

 下载地址：http://mirror.neu.edu.cn/centos/7/storage/x86_64/ceph-hammer/

 安装：yum -y install libcephfs1-0.94.9-0.el7.x86_64.rpm libcephfs_jni1-0.94.9-0.el7.x86_64.rpm

 设置链接：

 ln -s /usr/lib64/libcephfs_jni.so.1.0.0 /usr/lib/libcephfs_jni.so.1

 ln -s /usr/lib64/libcephfs_jni.so.1.0.0 /usr/lib/libcephfs_jni.so

 同时需要安装ceph-common组件：yum -y install ceph-common
 * </PRE>
 *
 * @author: latico
 * @date: 2020-04-09 16:17
 * @version: 1.0
 */
package com.latico.commons.cephfs.libcephfs;