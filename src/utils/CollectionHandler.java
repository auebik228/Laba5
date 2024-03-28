package utils;
import ticket.Ticket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;

public class CollectionHandler {
    private static LinkedList<Ticket> ticketList = new LinkedList<>();
    private static ArrayList<Long> ticketIdList = new ArrayList<Long>();
    private static ArrayList<Long> venueIdList = new ArrayList<Long>();
    private final static ZonedDateTime collectionCreateTime=ZonedDateTime.now();

    public static LinkedList<Ticket> getCollection() {
        return ticketList;
    }

    public static ArrayList<Long> getTicketIdList() {
        return ticketIdList;
    }

    public static ArrayList<Long> getVenueIdList() {
        return venueIdList;
    }
    public static String getCreatingtime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss Z");
        return collectionCreateTime.format(formatter);
    }
    public static int findIndexById(Long id){
        for (Ticket ticket: ticketList){
            if (ticket.getId()==id){
                return ticketList.indexOf(ticket);
            }
        }
        return 0;
    }

}
