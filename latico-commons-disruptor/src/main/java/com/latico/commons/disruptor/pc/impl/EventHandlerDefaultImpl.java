package com.latico.commons.disruptor.pc.impl;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.lmax.disruptor.EventHandler;

/**
 * <PRE>
 * 事件处理器，消费者
 * 继承EventHandlerDefaultImpl后，子类需要重写onEvent方法
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-05-22 17:16
 * @Version: 1.0
 */
public class EventHandlerDefaultImpl implements EventHandler<OneArgEventDefault<String>> {
    private static final Logger LOG = LoggerFactory.getLogger(EventHandlerDefaultImpl.class);
    private String name;

    public EventHandlerDefaultImpl() {
    }
    public EventHandlerDefaultImpl(String name) {
        this.name = name;
    }
    @Override
    public void onEvent(OneArgEventDefault<String> event, long sequence, boolean endOfBatch) throws Exception {
        LOG.info("线程:{},处理器:{},事件序号:{}, 是否批量事件的结尾:{}, 事件内容:{}", Thread.currentThread().getName(), name, sequence, endOfBatch, event);
        Thread.sleep(200);
    }
}
