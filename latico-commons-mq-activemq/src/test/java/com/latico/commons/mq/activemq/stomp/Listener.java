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
package com.latico.commons.mq.activemq.stomp;

import com.latico.commons.mq.activemq.ActiveMQUtils;

import javax.jms.*;

class Listener {

    public static void main(String[] args) throws JMSException {
        String destination = "event";
        String user = "admin";
        String password = "admin";
        String socket = "localhost:61613";


        ConnectionFactory factory = ActiveMQUtils.createStompConnectionFactoryBySockets(user, password, socket);
        Connection connection = ActiveMQUtils.createConnection(factory);
        Session session = ActiveMQUtils.createSession(connection);
        MessageConsumer consumer = ActiveMQUtils.createConsumerByTopic(session, destination);

        long start = System.currentTimeMillis();
        long count = 1;
        System.out.println("Waiting for messages...");
        while (true) {
            TextMessage msg = ActiveMQUtils.receive(consumer);
            String body = ((TextMessage) msg).getText();
            if ("SHUTDOWN".equals(body)) {
                long diff = System.currentTimeMillis() - start;
                System.out.println(String.format("Received %d in %.2f seconds", count, (1.0 * diff / 1000.0)));
                break;
            } else {
                if (count != msg.getIntProperty("id")) {
                    System.out.println("mismatch: " + count + "!=" + msg.getIntProperty("id"));
                }
                count = msg.getIntProperty("id");

                if (count == 0) {
                    start = System.currentTimeMillis();
                }
                if (count % 1000 == 0) {
                    System.out.println(String.format("Received %d messages.", count));
                }
                count++;
            }

        }
        ActiveMQUtils.close(connection);
    }

}