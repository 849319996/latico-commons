package com.latico.commons.common.util.json.bean;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-07-16 15:44
 * @version: 1.0
 */
public class RespBean {

    /**
     * errcode : 0
     * errmsg : ok
     * msgid : 900223914733961220
     */

    private int errcode;
    private String errmsg;
    private long msgid;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public long getMsgid() {
        return msgid;
    }

    public void setMsgid(long msgid) {
        this.msgid = msgid;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RespBean{");
        sb.append("errcode=").append(errcode);
        sb.append(", errmsg='").append(errmsg).append('\'');
        sb.append(", msgid=").append(msgid);
        sb.append('}');
        return sb.toString();
    }
}
