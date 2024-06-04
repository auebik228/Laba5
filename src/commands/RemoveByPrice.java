package commands;

import ticket.Ticket;
import utils.CollectionHandler;
import utils.DataBaseManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The RemoveByPrice class is a command that removes an element from the collection based on its price.
 * It extends the AbstractCommand class and implements the use() method.
 * The class provides the following functionality:
 * - Removes the element from the collection with the specified price.
 * - Updates the ticket ID list and venue ID list accordingly.
 * - Prints a message indicating whether the element was successfully removed or not.
 *
 * The RemoveByPrice class has the following attributes:
 * - name: The name of the command, which is set to CommandNames.removeByPrice.
 * - specification: A description of the command, which is set to "Удалить элемент по цене".
 *
 * The RemoveByPrice class overrides the use() method from the AbstractCommand class.
 * In the use() method, it iterates through the collection and checks if the price of each element matches the input data.
 * If a match is found, the element is removed from the collection, and the corresponding IDs are removed from the ID lists.
 * Finally, a message is printed indicating whether the element was successfully removed or not.
 */
public class RemoveByPrice extends AbstractCommand {
    public RemoveByPrice() {
        this.name = CommandNames.removeByPrice;
        this.specification = "Удалить элемент по цене";
    }

    @Override
    public String use() {
        long price = Long.parseLong(this.getInputData());
        List<Ticket> ticketsToRemove = CollectionHandler.getCollection().parallelStream()
                .filter(element -> element.getPrice() == price && element.getOwner().equals(getUser()))
                .toList();
        for (Ticket ticket : ticketsToRemove) {
                CollectionHandler.getTicketIdList().remove(ticket.getId());
                CollectionHandler.getVenueIdList().remove(ticket.getVenue().getId());
                CollectionHandler.getCollection().remove(ticket);
                try {
                    DataBaseManager.deleteTicket((int) ticket.getId());
                } catch (SQLException e) {
                    return "Проблемы с базой данных";
                }
        }
        if (ticketsToRemove.isEmpty()) {
            return "Элементы с ценой " + this.getInputData() + " не найдены.";
        } else {
            return "Элементы с ценой " + this.getInputData() + " удалены.";
        }

    }
}
