package utils;

import commands.Add;
import commands.CommandNames;
import ticket.*;

import java.nio.charset.CoderResult;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

/**
 * The ConsoleAdministrator class provides methods for interacting with the console and obtaining user input.
 * It includes methods for requesting commands, getting ticket information, venue information, coordinates, and addresses.
 * The class also includes methods for validating user input using the Corrector class.
 *
 * Methods:
 * - commandRequest(): Requests a command from the user and returns it as an array of strings.
 * - getTicketName(): Prompts the user to enter a ticket name and returns it as a string.
 * - getVenueName(): Prompts the user to enter a venue name and returns it as a string.
 * - getTicketCoordinates(): Prompts the user to enter ticket coordinates and returns them as a Coordinates object.
 * - getTicketPrice(): Prompts the user to enter a ticket price and returns it as a Long value.
 * - getVenueCapacity(): Prompts the user to enter a venue capacity and returns it as an integer.
 * - getZipCode(): Prompts the user to enter a zip code and returns it as a string.
 * - getTicketType(): Prompts the user to enter a ticket type and returns it as a TicketType enum value.
 * - getLongPrimitive(): Prompts the user to enter a long value and returns it as a primitive long.
 * - getLongReference(): Prompts the user to enter a long value or null and returns it as a Long object.
 * - getIntReference(): Prompts the user to enter an integer value or null and returns it as an Integer object.
 * - getIntPrimitive(): Prompts the user to enter an integer value and returns it as a primitive int.
 * - getString(): Prompts the user to enter a string value and returns it as a string.
 * - getYesOrNo(): Prompts the user to enter "YES" or "NO" and returns it as a string.
 * - getAddress(): Prompts the user to enter an address and returns it as an Address object.
 * - getStreet(): Prompts the user to enter a street name and returns it as a string.
 * - promt(): Prints a prompt symbol to the console.
 */
public class ConsoleAdministrator {
    static Scanner scanner = new Scanner(System.in);

