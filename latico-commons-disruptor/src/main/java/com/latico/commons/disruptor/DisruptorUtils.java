package com.latico.commons.disruptor;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.thread.NamePrefixThreadFactory;
import com.latico.commons.disruptor.pc.impl.EventFactoryDefaultImpl;
import com.latico.commons.disruptor.pc.impl.EventProducerDefault;
import com.latico.commons.disruptor.pc.impl.EventTranslatorOneArgDefaultImpl;
import com.latico.commons.disruptor.pc.impl.OneArgEventDefault;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * <PRE>

 生产者消费者示例：
 public void testPC() {

 //必须是2的N次方
 int ringBufferSize = 1024;
 Disruptor<OneArgEventDefault<String>> disruptor = DisruptorUtils.createDisruptorDefault(ringBufferSize, "disruptor", new ExceptionHandlerDefaultImpl<String>(), new EventHandlerDefaultImpl<String>());

 //        创建一个生产者
 EventProducerDefault<String> producer = DisruptorUtils.createProducerDefault(disruptor);

 //        生产者不断生产数据
 for (int i = 0; true; i++) {
 producer.publishEvent("Hello Disruptor!" + i++);
 ThreadUtils.sleep(500);
 }

 }

 Disruptor支持的高级功能，参考网址：https://blog.csdn.net/shengqianfeng/article/details/80710471

 例如：
 EventHandlerGroup<Trade> handlerGroup =
 disruptor.handleEventsWith(new Handler1(), new Handler2());
 //声明在C1,C2完事之后执行JMS消息发送操作 也就是流程走到C3
 handlerGroup.then(new Handler3());

 Disruptor 几个潜在的坑，
 1. event handler 的 wait strategy 要选好，一般用 blocking wait，要不然 cpu 消耗很大
 2. 当 ringbuffer 满了，event handler 速度跟不上，event publish 那边会空转等 ringbuffer 的空间，消耗额外 cpu
 3. 如果用 dependant event handler，他在等他依赖的 event handler 完成时也会空转，空转久了又耗 cpu 了


 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-05-22 14:59
 * @Version: 1.0
 */
public class DisruptorUtils {
    private static final Logger LOG = LoggerFactory.getLogger(DisruptorUtils.class);

    /**
     * 创建一个顺序执行的并发器，对于eventHandlers，按顺序，一个执行完，把执行完的结果传递给下一个
     * @param producerType     生产者类型，单生产者/多生产者
     * @param eventFactory     事件创建工厂
     * @param ringBufferSize   必须是2的N次方
     * @param threadFactory    线程工厂
     * @param exceptionHandler 异常处理器
     * @param eventHandlers    事件处理器，可以多个
     * @param <Event>
     * @return
     */
    public static <Event> Disruptor<Event> createDisruptorBySequence(ProducerType producerType, EventFactory<Event> eventFactory,
                                                                     int ringBufferSize, ThreadFactory threadFactory,
                                                                     ExceptionHandler<Event> exceptionHandler,
                                                                     EventHandler<? super Event>... eventHandlers) {

        Disruptor<Event> disruptor = new Disruptor<Event>(eventFactory, ringBufferSize,
                threadFactory, producerType, new BlockingWaitStrategy());

//        因为是顺序执行，所以每个处理器都是单独的一个组，下面的写法等于：
//          disruptor.handleEventsWith(new Handler1()).
//        	handleEventsWith(new Handler2()).
//        	handleEventsWith(new Handler3());

        EventHandlerGroup<Event> eventEventHandlerGroup = disruptor.handleEventsWith(eventHandlers[0]);
        if (eventHandlers.length >= 2) {
            for (int i = 1; i < eventHandlers.length; i++) {
                eventEventHandlerGroup = eventEventHandlerGroup.handleEventsWith(eventHandlers[i]);
            }
        }

        disruptor.setDefaultExceptionHandler(exceptionHandler);
        disruptor.start();
        return disruptor;
    }

