package commands;


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
    public void use() {
        System.out.println("Список команд");
        for (Map.Entry<CommandNames, AbstractCommand> entry : CommandManager.getComands().entrySet()) {
            if (entry.getKey() != CommandNames.voidCommand) {
                System.out.println(entry.getValue());
            }
        }
    }
}
