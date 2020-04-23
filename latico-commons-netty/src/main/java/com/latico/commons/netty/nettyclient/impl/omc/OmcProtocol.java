package com.latico.commons.netty.nettyclient.impl.omc;

import com.latico.commons.common.util.codec.CodecUtils;


/**
 * <PRE>
 *     华为北向OMC网管协议
 * 0:realTimeAlarm
 * 1:reqLoginAlarm
 * 2:ackLoginAlarm
 * 3:reqSyncAlarmMsg
 * 4:ackSyncAlarmMsg
 * 5:reqSyncAlarmFile
 * 6:ackSyncAlarmFile
 * 7:ackSyncAlarmFileResult
 * 8:reqHeartBeat
 * 9:ackHeartBeat
 * 10:closeConnAlarm
 * </PRE>
 *
 * @author: latico
 * @date: 2019-12-13 22:08
 * @version: 1.0
 */
public class OmcProtocol {
    /**
     * 基本长度
     */
    public static final int BASE_LENGTH = 9;
    public static final String charset = "UTF-8";
    public static final byte msgType_realTimeAlarm = 0x00;
    public static final byte msgType_reqLoginAlarm = 0x01;
    public static final byte msgType_ackLoginAlarm = 0x02;
    public static final byte msgType_reqSyncAlarmMsg = 0x03;
    public static final byte msgType_ackSyncAlarmMsg = 0x04;
    public static final byte msgType_reqSyncAlarmFile = 0x05;
    public static final byte msgType_ackSyncAlarmFile = 0x06;
    public static final byte msgType_ackSyncAlarmFileResult = 0x07;
    public static final byte msgType_reqHeartBeat = 0x08;
    public static final byte msgType_ackHeartBeat = 0x09;
    public static final byte msgType_closeConnAlarm = 0x0A;
    /**
     * 前缀，报文开始标志，占用2个字节
     */
    public static final short startSign = (short) 0xFFFF;

    /**
     * 消息类型，占用1个字节
     */
    private byte msgType;

    /**
     * 时间戳，占用4个字节，单位：秒
     */
    private int timeStamp;

    /**
     * 数据体的字节数
     */
    private short lenOfBody;

    /**
     * 消息体
     */
    private byte[] msgBody;

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public short getLenOfBody() {
        return lenOfBody;
    }

    public void setLenOfBody(short lenOfBody) {
        this.lenOfBody = lenOfBody;
    }

    public byte[] getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(byte[] msgBody) {
        this.msgBody = msgBody;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OmcProtocol{");
        sb.append("msgType=").append(msgType);
        sb.append(", timeStamp=").append(timeStamp);
        sb.append(", lenOfBody=").append(lenOfBody);
        sb.append(", msgBody=").append(CodecUtils.toStr(msgBody, charset));
        sb.append('}');
        return sb.toString();
    }
}
