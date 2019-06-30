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
package com.latico.commons.mq.activemq.mqtt;

import com.latico.commons.common.util.logging.LogUtils;
import com.latico.commons.mq.activemq.ActiveMQUtils;
import org.fusesource.hawtbuf.AsciiBuffer;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;

import java.util.LinkedList;

/**
 * Uses a Future based API to MQTT.
 */
public class PublisherBlockingConnection {

    public static void main(String []args) throws Exception {
        LogUtils.loadLogBackConfigDefault();
        String destination = "event";
        String user = "admin";
        String password = "admin";
        String socket = "localhost:1883";

        MQTT factory = ActiveMQUtils.createMqttConnectionFactoryBySockets(user, password, socket);
        BlockingConnection connection = ActiveMQUtils.createBlockingConnection(factory);

        int messages = 10000;
        int size = 256;

        String DATA = "abcdefghijklmnopqrstuvwxyz";
        String body = "";
        for( int i=0; i < size; i ++) {
            body += DATA.charAt(i%DATA.length());
        }
        Buffer msg = new AsciiBuffer(body);
        final LinkedList<Future<Void>> queue = new LinkedList<Future<Void>>();
        UTF8Buffer topic = new UTF8Buffer(destination);
        for( int i=1; i <= messages; i ++) {
            connection.publish(topic, msg, QoS.AT_LEAST_ONCE, false);

            if( i % 1000 == 0 ) {
                System.out.println(String.format("Sent %d messages.", i));
            }
        }
        connection.publish(topic, new AsciiBuffer("SHUTDOWN"), QoS.AT_LEAST_ONCE, false);

        connection.disconnect();

        System.exit(0);
    }


}