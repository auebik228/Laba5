package commands;

import main.client.Client;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 */
public class ExitAccount extends AbstractCommand{
    public ExitAccount(){
        this.name = CommandNames.exitAccount;
        this.specification = "Выйти из аккаунта";
    }
    @Override
    public String use() {
        Client.setIsAuthorisated(true);
        Client.setCurrentUser("");
        System.out.println("Вы вышли из акканута");
        return null;
    }
}
