package commands;

import ticket.Ticket;
import utils.CollectionHandler;

import java.util.Collections;
import java.util.LinkedList;

public class PrintFieldAscendingType extends AbstractCommand {
    public PrintFieldAscendingType() {
        this.name = CommandNames.printTypes;
        this.specification = "Выводит значение поля type всех элементов в порядке возврастания";
    }

    @Override
    public void use() {
        LinkedList<Ticket> list = (LinkedList<Ticket>) CollectionHandler.getCollection().clone();
        Collections.sort(list);
        for (Ticket ticket : list) {
            System.out.println("Билет с id " + ticket.getId() + " имеет тип: " + ticket.getType());
        }

    }
}

