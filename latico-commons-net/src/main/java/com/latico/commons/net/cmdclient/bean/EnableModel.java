package com.latico.commons.net.cmdclient.bean;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-06-06 11:15
 * @version: 1.0
 */
public class EnableModel {
    private String manufacturer;

    private String model;

    private boolean enable;

    private String enablePwd;

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getEnablePwd() {
        return enablePwd;
    }

    public void setEnablePwd(String enablePwd) {
        this.enablePwd = enablePwd;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EnableModel{");
        sb.append("manufacturer='").append(manufacturer).append('\'');
        sb.append(", model='").append(model).append('\'');
        sb.append(", enable=").append(enable);
        sb.append(", enablePwd='").append(enablePwd).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
