package com.latico.commons.common.config;

/**
 * <PRE>
 * 用于加载复杂配置文件，
 * 对于一些比较大量的，bean类型的配置，建议使用spring的上下文getBean方法加载初始化。
 *
 * </PRE>
 * @author: latico
 * @date: 2019-06-27 11:24:41
 * @version: 1.0
 */
public interface Config {
    /**
     * 配置文件根目录
     */
    public static final String CONFIG_FILE_ROOT_DIR = "config/";

    /**
     * 资源配置文件根目录
     */
    public static final String RESOURCES_CONFIG_FILE_ROOT_DIR = "/config/";

    /**
     * 加载或者刷新配置
     * @return true:成功 false:失败
     */
    boolean initOrRefreshConfig();
}
