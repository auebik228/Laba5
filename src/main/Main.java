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
    /**
     * The Main class is the entry point of the program.
     * It contains the main method which loads the collection from a file,
     * starts the program, and handles user commands.
     * The program continues to prompt the user for commands until the user decides to exit.
     * The Main class uses the CommandManager class to execute the user commands.
     * If the user closes the input scanner, the program will exit gracefully.
     */
    public static void main(String[] args) {
        if (args.length==1){
            String filePath = args[0];
            FileWorker.loadCollection(filePath);
        }else{
            System.out.println("Неверное количество аргументов, коллекция не загружена");
        }System.out.println("Работа программы начата.");
        try {
            while (true) {;
              System.out.println(CommandManager.useCommand(ConsoleAdministrator.commandRequest()));
            }
        } catch (NoSuchElementException e) {
            System.out.println("Вы закрыли ввод для сканера, программа не может дальше продолжать работу.");
            new Exit().use();
        }

    }
}
