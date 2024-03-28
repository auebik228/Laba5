package commands;


import java.util.Map;

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
