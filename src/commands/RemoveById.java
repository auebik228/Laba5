package commands;

import ticket.Ticket;
import utils.CollectionHandler;

import java.util.Collection;
import java.util.Map;
public class RemoveById extends AbstractCommand{
    /**
     * The RemoveById class is a command class that removes an element from the collection based on its ID.
     * It extends the AbstractCommand class and implements the use() method.
     * The class provides the following functionality:
     * - Removes the element from the collection with the specified ID.
     * - Removes the corresponding ID from the ticket ID list and venue ID list.
     * - Prints a message indicating whether the element was successfully removed or if the ID was not found.
     *
     * The RemoveById class has the following attributes:
     * - name: The name of the command, which is set to CommandNames.removeById.
     * - specification: A description of the command, which is set to "Удалаяет элемент по номеру".
     *
     * The RemoveById class overrides the use() method from the AbstractCommand class.
     * The use() method iterates through the collection and checks if the ID of each element matches the specified ID.
     * If a match is found, the element is removed from the collection, and its ID is removed from the ticket ID list and venue ID list.
     * If no match is found, a message is printed indicating that there is no element with the specified ID.
     *
     * Example usage:
     * RemoveById removeCommand = new RemoveById();
     * removeCommand.setInputData("123");
     * removeCommand.use();
     *
     * This will remove the element with ID 123 from the collection, ticket ID list, and venue ID list.
     * If the element is found, a message will be printed indicating that the element was removed.
     * If the element is not found, a message will be printed indicating that there is no element with the specified ID.
     */
    public RemoveById(){
        this.name=CommandNames.removeById;
        this.specification="Удалаяет элемент по номеру";
    }
    @Override

    public String use() {
        for (int i = 0; i < CollectionHandler.getCollection().size(); i++) {
            if (CollectionHandler.getCollection().get(i).getId() == Long.parseLong(this.getInputData())) {
                CollectionHandler.getTicketIdList().remove(CollectionHandler.getCollection().get(i).getId());
                CollectionHandler.getVenueIdList().remove(CollectionHandler.getCollection().get(i).getVenue().getId());
                CollectionHandler.getCollection().remove(i);
                return "Элемент с id " + this.getInputData() + " удален.";
            }
        }
           return "Элемента с таким id нет.";
    }

}
