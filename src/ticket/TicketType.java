package ticket;

import java.io.Serializable;

/**
 * The 'TicketType' class represents the different types of tickets available.
 * It is an enumeration class that defines the following ticket types:
 * - VIP: Represents a VIP ticket.
 * - USUAL: Represents a usual ticket.
 * - BUDGETARY: Represents a budgetary ticket.
 * - CHEAP: Represents a cheap ticket.
 *
 * This class is used to categorize tickets based on their type and provide a way to
 * differentiate between different ticket types in the system.
 */
public enum TicketType implements Serializable {
    VIP,
    USUAL,
    BUDGETARY,
    CHEAP;
}
