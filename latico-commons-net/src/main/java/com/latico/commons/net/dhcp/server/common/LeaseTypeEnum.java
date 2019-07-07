package com.latico.commons.net.dhcp.server.common;

/**
 * <PRE>
 * 租约枚举类型
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-07 23:00
 * @Version: 1.0
 */
public enum LeaseTypeEnum {
    offerdLease(1), ackLease(2), clientDeclineLease(3), serverDeclineLease(4), expireLease(5), clientAbandonLease(6);

    LeaseTypeEnum(int code){
        this.code = code;
    }
    private int code;
    public int getCode(){
        return code;
    }
}
