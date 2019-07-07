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
public class TcpServerServiceObjectHandler implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(TcpServerServiceObjectHandler.class);
    private SocketHandler socketHandler;
    int count;

    public TcpServerServiceObjectHandler(SocketHandler socketHandler) {

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
            Object obj = socketHandler.getAndRemoveReceiveDataObject();
            if (obj == null) {
                LOG.warn("数据为空");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            TransferBean transferBean = (TransferBean)obj;
            System.out.println("接收:" + transferBean);

            if (transferBean.isClose()) {
                socketHandler.close();
                break;
            }

            transferBean = new TransferBean();
            transferBean.setName(transferBean.getName() + "服务端处理过" + count++);
            socketHandler.sendObjectData(transferBean);
            System.out.println("发送:" + transferBean);

        }

    }
}
