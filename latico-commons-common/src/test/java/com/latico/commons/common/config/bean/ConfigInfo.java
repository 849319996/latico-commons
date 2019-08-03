package com.latico.commons.common.config.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-08-04 2:38
 * @Version: 1.0
 */
@XStreamAlias("config")
public class ConfigInfo {
    Common common;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConfigInfo{");
        sb.append("common=").append(common);
        sb.append('}');
        return sb.toString();
    }
}
