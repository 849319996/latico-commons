/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.latico.commons.mq.activemq.amqp;

import com.latico.commons.mq.activemq.ActiveMQUtils;

import javax.jms.*;

class Publisher {

    public static void main(String[] args) throws Exception {

        String destinationName = "event";
        String user = "admin";
        String password = "password";
        String socket = "localhost:5672";


        ConnectionFactory factory = ActiveMQUtils.createAmqpConnectionFactoryBySockets(user, password, socket);
        Connection connection = ActiveMQUtils.createConnection(factory);
        Session session = ActiveMQUtils.createSession(connection);
        MessageProducer producer = ActiveMQUtils.createProducerByTopic(session, destinationName, DeliveryMode.NON_PERSISTENT);

        int messages = 10000;
        int size = 256;

        String DATA = "abcdefghijklmnopqrstuvwxyz";
        String body = "";
        for (int i = 0; i < size; i++) {
            body += DATA.charAt(i % DATA.length());
        }
        for (int i = 1; i <= messages; i++) {
            TextMessage msg = ActiveMQUtils.createTextMessage(session, "#:" + i);
            msg.setIntProperty("id", i);

            ActiveMQUtils.send(producer, msg);
            if ((i % 1000) == 0) {
                System.out.println(String.format("Sent %d messages", i));
            }
        }

        ActiveMQUtils.send(producer, session.createTextMessage("SHUTDOWN"));
        Thread.sleep(1000 * 3);
        ActiveMQUtils.close(connection);
        System.exit(0);
    }


}