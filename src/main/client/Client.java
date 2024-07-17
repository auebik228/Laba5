package main.client;

import commands.*;
import graphic.LoginRegisterFrame;
import graphic.TicketPanel;
import graphic.TicketTable;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.sql.SQLException;

public class Client {
    private static volatile boolean isAuthorisated = false;
    private static String currentUser;
    private static TicketTable table;
    private static TicketPanel ticketPanel;
    private  static Frame mainFrame;

    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            NetWork netWork = new NetWork("localhost", 2675);
            Requestor requestor = new Requestor(netWork);
            DataBaseManager.startConnection();
            FileWorker.loadCollection(DataBaseManager.getConnection());
            table = new TicketTable(CollectionHandler.getCollection(),requestor);
            javax.swing.SwingUtilities.invokeLater(() -> {
                table.setVisible(false);
            });
            JFrame frame = new JFrame("Ticket Visualizer");
            ticketPanel = new TicketPanel(CollectionHandler.getCollection(),requestor);
            frame.add(ticketPanel);
            frame.pack();
            frame.setSize(800,600);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(false);
            mainFrame = frame;
            while (true) {
                if (!isAuthorisated) {
                    if (!LoginRegisterFrame.getIsOpen()) {
                        SwingUtilities.invokeLater(() -> new LoginRegisterFrame().setVisible(true));
                        Thread.sleep(100);// задержка в 500 миллисекунд
                    }
                } else {
                    table.setVisible(true);
                    frame.setVisible(true);
                    requestor.query(currentUser);
                }
            }
        } catch (SocketException  e) {
            System.out.println("Сервер завершил работу, клиент тоже завершает");
            new Exit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    public static String getCurrentUser(){
        return currentUser;
    }
    public static void updateDataForGraphic() {
        CollectionHandler.getCollection().clear();
        FileWorker.loadCollection(DataBaseManager.getConnection());
        ticketPanel.updateTableData(CollectionHandler.getCollection());
        table.updateTableData(CollectionHandler.getCollection());
        SwingUtilities.invokeLater(() -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ticketPanel);
            if (frame != null) {
                frame.setTitle(LanguageManager.getString("title"));
                frame.repaint();
            }
        });
    }
    public static void logout() {
        isAuthorisated = false;
        currentUser = null;

        // Закрываем текущие окна
        if (table != null) {
            table.dispose();
        }
        if (ticketPanel != null) {
            mainFrame.dispose();
        }
        SwingUtilities.invokeLater(() -> new LoginRegisterFrame().setVisible(true));
    }

}