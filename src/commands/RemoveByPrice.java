package commands;

import utils.CollectionHandler;

public class RemoveByPrice extends AbstractCommand {
    public RemoveByPrice() {
        this.name = CommandNames.removeByPrice;
        this.specification = "Удалить элемент по цене";
    }

    @Override
    public void use() {
        boolean t = true;
        for (int i = 0; i < CollectionHandler.getCollection().size(); i++) {
            if (CollectionHandler.getCollection().get(i).getPrice() == Long.parseLong(this.getInputData())) {
                CollectionHandler.getTicketIdList().remove(CollectionHandler.getCollection().get(i).getId());
                CollectionHandler.getVenueIdList().remove(CollectionHandler.getCollection().get(i).getVenue().getId());
                CollectionHandler.getCollection().remove(i);
                System.out.println("Элемент с ценой " + this.getInputData() + " удален.");
                t = false;
            }
        }
        if (t) {
            System.out.println("Элемента с такой ценой нет.");
        }
    }
}
