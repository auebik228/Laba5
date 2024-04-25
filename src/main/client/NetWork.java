package main.client;

import commands.Exit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NetWork {
    Socket socket;
    String hostName;
    int port;
    public NetWork(String hostName, int port ){
        try {
            this.socket = new Socket(hostName,port);
        } catch (IOException e) {
            System.out.println("Неподходящие имя сети или порт, клиент не может работать");
            new Exit().use();
        }
        this.hostName = hostName;
        this.port = port;
        System.out.println("Клиент на порте " + port + " начал свою работу");
    }

    public OutputStream getSocketOut() throws IOException {
        return socket.getOutputStream();
    }
    public InputStream getSocketInput() throws IOException {
        return socket.getInputStream();
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
