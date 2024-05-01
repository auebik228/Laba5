package commands;

import ticket.Ticket;
import utils.CollectionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Show class is a subclass of AbstractCommand.
 * It represents a command that displays information about the collection.
 * The class overrides the use() method from the AbstractCommand class to implement the functionality of the command.
 * If the collection is not empty, the use() method iterates through the collection and prints each element.
 * If the collection is empty, it prints a message indicating that the collection is empty.
 *
 * This class provides the following methods:
 * - use(): Displays information about the collection.
 *
 * The specification of the Show command is "Отображает данные о коллекции".
 */
public class Show extends AbstractCommand {
    public Show() {
        this.name = CommandNames.show;
        this.specification = "Отображает данные о коллекции";
    }

    @Override
    public String use() {
        if(CollectionHandler.getCollection().size()>0) {
            List<Ticket> collection ;
            collection= CollectionHandler
                    .getCollection()
                    .stream()
                    .sorted()
                    .collect(Collectors.toList());
            return collection.toString();

        }else{
            return "Коллекция пуста нечего выводить.";
        }
    }
}

