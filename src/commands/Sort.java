package commands;

import ticket.Ticket;
import utils.CollectionHandler;

import java.util.Collections;

public class Sort extends AbstractCommand {
    public Sort() {
        this.name = CommandNames.sort;
        this.specification = "Сортирует коллекцию";
    }

    @Override
    public void use() {
        Collections.sort(CollectionHandler.getCollection());
        System.out.println("Коллекцию отсортирована.");
    }
}
