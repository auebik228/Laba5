package commands;

import ticket.*;
import utils.CollectionHandler;
import utils.ConsoleAdministrator;
import utils.Corrector;
import utils.FileWorker;

import java.io.Console;
import java.io.File;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class Add extends AbstractCommand implements Cloneable{
    public Add() {
        this.name = CommandNames.add;
        this.specification = "Команда для добавления нового билета в коллекцию";
        this.mode = true;
    }

    @Override
    public void use() {
        if (mode) {
            ZonedDateTime time = ZonedDateTime.now();
            long ticketId = (long) ((Math.random() + 1) * 100);
            long venueId = (long) ((Math.random() + 1) * 100);
            Ticket ticket = new Ticket(ticketId, ConsoleAdministrator.getTicketName(), ConsoleAdministrator.getTicketCoordinates(),
                    time, ConsoleAdministrator.getTicketPrice(), ConsoleAdministrator.getTicketType(),
                    new Venue(venueId, ConsoleAdministrator.getVenueName(), ConsoleAdministrator.getVenueCapacity(), ConsoleAdministrator.getAdress()));
            CollectionHandler.getCollection().add(ticket);
            CollectionHandler.getTicketIdList().add(ticketId);
            System.out.println("Билет с id " + ticket.getId() + " создан.");
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
                        CollectionHandler.getCollection().add(ticket);
                        CollectionHandler.getTicketIdList().add(ticket.getId());
                        CollectionHandler.getVenueIdList().add(ticket.getVenue().getId());
                        System.out.println("Билет с id " + ticket.getId() + " создан.");
                    } else {
                        System.out.println("Данные о билете не проходят по заданным заданным в задание ограничениям");
                    }
                } catch (DateTimeException | IllegalArgumentException e) {
                    System.out.println("Некоретнные данные о билете команда add не будет выполнена.");
                }
            } else {
                System.out.println("Неверное число аргументов для создания билета");
            }
        }
    }


}


