package com.niodemo.nio;

import org.apache.commons.io.IOUtils;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;

public class ClientHandler extends Thread {

    private static CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
    private static CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();

    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 20000;
    private Selector selector = null;
    private SocketChannel socketChannel = null;
    private SelectionKey clientKey = null;

    public ClientHandler() {

        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            clientKey = socketChannel.register(selector, SelectionKey.OP_CONNECT);
            socketChannel.connect(new InetSocketAddress(ADDRESS, PORT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {

            while (true) {
                selector.select();
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    if (key.isConnectable()) {
                        SocketChannel channel = (SocketChannel)key.channel();
                        // socket连接就绪，但还没有连接完成，则等待完成连接
                        if (channel.isConnectionPending()) {
                            channel.finishConnect();
                        }
                        channel.register(selector, SelectionKey.OP_READ);
                        System.out.println("服务连接客户成功");
                    } else if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel)key.channel();
                        ByteBuffer buf = ByteBuffer.allocate(500);
                        channel.read(buf);
                        buf.flip();
                        String upperText = decoder.decode(buf).toString();
                        System.out.println("client get " + upperText);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(selector);
            IOUtils.closeQuietly(socketChannel);
        }
    }

    public void send(String msg) {
        try {
            SocketChannel socketChannel = (SocketChannel)clientKey.channel();
            socketChannel.write(encoder.encode(CharBuffer.wrap(msg)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
