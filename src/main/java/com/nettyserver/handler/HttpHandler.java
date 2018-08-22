package com.nettyserver.handler;

import com.nettyserver.bean.RequestBean;
import com.nettyserver.common.ServerResponse;
import com.nettyserver.service.ProductHttpService;
import com.nettyserver.service.UserHttpService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@Slf4j
public class HttpHandler {

    private static final String user = UserHttpService.class.getName();
    private static final String product = ProductHttpService.class.getName();

    public static String transfer(ChannelHandlerContext ctx, Object msg) {
        RequestBean request = new RequestBean(ctx, msg);
        return invoke(request);
    }

    public static String invoke(RequestBean requestBean) {

        Class clazz = null;
        Object result = null;
        String methodName = requestBean.getMethodName();

        try {

            if (methodName.startsWith("user")) {
                clazz = Class.forName(user);
            } else if (methodName.startsWith("product")) {
                clazz = Class.forName(product);
            } else {
                // 这里说一个有意思的现象，对每一个http请求，浏览器会多发送一个url为/favicon.ico的请求
                // 可以让浏览器的收藏夹中除显示相应的标题外，还以图标的方式区别不同的网站
                return ServerResponse.methodNotImpl(methodName);
            }
            Constructor<RequestBean> constructor = clazz.getConstructor(RequestBean.class);
            Object classObject = constructor.newInstance(requestBean);

            Method method = clazz.getMethod(methodName);
            result = method.invoke(classObject);

        } catch (Exception e) {
            log.info("class or method not exists", e);
            return ServerResponse.internalError("class or method not exists");
        }

        return ServerResponse.success(result);
    }
}
