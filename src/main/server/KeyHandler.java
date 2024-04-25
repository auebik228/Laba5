package main.server;

import commands.AbstractCommand;
import commands.Add;
import commands.CommandManager;
import utils.Serializer;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class KeyHandler {
private final Selector selector;
private ByteBuffer bufferForRead = ByteBuffer.allocate(10000);
private ByteBuffer bufferForWrite;
public KeyHandler(Selector selector){
    this.selector = selector;
}
public void acceptKey(SelectionKey key) throws IOException {
    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
    SocketChannel clientChannel = ssc.accept();
    clientChannel.configureBlocking(false);
    clientChannel.register(selector, SelectionKey.OP_READ);
    System.out.println("Принято подключение по адресу: "+ clientChannel.getRemoteAddress());
}
public void readKey(SelectionKey key) throws IOException, ClassNotFoundException {
    SocketChannel clientChannel = (SocketChannel) key.channel();
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
    AbstractCommand command= (AbstractCommand) Serializer.deserializeObject(bufferForRead);
    System.out.println("Выполнена команда " + command.getName() + " от клиента - " + clientChannel.getLocalAddress());
    String string = CommandManager.useCommand(command);
    clientChannel.register(selector, SelectionKey.OP_WRITE);
    bufferForWrite= ByteBuffer.wrap(Serializer.serializeObject(string));
}
public void writeKey(SelectionKey key) throws IOException {
    SocketChannel clientChannel = (SocketChannel) key.channel();
    clientChannel.configureBlocking(false);
    clientChannel.write(bufferForWrite);
    bufferForWrite.clear();
    clientChannel.register(selector, SelectionKey.OP_READ);
}
}
