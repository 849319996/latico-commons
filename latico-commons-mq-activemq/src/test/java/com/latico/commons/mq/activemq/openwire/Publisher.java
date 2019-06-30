/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.latico.commons.mq.activemq.openwire;

import com.latico.commons.mq.activemq.ActiveMQUtils;

import javax.jms.*;

class Publisher {

    public static void main(String []args) throws JMSException {

        String user = "admin";
        String password = "admin";
        String destination = "event";
//        默认端口是61616，但是因为windows下有服务占用了该端口
        String socket = "localhost:61618";

        ConnectionFactory factory = ActiveMQUtils.createOpenwireConnectionFactoryByTcpSockets(user, password, socket);
        Connection connection = ActiveMQUtils.createConnection(factory);
        Session session = ActiveMQUtils.createSession(connection);
//
        MessageProducer producer = ActiveMQUtils.createProducerByTopic(session, destination, DeliveryMode.NON_PERSISTENT);

        int messages = 10000;
        int size = 256;

        String DATA = "abcdefghijklmnopqrstuvwxyz";
        String body = "";
        for( int i=0; i < size; i ++) {
            body += DATA.charAt(i%DATA.length());
        }
        for( int i=1; i <= messages; i ++) {
            TextMessage msg = ActiveMQUtils.createTextMessage(session, body);
//            可以添加一些其他配置
            msg.setIntProperty("id", i);

            ActiveMQUtils.send(producer, msg);
//            每发送了1000条打印一下
            if( (i % 1000) == 0) {
                System.out.println(String.format("Sent %d messages", i));
            }
        }
        ActiveMQUtils.send(producer, session.createTextMessage("SHUTDOWN"));
        ActiveMQUtils.close(connection);

    }

}