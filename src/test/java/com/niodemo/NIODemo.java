package com.niodemo;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIODemo {

    @Test
    public void ioCopyFile() throws Exception {
        FileInputStream fis = new FileInputStream("readfile.txt");
        FileOutputStream fos = new FileOutputStream("writefile.txt");
        int ch;
        byte[] by = new byte[1024];
        while ((ch = fis.read(by)) != -1) {
            fos.write(by, 0, ch);
        }
        fos.close();
        fis.close();
    }

    @Test
    public void readFile() throws Exception {

        RandomAccessFile aFile = new RandomAccessFile("nio-data.txt", "r");
        FileChannel inChannel = aFile.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(48);
        // 将数据读入buffer
        int bytesRead = 0;
        bytesRead = inChannel.read(buf);
        while (bytesRead != -1) {
            System.out.println("Read " + bytesRead);
            // 将写模式切换为读模式
            buf.flip();
            // current position 和 limit是否有数据
            while (buf.hasRemaining()) {
                System.out.println((char)buf.get());
            }
            // 清空buffer为了再次写入
            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        aFile.close();
    }


    @Test
    public void copyFile() throws Exception {

        // 获取通道
        FileInputStream fin = new FileInputStream("readfile.txt");
        FileChannel finChannel = fin.getChannel();
        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        FileOutputStream fout = new FileOutputStream("writefile.txt");
        FileChannel foutChannel = fout.getChannel();

        int readbytes = 0;
        while ((readbytes = finChannel.read(buffer)) != -1) {
            buffer.flip();
            foutChannel.write(buffer);
            buffer.clear();
        }
        fin.close();
        fout.close();
    }
}
