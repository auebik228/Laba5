package commands;

import utils.FileWorker;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;
/**
 * The CommandManager class is responsible for managing the commands in the program.
 * It contains a HashMap that maps each command name to its corresponding AbstractCommand object.
 * The class provides methods to use a command, get the list of commands, and set the count of using commands.
 * It also provides methods to set the file mode and console mode for certain commands.
 */
public class CommandManager {
    final static private HashMap<CommandNames, AbstractCommand> commands = new HashMap<>();
    public static int countOfUsingCommands;

    static {
        commands.put(CommandNames.add, new Add());
        commands.put(CommandNames.clear, new Clear());
        commands.put(CommandNames.help, new Help());
        commands.put(CommandNames.show, new Show());
        commands.put(CommandNames.info, new Info());
        commands.put(CommandNames.removeById, new RemoveById());
        commands.put(CommandNames.save, new Save());
        commands.put(CommandNames.sort, new Sort());
        commands.put(CommandNames.shuffle, new Shuffle());
        commands.put(CommandNames.voidCommand, new VoidCommand());
        commands.put(CommandNames.exit, new Exit());
        commands.put(CommandNames.printTypes, new PrintFieldAscendingType());
        commands.put(CommandNames.removeByPrice, new RemoveByPrice());
        commands.put(CommandNames.insertAtIndex, new InsertAtIndex());
        commands.put(CommandNames.update, new Update());
        commands.put(CommandNames.executeScript, new ExecuteScript());
    }

    static public String useCommand(AbstractCommand abstractCommand) {
        return abstractCommand.use();
    }
    public static AbstractCommand createCommand(String[] command){
        if(command.length==2) {
            var commmand = commands.get(CommandNames.valueOf(command[0])).clone();
            commmand.setInputData(command[1]);
            return commmand;
        }else{
            var commmand = commands.get(CommandNames.valueOf(command[0])).clone();
            return commmand;
        }
    }

    static public HashMap<CommandNames, AbstractCommand> getComands() {
        return commands;
    }

    static public void setCountOfUsingCommands(int i) {
        countOfUsingCommands = i;
    }

    static public int getCountOfUsingCommands() {

        return countOfUsingCommands;
    }
    static public void setFileMode(Scanner scanner){
        commands.get(CommandNames.update).setMode(false);
        commands.get(CommandNames.add).setMode(false);
        commands.get(CommandNames.insertAtIndex).setMode(false);
        FileWorker.setScannerForRead(scanner);
    }
    static public void setConsoleMode(){
        commands.get(CommandNames.update).setMode(true);
        commands.get(CommandNames.add).setMode(true);
        commands.get(CommandNames.insertAtIndex).setMode(true);
    }
}
