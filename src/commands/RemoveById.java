package commands;

import ticket.Ticket;
import utils.CollectionHandler;

import java.util.Collection;
import java.util.Map;
public class RemoveById extends AbstractCommand{
    public RemoveById(){
        this.name=CommandNames.removeById;
        this.specification="Удалаяет элемент по номеру";
    }
    @Override

    public void use() {
        boolean t = true;
        for (int i = 0; i < CollectionHandler.getCollection().size(); i++) {
            if (CollectionHandler.getCollection().get(i).getId() == Long.parseLong(this.getInputData())) {
                CollectionHandler.getTicketIdList().remove(CollectionHandler.getCollection().get(i).getId());
                CollectionHandler.getVenueIdList().remove(CollectionHandler.getCollection().get(i).getVenue().getId());
                CollectionHandler.getCollection().remove(i);
                System.out.println("Элемент с id " + this.getInputData() + " удален.");
                t = false;
            }
        }
        if (t) {
            System.out.println("Элемента с таким id нет.");
        }
    }

}
