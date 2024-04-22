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
        while (true) {
            AbstractCommand command = ConsoleAdministrator.commandRequest();
            byte[] sendObjectBytes;
            if(command.getName()!= CommandNames.voidCommand) {
                if (command instanceof AddingCommand) {
                    AddingCommand command1 = (AddingCommand) command;
                    command1.ticketRequest();
                    sendObjectBytes = Serializer.serializeObject(command1);
                } else {
                    sendObjectBytes = Serializer.serializeObject(command);
                }
                netWork.getSocketOut().write(sendObjectBytes);
                byte[] recieveObjectBytes = new byte[1000];
                netWork.getSocketInput().read(recieveObjectBytes);
                ByteBuffer buffer = ByteBuffer.wrap(recieveObjectBytes);
                String string = (String) Serializer.deserializeObject(buffer);
                System.out.println(string);
            }
        }

    }
}