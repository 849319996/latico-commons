package com.latico.commons.common.util.json.bean;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-07-16 15:44
 * @Version: 1.0
 */
public class DataBean {


    /**
     * msg : 成功
     * code : 0
     * send_time : 2019-07-16 12:03:17
     * create_time : 2019-07-16 12:03:17
     * open_id : ozdxT5vBT9F950Hsi7Pvjjvsmx8Q
     * resp : {"errcode":0,"errmsg":"ok","msgid":900223914733961220}
     * nickname : 123
     * content :
     */

    private String msg;
    private int code;
    private String send_time;
    private String create_time;
    private String open_id;
    private RespBean resp;
    private String nickname;
    private String content;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public RespBean getResp() {
        return resp;
    }

    public void setResp(RespBean resp) {
        this.resp = resp;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
