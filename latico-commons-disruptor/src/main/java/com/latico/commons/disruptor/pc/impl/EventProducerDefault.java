package com.latico.commons.disruptor.pc.impl;

import com.latico.commons.disruptor.DisruptorUtils;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * <PRE>
 * 生产者
 * 把消息推送到RingBuffer队列中
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-05-22 17:02
 * @Version: 1.0
 */
public class EventProducerDefault<Arg> {
    /**
     * 并发器
     */
    private Disruptor<OneArgEventDefault<Arg>> disruptor;
    /**
     * 消息队列
     */
    private RingBuffer<OneArgEventDefault<Arg>> ringBuffer;
    /**
     * 消息转换器
     */
    private EventTranslatorOneArg<OneArgEventDefault<Arg>, Arg> translator = DisruptorUtils.createEventTranslatorOneArgDefault();

    public EventProducerDefault(Disruptor<OneArgEventDefault<Arg>> disruptor) {
        this.disruptor = disruptor;
        this.ringBuffer = disruptor.getRingBuffer();
    }

    /**
     * 将消息输出到ringBuffer队列
     *
     * @param eventMessage 事件内容
     */
    public void publishEvent(Arg eventMessage) {
        ringBuffer.publishEvent(translator, eventMessage);
    }
}
