package main;

import commands.Add;
import commands.CommandManager;


import commands.Exit;
import ticket.Ticket;
import utils.*;
import utils.FileWorker;

import java.io.File;
import java.util.*;

import static commands.CommandNames.exit;


public class Main {

    public static void main(String[] args) {
        String filePath = args[0];
        FileWorker.loadCollection(filePath);
        System.out.println("Работа программы начата.");
        try {
            while (true) {
                CommandManager.useCommand(ConsoleAdministrator.commandRequest());
            }
        } catch (NoSuchElementException e) {
            System.out.println("Вы закрыли ввод для сканера, программа не может дальше продолжать работу.");
            new Exit().use();
        }

    }
}
