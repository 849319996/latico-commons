package com.latico.commons.mq.rocketmq;


import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * Created by lance on 2017/2/10.
 */
public class RocketMQListener  implements MessageListenerConcurrently {
 
 
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//        System.out.println("get data from rocketMQ:" + msgs);
        for (MessageExt message : msgs) {
 
            String msg = new String(message.getBody());
            System.out.println("msg data from rocketMQ:" + msg);
        }
 
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}