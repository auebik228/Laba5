package main.server;

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
private ByteBuffer buffer = ByteBuffer.allocate(10000);
private ByteBuffer buffer1;
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
    buffer.clear();
    System.out.println(buffer);
    int bytesRead;
    try {
        bytesRead = clientChannel.read(buffer);
        System.out.println(buffer);
    } catch (IOException e) {
        key.cancel();
        clientChannel.close();
        return;
    }

    if (bytesRead == -1) {
        key.cancel();
        return;
    }
    buffer.flip();
    Add add = (Add) Serializer.deserializeObject(buffer);
    System.out.println(add);
    String string = CommandManager.useCommand(add);
    clientChannel.register(selector, SelectionKey.OP_WRITE);
    buffer1= ByteBuffer.wrap(Serializer.serializeObject(string));
}
public void writeKey(SelectionKey key) throws IOException {
    SocketChannel clientChannel = (SocketChannel) key.channel();
    clientChannel.configureBlocking(false);
    clientChannel.write(buffer1);
    buffer1.clear();
    clientChannel.register(selector, SelectionKey.OP_READ);
}
}
