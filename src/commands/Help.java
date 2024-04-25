package commands;


import java.util.ArrayList;
import java.util.Map;
/**
 * The Help class is a subclass of AbstractCommand.
 * It represents a command that displays a list of available commands.
 * The use() method prints the list of commands by iterating over the CommandManager's commands HashMap.
 * The name of the command is "help" and the specification is "Выводит список команд".
 */
public class Help extends AbstractCommand {
    public Help() {
        this.name = CommandNames.help;
        this.specification = "Выводит список команд";
    }

    @Override
    public String use() {
        String string="";
        for (Map.Entry<CommandNames, AbstractCommand> entry : CommandManager.getComands().entrySet()) {
            if (entry.getKey() != CommandNames.voidCommand) {
                string += entry.getKey().toString() + ": " +entry.getValue().getSpecification() + System.lineSeparator();
            }
        }
        return string;
    }
}
