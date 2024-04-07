package commands;

import ticket.Address;
import ticket.Coordinates;
import ticket.Ticket;
import ticket.Venue;
import utils.CollectionHandler;
import utils.ConsoleAdministrator;
import utils.Corrector;
import utils.FileWorker;

import java.io.File;
import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.logging.Logger;
/**
 * The 'Update' class is a subclass of the 'AbstractCommand' class.
 * It represents a command that updates the data of a ticket with a selected id.
 *
 * The 'Update' class has the following attributes:
 * - name: a CommandNames enum value representing the name of the command (update)
 * - specification: a string representing the description of the command ("Обновляет данные билета с выбранным id")
 * - mode: a boolean indicating the mode of the command (true for interactive mode, false for script mode)
 * - inputData: a string representing the input data for the command
 *
 * The 'Update' class provides the following methods:
 * - use(): a method that executes the update command based on the mode and input data
 *
 * In interactive mode, the 'use' method checks if the ticket with the given id exists in the collection.
 * If it exists, it updates the ticket's data with the user input values and prints a success message.
 * If it doesn't exist, it prints an error message.
 *
 * In script mode, the 'use' method reads the ticket fields from a file and creates a new ticket object.
 * It then checks if the ticket is valid based on certain criteria and updates the ticket in the collection.
 * If the ticket is not valid or the number of fields is incorrect, it prints an error message.
 *
 * The 'Update' class is used to update the data of a ticket in the collection.
 * It is part of a larger system that manages tickets and venues.
 */
public class Update extends AbstractCommand {

    public Update() {
        this.name = CommandNames.update;
        this.specification = "Обновляет данные билета с выбранным id";
        mode = true;
    }

    @Override
    public void use() {
        if (mode) {
            if (CollectionHandler.getTicketIdList().contains(Long.parseLong(getInputData()))) {
                ZonedDateTime time = ZonedDateTime.now();
                long ticketId = Long.parseLong(getInputData());
                long venueId = (long) ((Math.random() + 1) * 100);
                Ticket t = CollectionHandler.getCollection().get(CollectionHandler.findIndexById(ticketId));
                t.setName(ConsoleAdministrator.getTicketName());
                t.setCoordinates(ConsoleAdministrator.getTicketCoordinates());
                t.setCreationDate(time);
                t.setPrice(ConsoleAdministrator.getTicketPrice());
                t.setType(ConsoleAdministrator.getTicketType());
                t.getVenue().setId(venueId);
                t.getVenue().setName(ConsoleAdministrator.getVenueName());
                t.getVenue().setCapacity(ConsoleAdministrator.getVenueCapacity());
                t.getVenue().setAddress( ConsoleAdministrator.getAdress());
                System.out.println("Данные билета с id - " + t.getId() + " обновлены");
            } else {
                System.out.println("Невозможно обновить билет с id - " + Long.parseLong(getInputData()) + " так как его не существует.");

            }
        } else {
            if(CollectionHandler.getTicketIdList().contains(Long.parseLong(getInputData()))) {
                ArrayList<String> ticketFields = FileWorker.readTicketFields();
                if (ticketFields.size() == 9 || ticketFields.size() == 11) {
                    try {
                        Ticket ticket = new Ticket(9999, ticketFields.get(0),
                                new Coordinates(Long.parseLong(ticketFields.get(1)), Integer.parseInt(ticketFields.get(2))),
                                ZonedDateTime.parse(ticketFields.get(3)), FileWorker.parseTicketPrice(ticketFields.get(4)),
                                FileWorker.parseTicketType(ticketFields.get(5)), new Venue(Long.parseLong(ticketFields.get(6)),
                                ticketFields.get(7), Integer.parseInt(ticketFields.get(8)), null));
                        if (ticketFields.size() == 11) {
                            ticket.getVenue().setAddress(new Address(ticketFields.get(9), ticketFields.get(10)));
                        }
                        if (Corrector.checkTicketForCorrect(ticket)) {
                            int i = CollectionHandler.findIndexById(Long.parseLong(getInputData()));
                            CollectionHandler.getCollection().remove(i);
                            ticket.setId(Long.parseLong(getInputData()));
                            CollectionHandler.getCollection().add(i, ticket);
                            CollectionHandler.getTicketIdList().add(ticket.getId());
                            CollectionHandler.getVenueIdList().add(ticket.getVenue().getId());
                            System.out.println("Значения билета с id " + ticket.getId() + " обновлены.");
                        } else {
                            System.out.println("Данные о билете не проходят по заданным заданным в задание ограничениям");
                        }
                    } catch (DateTimeException | IllegalArgumentException e) {
                        System.out.println("Некоретнные данные о билете команда update не будет выполнена.");
                    }
                } else {
                    System.out.println("Неверное число аргументов для обновления билета");
                }
            }else{
                System.out.println("Невозможно обновить билет с id - " + Long.parseLong(getInputData()) + " так как его не существует.");
            }

        }
    }
}