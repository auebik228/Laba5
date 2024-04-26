package commands;

import utils.Corrector;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
/**
 * The ExecuteScript class is a subclass of AbstractCommand.
 * It represents a command that executes a script from a specified file.
 * The class provides a method to use the command and execute the script.
 *
 * @version 1.0
 * @since 2021-10-01
 */
public class ExecuteScript extends AbstractCommand {
    public ExecuteScript() {
        this.name = CommandNames.executeScript;
        this.specification = "Выполняет скрипт из указанного файла";
    }

    @Override
    public String use() {
        try {
            Path path = Paths.get(getInputData());
            Scanner scanner = new Scanner(path);
            CommandManager.setFileMode(scanner);
            String s = "";
            while (scanner.hasNextLine()) {
                String[] command = scanner.nextLine().split(" ");
                CommandManager.setCountOfUsingCommands(CommandManager.getCountOfUsingCommands() + 1);
                if (Corrector.checkCommand(command)) {
                    if (command[0].equals("executeScript") && command[1].equals(path.toString()) | CommandManager.getCountOfUsingCommands() > 100) {
                       s+= "При выполнение скрипта, произошла рекурсия, выполнение скрипта окончено.";
                       return s;
                    }
                    s+= "Выполнена команда "+ command[0] + " результат: " + CommandManager.useCommand(CommandManager.createCommand(command)) + System.lineSeparator();
                }
            }
            CommandManager.setConsoleMode();
            s+="Выполнен скрипт из файла:" + path.toString();
            return s;

        } catch (IOException e) {
            return "Невозможно выполнить команду так, как файл не существует или к нему нет доступа.";
        }

    }
}
