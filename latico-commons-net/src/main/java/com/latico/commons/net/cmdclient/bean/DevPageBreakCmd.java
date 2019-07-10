package com.latico.commons.net.cmdclient.bean;

import java.util.List;

/**
 * <PRE>
 * 设备非分页输出命令
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-04-09 17:21
 * @Version: 1.0
 */
public class DevPageBreakCmd {

    private String manufacturer;

    private String model;

    private String pageBreakCmd;

    private List<String> pageBreakCmds;

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

    public List<String> getPageBreakCmds() {
        return pageBreakCmds;
    }

    public void setPageBreakCmds(List<String> pageBreakCmds) {
        this.pageBreakCmds = pageBreakCmds;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DevPageBreakCmd{");
        sb.append("manufacturer='").append(manufacturer).append('\'');
        sb.append(", model='").append(model).append('\'');
        sb.append(", pageBreakCmd='").append(pageBreakCmd).append('\'');
        sb.append(", pageBreakCmds=").append(pageBreakCmds);
        sb.append('}');
        return sb.toString();
    }
}
