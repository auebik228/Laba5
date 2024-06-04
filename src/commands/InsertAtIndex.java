package commands;

import ticket.Address;
import ticket.Coordinates;
import ticket.Ticket;
import ticket.Venue;
import utils.*;

import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * The Info class is a subclass of AbstractCommand.
 * It represents a command that provides information about the collection.
 * The class overrides the use() method to print the creation time of the collection and the number of elements in the collection.
 * <p>
 * This class provides the following methods:
 * - use(): Prints the creation time of the collection and the number of elements in the collection.
 * <p>
 * The specification of this command is "Infromation about collection".
 */
public class InsertAtIndex extends AddingCommand {


    public InsertAtIndex() {
        this.name = CommandNames.insertAtIndex;
        this.specification = "Команда для создания билета и помещения его на конкретный индекс в коллекции";
        mode = true;
    }

    @Override
    public String use() {
        if (ticket != null) {
            if (CollectionHandler.getCollection().size() - Integer.parseInt(getInputData()) > 0 && Integer.parseInt(getInputData()) >= 0) {
                String[] ticketAndVenueId = new String[0];
                try {
                    ticketAndVenueId = DataBaseManager.addTicket(ticket);
                    ticket.setId(Long.parseLong(ticketAndVenueId[0]));
                    ticket.getVenue().setId(Long.parseLong(ticketAndVenueId[1]));
                    CollectionHandler.getCollection().add(ticket);
                    CollectionHandler.getTicketIdList().add(ticket.getId());
                    CollectionHandler.getVenueIdList().add(ticket.getVenue().getId());
                    long id = this.ticket.getId();
                    return "Билет с id " + id + " создан на " + Integer.parseInt(getInputData()) + " индексе";
                }catch (SQLException e) {
                    return "Ошибка при работе с базой данных";
                }
            } else {
                return "Невозможно вставить на данный индекс так как размер коллекции - " + CollectionHandler.getCollection().size();
            }
        } else {
            return "Неверные данные о билете команда insertAtIndex не будет выполнена";
        }
    }


    @Override
    public void ticketRequest() {
        if (mode) {
            ZonedDateTime time = ZonedDateTime.now();
            long ticketId = (long) ((Math.random() + 1) * 100);
            long venueId = (long) ((Math.random() + 1) * 100);
            Ticket ticket = new Ticket(ticketId, ConsoleAdministrator.getTicketName(), ConsoleAdministrator.getTicketCoordinates(),
                    time, ConsoleAdministrator.getTicketPrice(), ConsoleAdministrator.getTicketType(),
                    new Venue(venueId, ConsoleAdministrator.getVenueName(), ConsoleAdministrator.getVenueCapacity(),
                            ConsoleAdministrator.getAdress()));
            setTicket(ticket);
        } else {
            ArrayList<String> ticketFields = FileWorker.readTicketFields();
            if (ticketFields.size() == 10 || ticketFields.size() == 12) {
                try {
                    Ticket ticket = new Ticket(Long.parseLong(ticketFields.get(0)), ticketFields.get(1),
                            new Coordinates(Long.parseLong(ticketFields.get(2)), Integer.parseInt(ticketFields.get(3))),
                            ZonedDateTime.parse(ticketFields.get(4)), FileWorker.parseTicketPrice(ticketFields.get(5)),
                            FileWorker.parseTicketType(ticketFields.get(6)), new Venue(Long.parseLong(ticketFields.get(7)),
                            ticketFields.get(8), Integer.parseInt(ticketFields.get(9)), null));
                    if (ticketFields.size() == 12) {
                        ticket.getVenue().setAddress(new Address(ticketFields.get(10), ticketFields.get(11)));
                    }
                    if (Corrector.checkTicketForCorrect(ticket)) {
                        setTicket(ticket);
                    }
                } catch (DateTimeException | IllegalArgumentException e) {
                    ticket=null;
                }
            }
        }
    }
}

