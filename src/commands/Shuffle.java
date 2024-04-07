package commands;

import utils.CollectionHandler;

import java.util.Collections;
/**
 * The Shuffle class is a subclass of AbstractCommand.
 * It represents a command that shuffles the collection of tickets.
 * The class overrides the use() method from the AbstractCommand class to perform the shuffle operation.
 * If the collection is not empty, the use() method shuffles the collection using the Collections.shuffle() method.
 * If the collection is empty, it prints a message indicating that the collection is empty and there is nothing to shuffle.
 *
 * This class provides the following methods:
 * - use(): Performs the shuffle operation on the collection of tickets.
 *
 * The specification of the Shuffle command is "Перемешивает коллекцию".
 */
public class Shuffle extends AbstractCommand {
    public Shuffle() {
        this.name = CommandNames.shuffle;
        this.specification = "Перемешивает коллекцию";
    }

    @Override
    public void use() {
        if (CollectionHandler.getCollection().size()>0) {
            Collections.shuffle(CollectionHandler.getCollection());
            System.out.println("Коллекция размешана.");
        }else{
            System.out.println("Коллекция пуста нечего размешивать.");
        }
    }
}
