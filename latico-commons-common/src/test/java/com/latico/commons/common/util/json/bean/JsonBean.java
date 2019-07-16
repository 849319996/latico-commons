package com.latico.commons.common.util.json.bean;

import java.util.Arrays;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-07-16 15:42
 * @Version: 1.0
 */
public class JsonBean<T> {


    /**
     * code : 0
     * data : [{"msg":"成功","code":0,"send_time":"2019-07-16 12:03:17","create_time":"2019-07-16 12:03:17","open_id":"ozdxT5vBT9F950Hsi7Pvjjvsmx8Q","resp":"{"errcode":0,"errmsg":"ok","msgid":900223914733961220}","nickname":"123","content":""}]
     * msg : 成功
     */

    private int code;
    private T[] data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T[] getData() {
        return data;
    }

    public void setData(T[] data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JsonBean{");
        sb.append("code=").append(code);
        sb.append(", data=").append(Arrays.toString(data));
        sb.append(", msg='").append(msg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