    /**
     * Prompts the user to enter a command and its arguments in a single line.
     *
     * @return an array of strings containing the command and its arguments
     *         or a default "voidCommand" if the input is invalid
     * @throws PatternSyntaxException if the input command is not valid
     */
    public static String[] commandRequest() {
        System.out.println("Введите название команды и аргументы команды(Если есть) в одну строку: ");
        promt();
        try {
            String s = scanner.nextLine();
            String[] command = s.split(" ");
            if (Corrector.checkCommand(command)) {
                return command;
            } else {
                System.out.println("Неверное название команды или аргумент, для ознакомления со списком команд введите help.");
                return new String[]{"voidCommand"};
            }
        } catch (PatternSyntaxException e) {
            System.out.println("Неверный ввод команды.");
            return new String[]{"voidCommand"};
        }

    }
    /**
     * Prompts the user to enter the name of a ticket.
     *
     * @return the name of the ticket entered by the user
     */
    public static String getTicketName() {
        String value;
        System.out.println("Введите имя билета: ");
        do {
            value = getString();
            if (!Corrector.checkTicketName(value)) {
                System.out.println("Значение не может быть null или пустым. Повторите ввод.");
            }
        } while (!Corrector.checkTicketName(value));
        return value;
    }
    /**
     * Prompts the user to enter the name of the venue and validates the input.
     *
     * @return the name of the venue entered by the user
     */
    public static String getVenueName() {
        String value;
        System.out.println("Введите имя места проведения: ");
        do {
            value = getString();
            if (!Corrector.checkTicketName(value)) {
                System.out.println("Значение не может быть null или пустым. Повторите ввод.");
            }
        } while (!Corrector.checkTicketName(value));
        return value;
    }
    /**
     * Retrieves the coordinates for a ticket.
     *
     * This method prompts the user to enter the x and y coordinates for a ticket. The x coordinate must be greater than -223.
     * If the entered coordinates are not valid, the user will be prompted to enter them again.
     *
     * @return the coordinates for the ticket as a Coordinates object
     */
    public static Coordinates getTicketCoordinates() {
        long x;
        int y;
        do {
            System.out.println("Введите целочисленную координату больше -223 (x): ");
            x = getLongPrimitive();
            System.out.println("Введите целочисленные координаты (y): ");
            y = getIntPrimitive();
            if (!Corrector.checkCoordinatesX(x)) {
                System.out.println("Введите координаты заново. Координата x должна быть больше -223.");
            }
        } while (!Corrector.checkCoordinatesX(x));
        return new Coordinates(x, y);
    }
    /**
     * Prompts the user to enter the price of a ticket.
     *
     * @return the price of the ticket as a Long value, or null if the user enters "null"
     *
     * @throws NoSuchElementException if the user input is invalid or cannot be parsed as a Long value
     *
     * @see Corrector#checkPrice(Long)
     */
    public static Long getTicketPrice() {
        Long value;
        System.out.println("Введите цену билета - целочисленное значение больше нуля или null: ");
        do {
            value = getLongReference();
            if (!Corrector.checkPrice(value)) {
                System.out.println("Значение не может меньше нуля или пустым.Повторите ввод.");
            }
        } while (!Corrector.checkPrice(value));
        return value;
    }
    /**
     * Prompts the user to enter an integer value representing the capacity of a venue.
     * The user will be repeatedly prompted until a valid positive integer value is entered.
     *
     * @return the capacity of the venue as an integer
     */
    public static int getVenueCapacity(){
        int value;
        System.out.println("Введите целое число - вмещяемость места проведения(больше нуля): ");
        do {
            value = getIntPrimitive();
            if (!Corrector.checkCapacity(value)) {
                System.out.println("Значение не может меньше нуля.Повторите ввод.");
            }
        } while (!Corrector.checkCapacity(value));
        return value;
    }
    /**
     * Prompts the user to enter a postal code for the venue. The postal code should contain at least 5 characters or be null.
     * Continues to prompt the user until a valid postal code is entered.
     *
     * @return the entered postal code as a String
     */
    public static String getZipCode() {
        String value;
        System.out.println("Введите почтовый индекс места проведения содержащий не менее 5 символов или null: ");
        do {
            value = getString();
            if (!Corrector.checkZipCode(value)) {
                System.out.println("Почтовый индекс должен содержать не менее 5 символов или null. Повторите ввод.");
            }
        } while (!Corrector.checkZipCode(value));
        return value;
    }
    /**
     * Prompts the user to enter a ticket type by providing a corresponding integer value.
     * The user is prompted to enter an integer value that corresponds to a specific ticket type:
     * 1 - VIP
     * 2 - USUAL
     * 3 - BUDGETARY
     * 4 - CHEAP
     * 5 - null
     * If the entered value is not valid, the user will be prompted to enter it again.
     *
     * @return the ticket type selected by the user as a TicketType enum value
     */
    public static TicketType getTicketType() {
        System.out.println("Введите целочисленное значение соответcвующее типу билета :" +
                " 1 - VIP, 2 - USUAL, 3 - BUDGETARY, 4 - CHEAP, 5 - null");
        int i = getIntPrimitive();
        return switch (i) {
            case 1 -> TicketType.VIP;
            case 2 -> TicketType.USUAL;
            case 3 -> TicketType.BUDGETARY;
            case 4 -> TicketType.CHEAP;
            case 5 -> null;
            default -> {
                System.out.println("Нет типа билета соответсвующего этому числу.Повторите ввод.");
                yield getTicketType();
            }
        };
    }
    /**
     * Prompts the user to enter a long value and returns it as a Long object.
     *
     * @return the Long value entered by the user, or null if the user enters "null"
     * @throws NumberFormatException if the user input is not a valid long value

     */
    public static long getLongPrimitive() {
        long a;
        promt();
        try {
            String s = scanner.nextLine();
            a=Long.parseLong(s);
            return a;
        }catch (NumberFormatException e){
            System.out.println("Неверный тип данных.Введите корректное целочисленое значение.");
            return getLongPrimitive();
        }
    }
    /**
     * Prompts the user to enter a long value and returns it as a primitive long.
     *
     * @return the long value entered by the user
     * @throws NumberFormatException if the user input is not a valid long value
     */
    public static Long getLongReference() {
        Long a;
        promt();
        try {
            String s = scanner.nextLine();
            if (s.equals("null")){
                return null;
            }
            a=Long.parseLong(s);
            return a;
        }catch (NumberFormatException e){
            System.out.println("Неверный тип данных.Введите корректное целочисленое значение или null.");
            return getLongReference();
        }
    }
    public static Integer getIntReference() {
        Integer a;
        promt();
        try {
            String s = scanner.nextLine();
            if (s.equals("null")){
                return null;
            }
            a=Integer.parseInt(s);
            return a;
        }catch (NumberFormatException e){
            System.out.println("Неверный тип данных.Введите корректное целочисленое значение или null.");
            return getIntReference();
        }
    }

    public static int getIntPrimitive() {
        int a;
        promt();
        try {
            String s = scanner.nextLine();
            a=Integer.parseInt(s);
            return a;
        }catch (NumberFormatException e){
            System.out.println("Неверный тип данных.Введите корректное целочисленое значение.");
            return getIntPrimitive();
        }

    }

    private static String getString() {
        String s;
        promt();
        s = scanner.nextLine();
        if (s.equals("null")) {
            s = null;
            return s;
        }
        if(s.trim().isEmpty()){
            s="";
            return s;
        }
        return s;
    }
    public static String getYesOrNo(){
        String s;
        promt();
        s = scanner.nextLine();
        if(s.equals("NO")){
            return "NO";
        }
        if(s.equals("YES")){
            return "YES";
        }
        System.out.println("Введено неверное значение. Введите YES или NO.");
        return getYesOrNo();
    }
    public static Address getAdress(){
        System.out.println("Будете ли вы вводить адрес(YES/NO)?");
        String s=getYesOrNo();
        if(s.equals("NO")){
            return null;
        }
        if (s.equals("YES")){
            return new Address(getStreet(),getZipCode());
        }
        return null;
    }
    public static String getStreet() {
        String value;
        System.out.println("Введите название улицы: ");
        do {
            value = getString();
            if (!Corrector.checkStreet(value)) {
                System.out.println("Значение не может быть null. Повторите ввод.");
            }
        } while (!Corrector.checkStreet(value));
        return value;
    }
    public static void promt(){
        System.out.print(">>");
    }
}

