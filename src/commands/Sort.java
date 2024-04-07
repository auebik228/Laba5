package commands;

import ticket.Ticket;
import utils.CollectionHandler;

import java.util.Collections;
/**
 * The Sort class is a subclass of AbstractCommand.
 * It represents a command that sorts the collection of tickets.
 * The class overrides the use() method from the superclass to implement the sorting functionality.
 * If the collection is not empty, the use() method sorts the collection using the Collections.sort() method.
 * If the collection is empty, it prints a message indicating that the collection is empty and there is nothing to sort.
 *
 * This class provides the following methods:
 * - use(): Sorts the collection of tickets.
 *
 * The Sort class is used in the ticket management system to sort the collection of tickets.
 */
public class Sort extends AbstractCommand {
    public Sort() {
        this.name = CommandNames.sort;
        this.specification = "Сортирует коллекцию";
    }

    @Override
    public void use() {
        if(CollectionHandler.getCollection().size()>0) {
            Collections.sort(CollectionHandler.getCollection());
            System.out.println("Коллекция отсортирована.");
        }else{
            System.out.println("Коллекция пуста нечего сортировать.");
        }
    }
}
