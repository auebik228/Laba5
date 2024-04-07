package commands;

import utils.CollectionHandler;
/**
 * The Clear class is a subclass of AbstractCommand.
 * It represents a command to clear the collection of tickets.
 * When executed, it clears the ticket list, ticket ID list, and venue ID list.
 * After clearing the collection, it prints a message indicating that the collection has been cleared.
 *
 * This class provides the following methods:
 * - use(): Clears the collection of tickets and prints a message indicating that the collection has been cleared.
 *
 * Example usage:
 * Clear clearCommand = new Clear();
 * clearCommand.use();
 *
 * Output:
 * Коллекция очищена.
 */
public class Clear extends AbstractCommand {
    public Clear() {
        this.name = CommandNames.clear;
        this.specification = "Очистить коллекцию";
    }

    @Override
    public void use() {
        CollectionHandler.getCollection().clear();
        CollectionHandler.getVenueIdList().clear();;
        CollectionHandler.getTicketIdList().clear();
        System.out.println("Коллекция очищена.");

    }
}
