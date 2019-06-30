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
import com.latico.commons.common.util.thread.ThreadUtils;
import com.latico.commons.mq.activemq.ActiveMQUtils;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;
import org.junit.Test;

/**
 * Uses an callback based interface to MQTT.  Callback based interfaces
 * are harder to use but are slightly more efficient.
 */
public class ListenerCallbackConnection {

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
        CallbackConnection connection = ActiveMQUtils.createCallbackConnection(factory);


        System.out.println("Waiting for messages...");

//创建消息监听器
        org.fusesource.mqtt.client.Listener listener = new org.fusesource.mqtt.client.Listener() {
            long count = 0;
            long start = System.currentTimeMillis();
            @Override
            public void onConnected() {
                System.out.println("监听已经连接上");
            }
            @Override
            public void onDisconnected() {
                System.out.println("监听已经断开");
            }
            @Override
            public void onFailure(Throwable value) {
                System.out.println("监听失败");
                value.printStackTrace();
            }

            @Override
            public void onPublish(UTF8Buffer topic, Buffer msg, Runnable ack) {
                doService(topic, msg);

//                执行
//                ack.run();
            }

            private void doService(UTF8Buffer topic, Buffer msg) {
                String body = msg.utf8().toString();
                if ("SHUTDOWN".equals(body)) {
                    long diff = System.currentTimeMillis() - start;
                    System.out.println(String.format("Received %d in %.2f seconds", count, (1.0 * diff / 1000.0)));

                    Callback disconnectCallback = createDisconnectCallback();
                    ActiveMQUtils.disconnect(connection, disconnectCallback);

                } else {
                    if (count == 0) {
                        start = System.currentTimeMillis();
                    }
                    if (count % 1000 == 0) {
                        System.out.println(String.format("Received %d messages.", count));
                    }
                    count++;
                }
            }

            /**
             * 断开连接的callback
             * @return
             */
            private Callback createDisconnectCallback() {
                //        是否断开成功状态记录
                Callback<Void> callback = new Callback<Void>() {
                    @Override
                    public void onSuccess(Void value) {
                        System.out.println("断开连接成功");
                        System.exit(0);
                    }

                    @Override
                    public void onFailure(Throwable value) {
                        System.out.println("断开连接失败");
                    }
                };
                return callback;
            }
        };

//        在listener中实现业务逻辑
        ActiveMQUtils.startMqttListener(connection, listener, destination);

        ThreadUtils.sleepSecond(10000);
    }

}