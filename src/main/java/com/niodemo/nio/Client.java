package com.niodemo.nio;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Client {

    public static void main(String[] args) {

        BufferedReader in = null;
        ClientHandler client = new ClientHandler();
        client.start();

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            String line = null;
            while ((line = reader.readLine()) != null) {
                if ("over".equals(line))
                    break;
                client.send(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}
