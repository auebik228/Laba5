package utils;
import ticket.Ticket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The CollectionHandler class is responsible for managing the collection of tickets.
 * It provides methods to access and manipulate the collection, as well as retrieve information about the collection.
 * The collection is stored as a LinkedList of Ticket objects.
 * The class also maintains separate lists for ticket IDs and venue IDs, stored as ArrayLists of Long.
 * The creation time of the collection is stored as a ZonedDateTime object.
 *
 * This class provides the following methods:
 * - getCollection(): Returns the collection of tickets as a LinkedList<Ticket>.
 * - getTicketIdList(): Returns the list of ticket IDs as an ArrayList<Long>.
 * - getVenueIdList(): Returns the list of venue IDs as an ArrayList<Long>.
 * - getCreatingtime(): Returns the creation time of the collection as a formatted string.
 * - findIndexById(Long id): Finds the index of a ticket in the collection based on its ID.
 *
 * The format of the creation time string is "MM/dd/yyyy - HH:mm:ss Z".
 */


public class CollectionHandler {
    private static LinkedList<Ticket> ticketList = new LinkedList<>();
    private static ArrayList<Long> ticketIdList = new ArrayList<Long>();
    private static ArrayList<Long> venueIdList = new ArrayList<Long>();
    private final static ZonedDateTime collectionCreateTime=ZonedDateTime.now();

    /**
     * Returns the collection of tickets.
     *
     * @return the collection of tickets as a LinkedList<Ticket>
     */
    public static LinkedList<Ticket> getCollection() {
        return ticketList;
    }
    /**
     * Returns the list of ticket IDs.
     *
     * @return the list of ticket IDs as an ArrayList<Long>
     */

    public static ArrayList<Long> getTicketIdList() {
        return ticketIdList;
    }
    /**
     * Returns the list of venue IDs.
     *
     * @return the list of venue IDs as an ArrayList<Long>
     */
    public static ArrayList<Long> getVenueIdList() {
        return venueIdList;
    }
    /**
     * Returns the creation time of the collection as a formatted string.
     *
     * @return the creation time of the collection as a formatted string in the format "MM/dd/yyyy - HH:mm:ss Z"
     */
    public static String getCreatingtime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss Z");
        return collectionCreateTime.format(formatter);
    }
    /**
     * Finds the index of a ticket in the collection based on its ID.
     *
     * @param id the ID of the ticket to find the index for
     * @return the index of the ticket in the collection, or 0 if the ticket is not found
     */
    public static int findIndexById(Long id){
        for (Ticket ticket: ticketList){
            if (ticket.getId()==id){
                return ticketList.indexOf(ticket);
            }
        }
        return 0;
    }

}
