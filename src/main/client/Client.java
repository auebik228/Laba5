package main.client;

import commands.*;
import utils.ConsoleAdministrator;
import utils.Serializer;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {
            NetWork netWork = new NetWork("LocalHost", 1337);
            Requestor requestor = new Requestor(netWork);
            requestor.startQuerying();
        }catch (SocketException e){
            System.out.println("Сервер завершил работу, клиент тоже завершает");
            new Exit();
        }
    }
}