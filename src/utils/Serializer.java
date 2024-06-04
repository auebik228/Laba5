package utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Serializer {
    public static byte[] serializeObject(Object object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.close();
        return baos.toByteArray();
    }
    public static Object deserializeObject(ByteBuffer buffer) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer.array());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object object = null;
        try {
            object = objectInputStream.readObject();
        }catch (ClassNotFoundException e){
            System.out.println("Не удалось десериализовать обьект");
        }
        return object;
    }
    public static String sha384Hash(String text )  {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-384");
        } catch (NoSuchAlgorithmException e) {
        }

        // Преобразование текста в байты
        byte[] messageBytes = text.getBytes();

        // Вычисление хеша
        byte[] hashBytes = md.digest(messageBytes);

        // Преобразование байтов хеша в шестнадцатеричную форму
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
