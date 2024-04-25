package main.client;

import commands.*;
import utils.ConsoleAdministrator;
import utils.Serializer;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        NetWork netWork = new NetWork("LocalHost", 1488);
        Requestor requestor = new Requestor(netWork);
        requestor.startQuerying();
    }
}