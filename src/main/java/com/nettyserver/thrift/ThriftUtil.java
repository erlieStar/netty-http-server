package com.nettyserver.thrift;

import com.nettyserver.thrift.service.SumService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

@Slf4j
public class ThriftUtil {

    /*
     * 这个类是用来快速搭建访问其他thrift服务的，main方法里面是一个简单的例子
     */

    public static final int BINARY = 1;
    public static final int COMPACT = 2;

    public static TTransport getTransport(String host, int port, int type) {

        int socketTimeout = 1000 * 60;
        int connectTimeout = 1000 * 60;

        TTransport transport = null;

        if (type == BINARY) {
            transport = new TSocket(host, port, socketTimeout, connectTimeout);
        } else if (type == COMPACT) {
            transport = new TFramedTransport.Factory().getTransport(new TSocket(host, port, socketTimeout, connectTimeout));
        }

        return transport;
    }

    public static TProtocol getProtocol(TTransport transport, int type) {

        TProtocol protocol = null;

        try {

            transport.open();

            if (type == BINARY) {
                protocol = new TBinaryProtocol(transport);
            } else if (type == COMPACT) {
                protocol = new TCompactProtocol(transport);
            }

        } catch (Exception e) {
            log.error("failed connect to thrift server", e);
        }

        return protocol;

    }


    public static void main(String[] args) throws TException {
        TTransport transport = ThriftUtil.getTransport("127.0.0.1", 100 , ThriftUtil.BINARY);
        TProtocol protocol = ThriftUtil.getProtocol(transport, ThriftUtil.BINARY);
        SumService.Iface client = new SumService.Client(protocol);
        Integer sum = client.getSum(10 ,20);
        System.out.println(sum);
    }

}
