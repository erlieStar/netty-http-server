package com.nettyserver;

import com.nettyserver.netty.NettyServer;

public class ServerLauncher {

    public static void main(String[] args) {

        NettyServer nettyServer = new NettyServer();
        nettyServer.start();
    }
}