    /**
     * 创建一个并行执行的并发器，对于eventHandlers，所有的事件处理器一起执行任务
     * @param producerType     生产者类型，单生产者/多生产者
     * @param eventFactory     事件创建工厂
     * @param ringBufferSize   必须是2的N次方
     * @param threadFactory    线程工厂
     * @param exceptionHandler 异常处理器
     * @param eventHandlers    事件处理器，可以多个
     * @param <Event>
     * @return
     */
    public static <Event> Disruptor<Event> createDisruptorByParallel(ProducerType producerType, EventFactory<Event> eventFactory,
                                                                     int ringBufferSize, ThreadFactory threadFactory,
                                                                     ExceptionHandler<Event> exceptionHandler,
                                                                     EventHandler<? super Event>... eventHandlers) {

        Disruptor<Event> disruptor = new Disruptor<Event>(eventFactory, ringBufferSize,
                threadFactory, producerType, new BlockingWaitStrategy());

        disruptor.handleEventsWith(eventHandlers);
        disruptor.setDefaultExceptionHandler(exceptionHandler);
        disruptor.start();
        return disruptor;
    }

    /**
     * 创建一个并发器，本方法只是简单的添加执行
     * @param ringBufferSize 队列大小
     * @param threadNamePrefix 执行线程的名字的前缀
     * @param exceptionHandler 异常处理器
     * @param eventHandlers 事件处理器
     * @return
     * @param <Arg>
     */
    public static <Arg> Disruptor<OneArgEventDefault<Arg>> createDisruptorDefault(int ringBufferSize, String threadNamePrefix,
                                                                                    ExceptionHandler<OneArgEventDefault<Arg>> exceptionHandler,
                                                                                    EventHandler<OneArgEventDefault<Arg>>... eventHandlers) {

        return createDisruptorBySequence(ProducerType.MULTI, new EventFactoryDefaultImpl<Arg>(), ringBufferSize, new NamePrefixThreadFactory(threadNamePrefix), exceptionHandler, eventHandlers);
    }

    /**
     * @param disruptor
     * @param <Event>
     * @return
     */
    public static <Event> RingBuffer<Event> getRingBuffer(Disruptor<Event> disruptor) {
        return disruptor.getRingBuffer();
    }

    /**
     * 创建单参数转换器
     * @param <Arg>
     * @return
     */
    public static <Arg> EventTranslatorOneArg<OneArgEventDefault<Arg>, Arg> createEventTranslatorOneArgDefault() {
        return new EventTranslatorOneArgDefaultImpl<Arg>();
    }

    /**
     * 关闭
     * 注意：调用此方法前，必须不能再往里面添加新任务，要不然永远关闭不了,
     * 除非使用了timeout大于等于0的超时限制
     * @param disruptor 被关闭的
     * @param timeout 小于0的话，就无限等待执行完成
     * @param timeUnit 时间单位
     * @return
     */
    public static boolean shutdown(Disruptor disruptor, long timeout, TimeUnit timeUnit) {
        if (disruptor != null) {
            try {
                disruptor.shutdown(timeout, timeUnit);
                return true;
            } catch (Exception e) {
                LOG.error(e);
            }
        }
        return false;
    }

    /**
     * 关闭，无限等待所有任务执行完成
     * 注意：调用此方法前，必须不能再往里面添加新任务，要不然永远关闭不了
     * @param disruptor 被关闭的
     * @return
     */
    public static boolean shutdown(Disruptor disruptor) {
        if (disruptor != null) {
            try {
                disruptor.shutdown();
                return true;
            } catch (Exception e) {
                LOG.error(e);
            }
        }
        return false;
    }

    /**
     * 创建一个默认生产者
     * @param disruptor
     * @param <Arg>
     * @return
     */
    public static <Arg> EventProducerDefault<Arg> createProducerDefault(Disruptor<OneArgEventDefault<Arg>> disruptor) {
        EventProducerDefault<Arg> producer = new EventProducerDefault<Arg>(disruptor);
        return producer;
    }
}
