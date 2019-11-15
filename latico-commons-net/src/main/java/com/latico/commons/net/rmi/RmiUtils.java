package com.latico.commons.net.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-11-15 9:42
 * @Version: 1.0
 */
public class RmiUtils {

    /**
     * 注册RMI的服务端
     * 注册多个服务
     * @param port
     * @param nameRemoteObjMap
     * @return
     * @throws Exception
     */
    public static Registry registryRmiServer(int port, Map<String, Remote> nameRemoteObjMap) throws Exception {
        Registry registry = LocateRegistry.createRegistry(port);
        for (Map.Entry<String, Remote> entry : nameRemoteObjMap.entrySet()) {
            registry.rebind(entry.getKey(), entry.getValue());
        }
        return registry;

    }

    /**
     * 注册RMI的服务端
     * @param port
     * @param remoteName
     * @param remote
     * @return
     * @throws Exception
     */
    public static Registry registryRmiServer(int port, String remoteName, Remote remote) throws Exception {
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind(remoteName, remote);
        return registry;

    }

    /**
     * @param serverHost
     * @param serverPort
     * @param remoteName
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T extends Remote> T getRmiClient(String serverHost, int serverPort, String remoteName) throws Exception {
        Registry registry = LocateRegistry.getRegistry(serverHost, serverPort);
        T remote = (T)registry.lookup(remoteName);
        return remote;
    }

    /**
     * 一次返回多个
     * @param serverHost
     * @param serverPort
     * @param remoteNames
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T extends Remote> Map<String, T> getRmiClients(String serverHost, int serverPort, List<String> remoteNames) throws Exception {
        Registry registry = LocateRegistry.getRegistry(serverHost, serverPort);
        Map<String, T> map = new HashMap<>();
        for (String remoteName : remoteNames) {
            T remote = (T)registry.lookup(remoteName);
            map.put(remoteName, remote);
        }
        return map;
    }

    /**
     * 一次返回多个
     * @param serverHost
     * @param serverPort
     * @param remoteNames
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T extends Remote> Map<String, T> getRmiClients(String serverHost, int serverPort, String... remoteNames) throws Exception {
        Registry registry = LocateRegistry.getRegistry(serverHost, serverPort);
        Map<String, T> map = new HashMap<>();
        for (String remoteName : remoteNames) {
            T remote = (T)registry.lookup(remoteName);
            map.put(remoteName, remote);
        }
        return map;
    }
}
