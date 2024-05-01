package commands;

import utils.Corrector;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    ArrayList<AbstractCommand> fileCommands;
    public ExecuteScript() {
        this.name = CommandNames.executeScript;
        this.specification = "Выполняет скрипт из указанного файла";
        fileCommands = new ArrayList<AbstractCommand>();
    }

    @Override
    public String use() {
            String s = "";
            for(AbstractCommand command : fileCommands){

                    s+= "Выполнена команда "+ command.getName() + " результат: " + CommandManager.useCommand(command) + System.lineSeparator();
            }
            s+="Выполнен скрипт из файла:" + getInputData();
            fileCommands.clear();
            System.out.println(fileCommands);
            return s;
    }
    public void readCommandsFromFile() throws IOException {
        try {
            fileCommands.clear();
            Path path = Paths.get(getInputData());
            Scanner scanner = new Scanner(path);
            CommandManager.setFileMode(scanner);
            while (scanner.hasNextLine()) {
                String[] command = scanner.nextLine().split(" ");
                if (Corrector.checkCommand(command)) {
                    AbstractCommand comm = CommandManager.createCommand(command);
                    if (comm instanceof AddingCommand) {
                        ((AddingCommand) comm).ticketRequest();
                        System.out.println(((AddingCommand) comm).getTicket());
                    }
                        fileCommands.add(comm);
                }
            }
            CommandManager.setConsoleMode();
        }catch (IOException e) {
            System.out.println( "Невозможно выполнить executeScript так, как файл не существует или к нему нет доступа.");
        }
    }
}
