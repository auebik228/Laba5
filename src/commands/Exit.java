package commands;

public class Exit extends AbstractCommand {
    public Exit() {
        this.name = CommandNames.exit;
        this.specification = "Завершение работы программы";
    }

    @Override
    public void use() {
        System.out.println("Спасибо за работу в этой крутейшей программе, досвидания.");
        System.exit(0);
    }
}
