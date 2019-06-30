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
package com.latico.commons.mq.activemq.mqtt;

import com.latico.commons.common.util.logging.LogUtils;
import com.latico.commons.mq.activemq.ActiveMQUtils;
import org.fusesource.mqtt.client.*;
import org.junit.Test;

/**
 * Uses an callback based interface to MQTT.  Callback based interfaces
 * are harder to use but are slightly more efficient.
 */
public class ListenerFutureConnection {

    /**
     *
     */
    @Test
    public void main() throws Exception {
        LogUtils.loadLogBackConfigDefault();
        String destination = "event";
        String user = "admin";
        String password = "admin";
        String socket = "localhost:1883";

        MQTT factory = ActiveMQUtils.createMqttConnectionFactoryBySockets(user, password, socket);
        FutureConnection connection = ActiveMQUtils.createFutureConnection(factory);

        Topic[] topics = {new Topic(destination, QoS.AT_LEAST_ONCE)};
        connection.subscribe(topics);

        System.out.println("Waiting for messages...");
        long count = 0;
        long start = System.currentTimeMillis();

        while (true) {
            System.out.print(count + " ");
            Future<Message> futrueMessage = connection.receive();
            Message message = futrueMessage.await();
            String body = message.getPayloadBuffer().utf8().toString();
            if ("SHUTDOWN".equals(body)) {
                long diff = System.currentTimeMillis() - start;
                System.out.println(String.format("Received %d in %.2f seconds", count, (1.0 * diff / 1000.0)));
                break;
            } else {
                if (count == 0) {
                    start = System.currentTimeMillis();
                }
                if (count % 1000 == 0) {
                    System.out.println();
                    System.out.println(String.format("Received %d messages.", count));
                }
                count++;
            }
        }

        connection.disconnect();
    }

}