package com.latico.commons.disruptor.pc.impl;

import com.lmax.disruptor.EventTranslatorOneArg;

/**
 * <PRE>
 * 一个参数的事件
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-05-22 16:09
 * @Version: 1.0
 */
public class EventTranslatorOneArgDefaultImpl<Arg> implements EventTranslatorOneArg<OneArgEventDefault<Arg>, Arg> {
    @Override
    public void translateTo(OneArgEventDefault<Arg> event, long sequence, Arg arg0) {
        event.setData(arg0);
    }
}
