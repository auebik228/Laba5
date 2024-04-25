package main.server;

import commands.Add;
import commands.CommandManager;
import utils.FileWorker;
import utils.Serializer;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Server {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(1488));
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        KeyHandler keyHandler = new KeyHandler(selector);
        System.out.println("Сервер начал работу на порте 1488");
        if (args.length==1){
            String filePath = args[0];
            FileWorker.loadCollection(filePath);
        }else{
            System.out.println("Неверное количество аргументов, коллекция не загружена");
        }
        while (true) {
            selector.select();
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();
                if (key.isAcceptable()) {
                    keyHandler.acceptKey(key);
                } else if (key.isReadable()) {
                    keyHandler.readKey(key);
                } else if (key.isWritable()) {
                    keyHandler.writeKey(key);
                }
            }
        }
    }
}
