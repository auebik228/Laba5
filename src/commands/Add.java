package commands;

import ticket.*;
import utils.CollectionHandler;
import utils.ConsoleAdministrator;
import utils.Corrector;
import utils.FileWorker;

import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * The 'Add' class is a command for adding a new ticket to the collection.
 * It extends the 'AbstractCommand' class and implements the 'Cloneable' interface.
 * <p>
 * Attributes:
 * - name: The name of the command (CommandNames.add)
 * - specification: The description of the command ("Команда для добавления нового билета в коллекцию")
 * - mode: The mode of the command (true or false)
 * <p>
 * Methods:
 * - use(): Executes the command based on the mode. If the mode is true, it creates a new ticket with random values and adds it to the collection.
 * If the mode is false, it reads the ticket fields from a file and creates a ticket object with the provided values.
 * It then checks if the ticket data is valid and adds it to the collection if it is.
 * <p>
 * Note: The 'Add' class is used in a ticket management system to add new tickets to the collection.
 */
public class Add extends AddingCommand {
    public Add() {
        this.name = CommandNames.add;
        this.specification = "Команда для добавления нового билета в коллекцию";
        this.mode = true;
    }

    @Override
    public String use() {

        if (ticket!=null) {
            CollectionHandler.getCollection().add(getTicket());
            CollectionHandler.getTicketIdList().add(getTicket().getId());
            CollectionHandler.getTicketIdList().add(getTicket().getId());
            long id = this.ticket.getId();
            this.ticket = null;

            return "Билет с id " + id + " создан.";
        }else {
            return "Неверные данные о билете команда add не будет выполнена";
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
                    }else{
                        ticket = null;
                    }
                } catch (DateTimeException | IllegalArgumentException e) {
                    ticket=null;
                }
            }
        }
    }
}



