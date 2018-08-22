package com.nettyserver.bean;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class RequestBean {

    private String uri = "";
    // 存放请求的参数
    private Map<String, String> parameterMap = new HashMap<>();

    public RequestBean(ChannelHandlerContext ctx, Object msg) {

        FullHttpRequest request = (FullHttpRequest)msg;
        queryStringDecoder(request.uri());
    }

    private void queryStringDecoder(String uri) {
        String[] strs = uri.split("\\?");
        // 这里说一个有意思的现象，对每一个http请求，浏览器会多发送一个url为/favicon.ico的请求
        // 可以让浏览器的收藏夹中除显示相应的标题外，还以图标的方式区别不同的网站
        this.uri = strs[0];
        log.info("uri {}", uri);

        // 这里做一次decoder的原因是
        // 假如你访问的url在浏览器上显示的是methodName=userDetail&username=张三
        // 实际发送过来的是methodName=userDetail&username=%E5%BC%A0%E4%B8%89
        // 浏览器会做encoder和decoder工作，不信你把浏览器中url中有中文字符的链接粘贴到记事本上看
        if (strs.length != 1) {
            QueryStringDecoder decoder = new QueryStringDecoder(uri);
            decoder.parameters().entrySet().forEach( entry -> {
                parameterMap.put(entry.getKey(), entry.getValue().get(0));
            });
        }
    }

}
