package com.latico.commons.disruptor;

import com.latico.commons.common.util.thread.ThreadUtils;
import com.latico.commons.disruptor.pc.impl.EventHandlerDefaultImpl;
import com.latico.commons.disruptor.pc.impl.EventProducerDefault;
import com.latico.commons.disruptor.pc.impl.ExceptionHandlerDefaultImpl;
import com.latico.commons.disruptor.pc.impl.OneArgEventDefault;
import com.lmax.disruptor.dsl.Disruptor;
import org.junit.Test;

public class DisruptorUtilsTest {

    /**
     * 测试生产者消费者
     */
    @Test
    public void testPC() {

        //必须是2的N次方
        int ringBufferSize = 1024;
        Disruptor<OneArgEventDefault<String>> disruptor = DisruptorUtils.createDisruptorDefault(ringBufferSize, "disruptor", new ExceptionHandlerDefaultImpl(), new EventHandlerDefaultImpl("处理器1"), new EventHandlerDefaultImpl("处理器2"), new EventHandlerDefaultImpl("处理器3"));

//        创建一个生产者
        EventProducerDefault<String> producer = DisruptorUtils.createProducerDefault(disruptor);

//        生产者不断生产数据
        for (int i = 0; true; i++) {

            producer.publishEvent("Hello Disruptor!" + i);
            ThreadUtils.sleep(200);

        }


    }
}