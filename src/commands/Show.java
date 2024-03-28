package commands;

import utils.CollectionHandler;

public class Show extends AbstractCommand {
    public Show() {
        this.name = CommandNames.show;
        this.specification = "Отображает данные о коллекции";
    }

    @Override
    public void use() {
        for (int i = 0; i < CollectionHandler.getCollection().size(); i++) {
            System.out.println(CollectionHandler.getCollection().get(i));
        }
    }
}

