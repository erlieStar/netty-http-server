package com.nettyserver.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        ChannelPipeline p = socketChannel.pipeline();

        // 聚合http请求或响应的不同部分
        p.addLast(new HttpServerCodec());
        // //把多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse
        p.addLast(new HttpObjectAggregator(1048576));
        // 压缩数据
        p.addLast(new HttpContentCompressor());
        // 大文件支持
        p.addLast(new ChunkedWriteHandler());
        p.addLast(new ServerHandler());
    }
}
