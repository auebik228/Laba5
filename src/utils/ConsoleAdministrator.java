package utils;

import commands.Add;
import commands.CommandNames;
import ticket.*;

import java.nio.charset.CoderResult;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

public class ConsoleAdministrator {
    static Scanner scanner = new Scanner(System.in);


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

