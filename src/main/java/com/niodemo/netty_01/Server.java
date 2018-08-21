package com.niodemo.netty_01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

    private static final int PORT = 20000;

    public static void main(String[] args) throws Exception {

        // 1.创建2个线程组
        // 一个用于处理服务器端接收客户端连接的
        // 一个是进行网络通信的（网络读写的）
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // 2.创建辅助工具类，用于服务器通道的一系列配置
        ServerBootstrap sever = new ServerBootstrap();
        sever.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 3.在这里配置数据接收方法的处理
                        socketChannel.pipeline().addLast(new ServerHandler());
                    }
                });

        // 4.进行绑定
        ChannelFuture future = sever.bind(PORT).sync();
        // 5.等待关闭
        future.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
