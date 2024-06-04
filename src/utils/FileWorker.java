package utils;

import commands.CommandManager;
import commands.CommandNames;
import ticket.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
/**
 * The FileWorker class is responsible for loading and saving the collection of tickets from/to a file.
 * It provides methods to load the collection from a file, save the collection to a file, and read ticket fields from the console.
 * The class uses the Gson library to serialize and deserialize the collection.
 * The class also provides methods to parse ticket price and ticket type from strings.
 *
 * This class provides the following methods:
 * - loadCollection(String filePath): Loads the collection of tickets from a file.
 * - saveCollection(File file): Saves the collection of tickets to a file.
 * - setScannerForRead(Scanner scanner): Sets the scanner for reading ticket fields from the console.
 * - readTicketFields(): Reads ticket fields from the console and returns them as an ArrayList<String>.
 * - parseTicketPrice(String s): Parses a string to a Long representing the ticket price.
 * - parseTicketType(String s): Parses a string to a TicketType enum value.
 */

public class FileWorker {
    private static Scanner scannerForRead;

    public static void loadCollection(Connection connection) {
        String selectTickets = "SELECT * FROM ticket";
        String selectVenue = "SELECT * FROM venue WHERE id = ?";
        String selectCoordinates = "SELECT * FROM coordinates WHERE id = ?";
        String selectAddress = "SELECT * FROM address WHERE id = ?";
        try {
            PreparedStatement pstmSelectTickets = connection.prepareStatement(selectTickets);
            ResultSet tickets = pstmSelectTickets.executeQuery();
            while (tickets.next()){
                Ticket ticket = new Ticket();
                Long ticketId = tickets.getLong(1);
                Long venueId = tickets.getLong(7);
                ticket.setId(tickets.getLong(1));
                ticket.setName(tickets.getString(2));
                ticket.setCreationDate(LocalDateTime.parse(tickets.getTimestamp(4).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")).atZone(ZoneId.of("UTC")));
                ticket.setPrice((long) tickets.getInt(5));
                ticket.setType(TicketType.valueOf(tickets.getString(6)));
                ticket.setOwner(tickets.getString(8));

                PreparedStatement pstmSelectCoordinates = connection.prepareStatement(selectCoordinates);
                pstmSelectCoordinates.setInt(1,tickets.getInt(3));
                ResultSet coordinates = pstmSelectCoordinates.executeQuery();
                coordinates.next();
                ticket.setCoordinates(new Coordinates(coordinates.getLong(2), coordinates.getInt(3)));

                PreparedStatement pstmSelectVenue = connection.prepareStatement(selectVenue);
                pstmSelectVenue.setInt(1,tickets.getInt(7));
                ResultSet venues = pstmSelectVenue.executeQuery();
                venues.next();
                Integer adressId = venues.getInt(4);
                ticket.setVenue(new Venue(venues.getLong(1), venues.getString(2), venues.getInt(3), null));
                if(adressId != 0){
                    PreparedStatement pstmSelectAddress = connection.prepareStatement(selectAddress);
                    pstmSelectAddress.setInt(1,adressId);
                    ResultSet addreses = pstmSelectAddress.executeQuery();
                    addreses.next();
                    ticket.getVenue().setAddress(new Address(addreses.getString(2), addreses.getString(3)));
                }
                CollectionHandler.getCollection().add(ticket);
                CollectionHandler.getTicketIdList().add(ticketId);
                CollectionHandler.getVenueIdList().add(venueId);

            }
            System.out.println("Коллекция успешно загружена");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке коллекции");
        }
    }

    public static String saveCollection(File file) {
        try {
            FileOutputStream writer = new FileOutputStream(file);
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(java.time.ZonedDateTime.class, new ZonedDateTimeAdapter());
            Gson gson = builder.create();
            String s = gson.toJson(CollectionHandler.getCollection());
            writer.write(s.getBytes());
            return "Коллекция сохранена в файл с названием - " + file + ".";
        } catch (IOException e) {
            return "Неверное имя файла или файл не может быть открыт или не может быть создан.";
        }

    }

    public static void setScannerForRead(Scanner scanner) {
        scannerForRead = scanner;
    }

    public static ArrayList<String> readTicketFields() {
        ArrayList<String> ticketFields = new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\b(" + String.join("|", getStringValues(CommandNames.values())) + ")\\b");
        while (!scannerForRead.hasNext(pattern) & scannerForRead.hasNextLine()) {
            String s = scannerForRead.nextLine();
            if (Corrector.checkCommandName(s)) {
                break;
            }
            ticketFields.add(s);
        }
        return ticketFields;
    }

    public static Long parseTicketPrice(String s) throws NumberFormatException {
        if (s.equals("null")) {
            return null;
        } else {
            return Long.parseLong(s);
        }
    }

    public static TicketType parseTicketType(String s) throws IllegalArgumentException {
        if (s.equals("null")) {
            return null;
        } else {
            return TicketType.valueOf(s);
        }
    }

    private static String[] getStringValues(Enum<?>[] enums) {
        String[] stringValues = new String[enums.length];
        for (int i = 0; i < enums.length; i++) {
            stringValues[i] = enums[i].toString();
        }
        return stringValues;
    }
}



