package main.client;

import commands.*;
import graphic.LoginRegisterFrame;
import utils.ConsoleAdministrator;
import utils.DataBaseManager;
import utils.Serializer;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.sql.SQLException;

public class Client {
    private static volatile boolean isAuthorisated = false;
    private static String currentUser;

    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            NetWork netWork = new NetWork("localhost", 2675);
            Requestor requestor = new Requestor(netWork);
            while (true) {
                if (!isAuthorisated) {
                    if (!LoginRegisterFrame.getIsOpen()) {
                        SwingUtilities.invokeLater(() -> new LoginRegisterFrame().setVisible(true));
                        Thread.sleep(100);// задержка в 500 миллисекунд
                    }
                } else {
                    requestor.query(currentUser);
                }
            }
        } catch (SocketException  e) {
            System.out.println("Сервер завершил работу, клиент тоже завершает");
            new Exit();
        }
    }

    public static void authorization() {
        if (ConsoleAdministrator.getLoginOrRegister()) {
            String login = ConsoleAdministrator.getLogin();
            String password = Serializer.sha384Hash(ConsoleAdministrator.getPassword());
            try {
                if (DataBaseManager.validateUser(login, password)) {
                    isAuthorisated = true;
                    currentUser = login;
                    System.out.println("Успешный вход в аккаунт");
                } else {
                    System.out.println("Неверный логин или пароль");
                }
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println("Ошибка с базой данных при авторизации");
            }
        } else {
            String login = ConsoleAdministrator.getLogin();
            String password = Serializer.sha384Hash(ConsoleAdministrator.getPassword());
            try {
                DataBaseManager.addUser(login, password);
                System.out.println("Пользователь успешно добавлен");
                currentUser = login;
                isAuthorisated = true;
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println("Не удалось добавить пользователя - либо такой пользователь уже существует либо проблемы с базой данных");
            }

        }
    }

    public static void setIsAuthorisated(boolean is) {
        isAuthorisated = is;
    }
    public static void setCurrentUser(String s){
        currentUser = s;
    }
}