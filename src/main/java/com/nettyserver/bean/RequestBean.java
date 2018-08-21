package com.nettyserver.bean;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import lombok.Data;

@Data
public class RequestBean {

    private String uri = "";
    private String methodName = "";

    public RequestBean(ChannelHandlerContext ctx, Object msg) {

        HttpRequest request = (HttpRequest)msg;
        this.uri = request.uri().split("\\?")[0];
    }
}
