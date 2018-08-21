package com.nettyserver.bean;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RequestBean {

    private String uri = "";
    private String methodName = "";
    // 存放请求的参数
    private Map<String, String> parameterMap = new HashMap<>();

    public RequestBean(ChannelHandlerContext ctx, Object msg) {

        FullHttpRequest request = (FullHttpRequest)msg;
        parseUri(request.uri());
    }

    private void parseUri(String uri) {
        String[] strs = uri.split("\\?");
        this.uri = strs[0];

        if (strs.length != 1) {
            String parameterStr = strs[1];
            String[] parameters = parameterStr.split("&");
            for (String parameter : parameters) {
                String[] temp =  parameter.split("=");
                if ("methodName".equals(temp[0])) {
                    this.methodName = temp[1];
                } else {
                    parameterMap.put(temp[0], temp[1]);
                }
            }
        }
    }


}
