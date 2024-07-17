package main.server;

import commands.Exit;
import commands.RemoveById;
import graphic.TicketTable;
import ticket.Coordinates;
import ticket.Ticket;
import ticket.TicketType;
import ticket.Venue;
import utils.CollectionHandler;
import utils.DataBaseManager;
import utils.FileWorker;
import graphic.TicketPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.*;

public class Server {
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        DataBaseManager.startConnection();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        try {
            serverSocket.bind(new InetSocketAddress(2675));
            System.out.println("Сервер начал работу на порте 2675");
        } catch (BindException e) {
            System.out.println("Порт занят не удалось запустить сервер");
            new Exit().use();
        }
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        KeyHandler keyHandler = new KeyHandler(selector);
        FileWorker.loadCollection(DataBaseManager.getConnection());
        System.out.println("Коллекция успешно загружена");
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
