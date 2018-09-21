package com.nettyserver.thrift;

import com.nettyserver.bean.RequestBean;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.transport.TNonblockingTransport;

import java.io.IOException;

public class AsyncUtil {

    public static AsyncMethodCallback<String> callback(TNonblockingTransport transport, RequestBean bean)
            throws IOException {
        AsyncMethodCallback<String> resultHandler = new AsyncCallBack<String>(bean, transport) {
            public String getResult(String res) throws TException {
                return res;
            }
        };

        return resultHandler;
    }
}
