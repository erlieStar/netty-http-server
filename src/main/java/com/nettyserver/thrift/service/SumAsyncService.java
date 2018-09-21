package com.nettyserver.thrift.service;

import com.nettyserver.config.Config;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;

public class SumAsyncService {

    private static String host = Config.getString("sum.server.host");
    private static int port = Config.getInt("sum.server.port");

    public static String getSum(int a, int b) throws Exception {
        TNonblockingTransport transport = new TNonblockingSocket(host, port);
    }

}
