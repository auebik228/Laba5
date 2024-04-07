package commands;

import utils.CollectionHandler;
/**
 * The Info class is a subclass of AbstractCommand.
 * It represents a command that provides information about the collection.
 * The class overrides the use() method to print the creation time of the collection and the number of elements in the collection.
 *
 * This class provides the following methods:
 * - use(): Prints the creation time of the collection and the number of elements in the collection.
 *
 * The specification of this command is "Information about Collection".
 */
public class Info extends AbstractCommand {
    public Info() {
        name = CommandNames.info;
        specification = "Выводит информацию о коллекции в консоль";
    }

    @Override
    public void use() {
        System.out.println("Время создания коллекции - " + CollectionHandler.getCreatingtime() + System.lineSeparator() +
                "Количество элементов в коллекции - " + CollectionHandler.getCollection().size());
    }
}
