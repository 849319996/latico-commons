package com.latico.commons.disruptor.pc.impl;

import com.lmax.disruptor.EventFactory;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-05-22 16:36
 * @Version: 1.0
 */
public class EventFactoryDefaultImpl <Arg> implements EventFactory<OneArgEventDefault<Arg>> {
    @Override
    public OneArgEventDefault<Arg> newInstance() {
        return new OneArgEventDefault<Arg>();
    }
}
