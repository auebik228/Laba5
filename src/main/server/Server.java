package main.server;

import commands.Add;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Server {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Создание серверного сокета и настройка на неблокирующий режим
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        // Получение серверного сокета и привязка к порту
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(8080));

        // Создание селектора
        Selector selector = Selector.open();

        // Регистрация серверного сокета в селекторе для прослушивания подключений
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();
                if (key.isAcceptable()) {
                    SocketChannel clientChannel = serverSocketChannel.accept();
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    clientChannel.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(4096);
                    buffer.clear();
                    clientChannel.read(buffer);
                    buffer.flip();
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer.array());
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    String string = (String)objectInputStream.readObject();
                    System.out.println(string);

                    clientChannel.register(selector, SelectionKey.OP_WRITE);
                } else if (key.isWritable()) {

                }
            }
        }
    }
}
