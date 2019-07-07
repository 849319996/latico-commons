package com.latico.commons.net.socket.io.tcp;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.net.socket.io.tcp.common.SocketHandler;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-16 18:38
 * @Version: 1.0
 */
public class TcpServerServiceHandler implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(TcpServerServiceHandler.class);
    private SocketHandler socketHandler;

    public TcpServerServiceHandler(SocketHandler socketHandler) {

        this.socketHandler = socketHandler;
        new Thread(socketHandler).start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        LOG.info("TcpServerServiceHandler处理:{}", socketHandler.getSocket().getRemoteSocketAddress());
        while (socketHandler.getStatus()) {
            String str = socketHandler.getAndRemoveCurrentAllReceiveDataString();
            socketHandler.sendDataAutoLineFeed("服务端处理了:" + str);
            if (str.contains("<<exit>>")) {
                socketHandler.close();
                break;
            }

        }

    }
}
