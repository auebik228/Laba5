package utils;

import commands.CommandNames;
import ticket.*;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
/**
 * The Corrector class provides methods for checking the correctness of various fields and arguments in the ticket management system.
 * It includes methods for checking the correctness of ticket fields, coordinates, venue, address, zip code, price, and command arguments.
 * The class also provides methods for checking the correctness of command names and IDs for tickets and venues.
 *
 * @version 1.0
 * @since 2021-10-01
 */

public class Corrector {
    public static boolean checkTicketForCorrect(Ticket ticket) {
        return checkTicketFields(ticket) && checkCoordinates(ticket.getCoordinates()) &&
                checkVenue(ticket.getVenue()) && checkAddress(ticket.getVenue().getAddress());
    }


    public static boolean checkTicketFields(Ticket ticket) {
        return checkTicketId(ticket.getId()) && checkTicketName(ticket.getName()) &&
                checkPrice(ticket.getPrice());
    }

    public static boolean checkCoordinates(Coordinates coordinates) {
        if (coordinates != null) {
            return checkCoordinatesX(coordinates.getX());
        }
        return false;
    }

    public static boolean checkVenue(Venue venue) {
        if (venue != null) {
            return (checkVenueId(venue.getId()) && checkVenueName(venue.getName()) && checkVenueCapacity(venue.getCapacity()));
        }

        return false;
    }

    public static boolean checkAddress(Address address) {
        if (address != null) {
            return address.getStreet() != null && checkZipCode(address.getZipCode());
        }
        return true;
    }


    public static boolean checkZipCode(String string) {
        if (string != null) {
            return string.length() >= 5;
        }
        return true;
    }

    public static boolean checkPrice(Long a) {
        if (a == null) return true;
        return a > 0;
    }

    public static boolean checkArgument(CommandNames commandName, String string, long length) {
        try {
            if (commandName == CommandNames.removeById || commandName == CommandNames.update ||
                    commandName == CommandNames.removeByPrice) {
                Long.parseLong(string);
                return true;
            }
            if (commandName == CommandNames.insertAtIndex) {
                Integer.parseInt(string);
                return true;
            }
            if (commandName == CommandNames.executeScript) {
                Paths.get(string);
                return true;
            }
            if (commandName == CommandNames.save) {
                return true;
            }
            return length == 1;
        } catch (NumberFormatException | InvalidPathException | NullPointerException e) {
            return false;
        }
    }

    public static boolean checkCommand(String[] command) {
        if ((command.length == 2 && Corrector.checkCommandName(command[0]) && Corrector.checkArgument(CommandNames.valueOf(command[0]), command[1], 2)
                || (command.length == 1 && Corrector.checkCommandName(command[0]) && Corrector.checkArgument(CommandNames.valueOf(command[0]), null, 1)))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkCommandName(String string) {
        try {
            CommandNames.valueOf(string);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean checkTicketId(Long id) {
        return (id > 0) && !CollectionHandler.getTicketIdList().contains(id);
    }

    public static boolean checkTicketName(String name) {
        return name != null && !name.isEmpty();
    }

    public static boolean checkVenueId(Long id) {
        return id > 0 && !CollectionHandler.getVenueIdList().contains(id);
    }

    public static boolean checkVenueName(String name) {
        return name != null && !name.isEmpty();
    }

    public static boolean checkVenueCapacity(Long capacity) {
        return capacity > 0;
    }

    public static boolean checkStreet(String street) {
        return street != null;
    }

    public static boolean checkCoordinatesX(long x) {
        return x > -223;
    }

    public static boolean checkCapacity(int a) {
        return a > 0;
    }

}


