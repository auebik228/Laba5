package commands;
/**
 * The 'Exit' class is a subclass of the 'AbstractCommand' class.
 * It represents the command to exit the program.
 *
 * The 'Exit' class has the following attributes:
 * - name: a CommandNames enum representing the name of the command (exit)
 * - specification: a String representing the specification of the command ("Завершение работы программы")
 *
 * The 'Exit' class has the following methods:
 * - use(): a method that prints a farewell message and exits the program
 *
 * Example usage:
 * Exit exitCommand = new Exit();
 * exitCommand.use();
 *
 * Output:
 * Спасибо за работу в этой крутейшей программе, досвидания.
 *
 * Note: The 'Exit' class inherits the 'name', 'specification', 'mode', and 'inputData' attributes from the 'AbstractCommand' class.
 * The 'name' attribute is set to CommandNames.exit and the 'specification' attribute is set to "Завершение работы программы" in the constructor.
 */
public class Exit extends AbstractCommand {
    public Exit() {
        this.name = CommandNames.exit;
        this.specification = "Завершение работы программы";
    }

    @Override
    public String use() {
        System.out.println("Спасибо за работу в этой крутейшей программе, досвидания.");
        System.exit(0);
        return null;
    }
}
