package com.niodemo.bio2;

import com.niodemo.bio.ServerHandler;
import org.apache.commons.io.IOUtils;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 10000;

    public static void main(String[] args) {

        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT);
            // 进行阻塞
            Socket socket = server.accept();
            // 新建一个线程执行客户端的任务
            new Thread(new ServerHandler(socket)).start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(server);
        }
    }
}
