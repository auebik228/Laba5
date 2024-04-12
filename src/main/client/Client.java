package main.client;

import commands.Add;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080)) {
            // Получение выходного потока для отправки данных на сервер
//            OutputStream outputStream = socket.getOutputStream();
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//            InputStream inputStream = socket.getInputStream();
//            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//            String add = new String();
//            objectOutputStream.writeObject(add);
//            objectOutputStream.flush();
            ObjectOutputStream  oos = new ObjectOutputStream(socket.getOutputStream());
            ByteArrayOutputStream outByte = new ByteArrayOutputStream();
            ObjectOutputStream outObject = new ObjectOutputStream(outByte);
            outObject.writeObject("lalalal");
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}