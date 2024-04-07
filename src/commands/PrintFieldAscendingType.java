package commands;

import ticket.Ticket;
import utils.CollectionHandler;

import java.util.Collections;
import java.util.LinkedList;
/**
 * The PrintFieldAscendingType class is a subclass of AbstractCommand.
 * It represents a command that prints the values of the 'type' field of all elements in the collection in ascending order.
 * The class overrides the 'use' method from the superclass to implement the functionality of the command.
 *
 * This class provides the following methods:
 * - use(): Prints the values of the 'type' field of all elements in the collection in ascending order.
 *
 * The output is formatted as follows:
 * "Билет с id [ticketId] имеет тип: [ticketType]"
 *
 * If the collection is empty, it prints "Коллекция пуста, нечего выводить."
 *
 * Example usage:
 * PrintFieldAscendingType command = new PrintFieldAscendingType();
 * command.use();
 */
public class PrintFieldAscendingType extends AbstractCommand {
    public PrintFieldAscendingType() {
        this.name = CommandNames.printTypes;
        this.specification = "Выводит значение поля type всех элементов в порядке возврастания";
    }

    @Override
    public void use() {
        LinkedList<Ticket> list = (LinkedList<Ticket>) CollectionHandler.getCollection().clone();
        Collections.sort(list);
        if(list.size()>0){
        for (Ticket ticket : list) {
            System.out.println("Билет с id " + ticket.getId() + " имеет тип: " + ticket.getType());
        }
            System.out.println("Типы выведены.");
        }
        else {
            System.out.println("Коллекция пуста, нечего выводить.");
        }
        }


    }


