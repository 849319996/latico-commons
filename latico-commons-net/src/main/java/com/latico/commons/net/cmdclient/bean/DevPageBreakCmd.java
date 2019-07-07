package com.latico.commons.net.cmdclient.bean;

/**
 * <PRE>
 * 设备非分页输出命令
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-04-09 17:21
 * @Version: 1.0
 */
public class DevPageBreakCmd {

    private String manufacturer;

    private String model;

    private String pageBreakCmd;

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

    public String getPageBreakCmd() {
        return pageBreakCmd;
    }

    public void setPageBreakCmd(String pageBreakCmd) {
        this.pageBreakCmd = pageBreakCmd;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DevPageBreakCmd{");
        sb.append("manufacturer='").append(manufacturer).append('\'');
        sb.append(", model='").append(model).append('\'');
        sb.append(", pageBreakCmd='").append(pageBreakCmd).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
