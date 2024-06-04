package main.server;

import commands.AbstractCommand;
import commands.Add;
import commands.CommandManager;
import utils.Serializer;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class KeyHandler {
    private final Selector selector;
    private volatile ByteBuffer bufferForRead = ByteBuffer.allocate(10000);
    private volatile ByteBuffer bufferForWrite = ByteBuffer.allocate(10000);
    private final ForkJoinPool forkpool = new ForkJoinPool();
    private final ExecutorService fixedpool = Executors.newFixedThreadPool(100);
    private AbstractCommand command;

    public KeyHandler(Selector selector) {
        this.selector = selector;
    }

    public void acceptKey(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = ssc.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("Принято подключение по адресу: " + clientChannel.getRemoteAddress());
    }

    public void readKey(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        RecursiveAction action = new RecursiveAction() {
            @Override
            protected void compute() {
                try {
                    clientChannel.configureBlocking(false);
                    bufferForRead.clear();
                    int bytesRead;
                    try {
                        bytesRead = clientChannel.read(bufferForRead);
                    } catch (IOException e) {
                        key.cancel();
                        clientChannel.close();
                        return;
                    }
                    if (bytesRead == -1) {
                        key.cancel();
                        return;
                    }
                    bufferForRead.flip();
                    command = (AbstractCommand) Serializer.deserializeObject(bufferForRead);
                    System.out.println("Выполнена команда " + command.getName() + " от клиента - " + clientChannel.getRemoteAddress());
                } catch (IOException e) {

                }
            }
        };
        forkpool.invoke(action);
        RecursiveAction action1 = new RecursiveAction() {
            @Override
            protected void compute() {
                try {
                    forkpool.invoke(action);
                    String string = CommandManager.useCommand(command);
                    clientChannel.register(selector, SelectionKey.OP_WRITE);
                    bufferForWrite = ByteBuffer.wrap(Serializer.serializeObject(string));
                } catch (IOException e) {

                }
            }
        };
        try {
            forkpool.invoke(action1);
        } catch (NullPointerException e) {
        }

    }

    public void writeKey(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        clientChannel.configureBlocking(false);
        clientChannel.write(bufferForWrite);
        bufferForWrite.clear();
        clientChannel.register(selector, SelectionKey.OP_READ);
    }
}
