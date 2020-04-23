package com.latico.commons.net.socket.io.tcp.server.handler.service;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

/**
 * <PRE>
 * TcpServerServiceTask示例
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-17 11:01
 * @version: 1.0
 */
public class TcpServerServiceTaskExample extends AbstractTcpServerServiceTask {
    private static final Logger LOG = LoggerFactory.getLogger(TcpServerServiceTaskExample.class);
    //结束符
    private String endTag = "\0";

    @Override
    protected void dealService() {
        //对数据进行切割缓存
        while (socketHandler.getStatus()) {
            String str = socketHandler.getAndRemoveCurrentAllReceiveDataString();
            LOG.info("服务端收到数据:{}", str);

            socketHandler.sendData("服务端处理了:" + str + endTag);
            socketHandler.sendData("\n服务端处理完成" + endTag);
            socketHandler.sendData("\n服务端处理完成2" + endTag);

            //收到关闭符号，退出
            if (str.contains("<<exit>>")) {
                socketHandler.close();
                break;
            }

        }
    }


}
