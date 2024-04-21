package main.client;

import commands.Add;
import utils.Serializer;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        NetWork netWork = new NetWork("LocalHost", 1234);
        while (true) {
            Add add = new Add();
            byte[] sendObjectBytes;
            sendObjectBytes = Serializer.serializeObject(add);
            System.out.println(sendObjectBytes);
            netWork.getSocketOut().write(sendObjectBytes);
            byte[] recieveObjectBytes = new byte[1000];
            netWork.getSocketInput().read(recieveObjectBytes);
            ByteBuffer buffer = ByteBuffer.wrap(recieveObjectBytes);
            String string = (String) Serializer.deserializeObject(buffer);
            System.out.println(string);
        }

    }
}