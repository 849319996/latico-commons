package com.latico.commons.net.socket.bean;

import java.util.List;

/**
 * <PRE>
 * 指定的结束符把字符串切割成每段结果
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-17 17:16
 * @Version: 1.0
 */
public class StrEndTagResult {

    /**
     * 根据指定的结束符解析出来的数据
     */
    private List<String> results;
    /**
     * 剩余数据，报文不完整的时候，会留有尾巴数据，返回给调用者，一般用于跟后面接收的新数据进行组拼
     */
    private String surplusData = "";

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }

    public String getSurplusData() {
        return surplusData;
    }

    public void setSurplusData(String surplusData) {
        this.surplusData = surplusData;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StrEndTagResult{");
        sb.append("results=").append(results);
        sb.append(", surplusData='").append(surplusData).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
