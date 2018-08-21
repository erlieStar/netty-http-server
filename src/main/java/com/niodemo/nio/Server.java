package com.niodemo.nio;


import org.apache.commons.io.IOUtils;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;

public class Server {

    private static final int PORT = 20000;
    private static CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
    private static CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();

    public static void main(String[] args) {

        Selector selector = null;
        ServerSocketChannel server = null;

        try {
            selector = Selector.open();
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);

            server.bind(new InetSocketAddress(PORT)).socket();

            while (true) {
                // 监听事件
                selector.select();
                // 事件来源列表
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();

                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    if (key.isAcceptable()) {
                        ServerSocketChannel ss = (ServerSocketChannel)key.channel();
                        SocketChannel channel = ss.accept();
                        channel.configureBlocking(false);
                        if (channel.isConnectionPending()) {
                            channel.finishConnect();
                        }
                        channel.register(selector, SelectionKey.OP_READ);
                        System.out.println("客户连接服务成功");
                    } else if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel)key.channel();
                        ByteBuffer buf = ByteBuffer.allocate(500);
                        channel.read(buf);
                        buf.flip();
                        String msg = decoder.decode(buf).toString();
                        System.out.println("server get " + msg);
                        channel.write(encoder.encode(CharBuffer.wrap(msg.toUpperCase())));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(selector);
            IOUtils.closeQuietly(server);
        }
    }
}
